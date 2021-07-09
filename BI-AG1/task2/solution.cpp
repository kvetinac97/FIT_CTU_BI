#ifndef __PROGTEST__
#include "flib.h"
#endif //__PROGTEST__

class CFile {
    
    int m_File;
    int m_BufferSize;
    FLIB_Mode m_Mode;
    
    int32_t * m_Buffer;
    int m_Size = 0;
    int m_Position = 0;
    
public:
    
    CFile ( int file, int bufferSize, FLIB_Mode mode )
    : m_File ( file ), m_BufferSize ( bufferSize ), m_Mode ( mode ) {
        m_Buffer = new int32_t [bufferSize];
        flib_open ( m_File, mode );
    }
    
    ~CFile () {
        Flush ();
        delete [] m_Buffer;
        flib_close ( m_File );
        
        if ( m_Mode == READ && m_File > 1 )
            flib_remove ( m_File );
    }
    
    bool Read ( int32_t * number ) {
        // Logic error
        if ( m_Mode != READ )
            return false;
        
        // Check cache
        if ( m_Position < m_Size ) {
            *number = m_Buffer[m_Position++];
            return true;
        }
        
        m_Size = flib_read ( m_File, m_Buffer, m_BufferSize );
        if ( m_Size == 0 )
            return false; // EOF
        
        m_Position = 0;
        return Read ( number );
    }
    
    void Write ( const int32_t & number ) {
        // Logic error
        if ( m_Mode != WRITE )
            return;
        
        // Write cache
        if ( m_Position == m_BufferSize ) {
            flib_write ( m_File, m_Buffer, m_BufferSize );
            m_Position = 0;
        }
        
        m_Buffer[m_Position++] = number;
    }
    
    void Flush () {
        if ( m_Mode != WRITE || m_Position == 0 )
            return;
        
        flib_write ( m_File, m_Buffer, m_Position );
        m_Position = 0;
    }
    
};

int comp ( int32_t * a, int32_t * b ) {
    return (*a > *b) - (*a < *b);
}

void ReadMin ( CFile ** arr, int * array, int length, int & min, int & minPos ) {
    min = 2147483647, minPos = -1;
    for ( int i = 0; i < length; i++ )
        if ( arr[i] && array[i] < min ) {
            min = array[i];
            minPos = i;
        }
}

void tarant_allegra ( int32_t in_file, int32_t out_file, int32_t bytes ){
    
    int numC = ( bytes > 2000 ? bytes / 8 : 2 * bytes );
    
    // Step 1: split input file into smaller files
    int32_t * tmp = new int32_t [ numC ];
    int fileid = 2;
    
    flib_open ( in_file, READ );
    while ( true ) {
        int read = flib_read ( in_file, tmp, numC );
        if ( !read )
            break;
        
        flib_open ( fileid, WRITE );
        qsort ( tmp, read, sizeof(*tmp), ( int(*)(const void *, const void *) ) comp );
        flib_write ( fileid, tmp, read );
        flib_close ( fileid++ );
    }
    flib_close ( in_file );
    delete [] tmp;
    
    // Step 2: merge sort files
    CFile ** files = new CFile * [ fileid - 2 ];
    int32_t * nums = new int32_t [ fileid - 2 ];
    int fileCnt = fileid - 2;
    
    for ( int i = 0; i < fileid - 2; i++ ) {
        files[i] = new CFile ( i + 2, numC / fileid, READ );
        files[i]->Read ( nums + i );
    }
    
    CFile outFile ( out_file, numC / fileid, WRITE );

    while ( fileCnt > 0 ) {
        
        int min, minPos;
        ReadMin ( files, nums, fileid - 2, min, minPos );
        
        outFile.Write ( min );
        
        if ( minPos == -1 )
            break;

        if ( !files[minPos]->Read ( nums + minPos ) ) {
            delete files[minPos];
            files[minPos] = nullptr;
            fileCnt --;
        }
        
    }
    
    outFile.Flush();
    
    delete [] files;
    delete [] nums;
    
}


#ifndef __PROGTEST__

uint64_t total_sum_mod;
void create_random(int output, int size){
    total_sum_mod=0;
    flib_open(output, WRITE);
    /* srand(time(NULL)); */
#define STEP 100ll
    int val[STEP];
    for(int i=0; i<size; i+=STEP){
        for(int j=0; j<STEP && i+j < size; ++j){
            val[j]=-1000 + (rand()%(2*1000+1));
            total_sum_mod += val[j];
        }
        flib_write(output, val, (STEP < size-i) ? STEP : size-i);
    }
    flib_close(output);
}

void tarant_allegra ( int in_file, int out_file, int bytes );

void check_result ( int out_file, int SIZE ){
    flib_open(out_file, READ);
    int q[30], loaded, last=-(1<<30), total=0;
    uint64_t current_sum_mod=0;
    while(loaded = flib_read(out_file, q, 30), loaded != 0){
        total += loaded;
        for(int i=0; i<loaded; ++i){
            if(last > q[i]){
                printf("the result file contains numbers %d and %d on position %d in the wrong order!\n", last, q[i], i-1);
                exit(1);
            }
            last=q[i];
            current_sum_mod += q[i];
        }
    }
    if(total != SIZE){
        printf("the output contains %d but the input had %d numbers\n", total, SIZE); exit(1);
    }
    if(current_sum_mod != total_sum_mod){
        printf("the output numbers are not the same as the input numbers\n");
        exit(1);
    }
    flib_close(out_file);
}

int main(int argc, char **argv){
    const uint16_t MAX_FILES = 65535;
    flib_init_files(MAX_FILES);
    int INPUT = 0;
    int RESULT = 1;
    int SIZE = 140;

    create_random(INPUT, SIZE);
    tarant_allegra(INPUT, RESULT, 4000);
    check_result(RESULT, SIZE);

    flib_free_files();
    return 0;
}
#endif //__PROGTEST__
