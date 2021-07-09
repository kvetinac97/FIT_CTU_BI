#include "flib.h"

// FLIB FILE IMPLEMENTATION - BEGIN
struct FileEntry{
    FileEntry(FILE *file,bool writable):file(file),writable(writable){}
    FILE *file;
    bool writable;
    bool operator<(const FileEntry &a)const{ return (void*)file < (void*)a.file; }
};
#define USER_ERR 1
#define SERVER_ERR 2
FileEntry** m;
int m_size;

void flib_init_files(uint16_t number_of_files){
    m_size = number_of_files;
    m = new FileEntry*[m_size]; //(FileEntry**)calloc(m_size, sizeof(FileEntry*));
    for(int i=0; i<m_size; ++i) m[i]=0;
}

void flib_free_files(){
    for(int i=0; i<m_size; ++i){
        if(m[i]!=0){
            printf("file %d was not closed while freeing!\n", i);
            delete(m[i]);
            m[i]=0;
        }
    }
    delete[](m);
}

void flib_open ( uint16_t file_ID, FLIB_Mode mode ){
    if(file_ID >= m_size){ printf("attempt to open file %d out of range [0,%d)\n", file_ID, m_size); exit(USER_ERR); }
    printf("open  %d %s\n", file_ID, (mode==READ?"for reading":"for writing"));
    if(m[file_ID]){ printf("attempt to open file %d while it is already open\n", file_ID); exit(USER_ERR); }
    char file_ID_str[12];
    snprintf(file_ID_str, 12, "%d", file_ID);
    FILE *f = fopen(file_ID_str, mode==READ?"rb+":"wb+");
    if(f==NULL) { printf("attempt to open file %d for reading, but it doesn't exist\n", file_ID); exit(USER_ERR); }
    m[file_ID] = new FileEntry(f,mode==WRITE);
}

void flib_close ( uint16_t file_ID ){
    printf("close %d\n", file_ID);
    if(!m[file_ID]){ printf("attempted close, but file %d is not open\n", file_ID); exit(USER_ERR); exit(USER_ERR); }
    fclose(m[file_ID]->file);
    delete(m[file_ID]);
    m[file_ID]=0;
}

// returns the number of succesfully loaded bits
int64_t flib_read ( uint16_t file_ID, int32_t * buffer, int64_t count ){
    if(!m[file_ID]){ printf("attempted read, but file %d is not open\n", file_ID); exit(USER_ERR); }
    if(m[file_ID]->writable){ printf("attempted read, but file %d is open for writing\n", file_ID); exit(USER_ERR); }
    int red = fread(buffer, sizeof(int), count, m[file_ID]->file);
    printf("read  %d, %ld numbers, %d red: ", file_ID, count, red);
    for(int i=0; i<red; ++i) printf("%d ", buffer[i]); printf("\n");
    return red;
}

int64_t flib_write ( uint16_t file_ID, int32_t * buffer, int64_t count ){
    if(!m[file_ID]){ printf("attempted write, but file %d is not open\n", file_ID); exit(USER_ERR); }
    if(!m[file_ID]->writable){ printf("attempted write, but file %d is NOT open for writing\n", file_ID); exit(USER_ERR); }
    int written = fwrite(buffer, sizeof(int), count, m[file_ID]->file);
    printf("write %d, %ld numbers, %d written: ", file_ID, count, written);
    for(int i=0; i<written; ++i) printf("%d ", buffer[i]); printf("\n");
    return written;
}

void flib_remove ( uint16_t file_ID ){
    if(m[file_ID]){ printf("attempted remove, but file %d is open\n", file_ID); exit(USER_ERR); }
    printf("remov %d\n", file_ID);
    char file_ID_str[12];
    snprintf(file_ID_str, 12, "%d", file_ID);
    if (remove(file_ID_str) != 0) {
      printf("remove of %d failed\n", file_ID);
      exit(USER_ERR);
    }
}

// FLIB FILE IMPLEMENTATION - END
