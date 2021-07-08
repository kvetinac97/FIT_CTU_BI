#ifndef __PROGTEST__
#include <cstring>
#include <cstdlib>
#include <cstdio>
#include <cctype>
#include <climits>
#include <cassert>
#include <cstdint>
#include <iostream>
#include <iomanip>
#include <fstream>
#include <sstream>
#include <map>
#include <vector>
#include <algorithm>
#include <set>
#include <queue>
#include <memory>
#include <functional>
#include <stdexcept>
using namespace std;
#endif /* __PROGTEST__ */

// Constants
const int PREFIX_16BIT = 49152;
const int PREFIX_24BIT = 14680064;
const int PREFIX_32BIT = -268435456;

// Tree structure
struct CNode {
    
    CNode * m_Left  = NULL;
    CNode * m_Right = NULL;
    int     m_Val   =   -1; // numeric representation
    int     m_Size  =    1; // in bytes
    int m_Priority  =   -1;
    
};

struct CChar {
    
    int m_Val = -1;
    int m_Size = 1;
    int m_Freq = 0;
    
    friend bool operator < ( const CChar & l, const CChar & r ) {
        return l.m_Val > r.m_Val;
    }
    
    friend bool operator > ( const CChar & l, const CChar & r ) {
        return l.m_Freq > r.m_Freq;
    }
    
    friend bool operator == ( const CChar & l, const CChar & r ) {
        return l.m_Val == r.m_Val;
    }
    
};

// Main file wrapper class
class CFile {
    
    // File streams
    ifstream m_IS;
    ofstream m_OS;
    
    // Bit reading helpers
    bool m_Bits[8];
    int m_Read = 8;
    
    // Bit writing helpers
    bool m_Write[8];
    int m_Written = 0;
    
    // Main properties
    CNode * m_Root = NULL;
    
    // Utility functions
    
    bool loadBit ( bool & bit ) {
        
        // Load new byte
        if ( m_Read == 8 ) {
            char c;
            if ( !m_IS.read( &c, sizeof(c) ) )
                return false;
            
            // Parse to bits
            for ( int i = 7; i >= 0; i-- ) {
                bool bit = (bool) ( ( c >> i ) & 1 );
                m_Bits[7-i] = bit;
            }
            
            m_Read = 0;
        }
        
        bit = m_Bits[m_Read++];
        return true;
        
    }
    
    bool writeBit ( const bool bit ) {
        
        m_Write[m_Written++] = bit;
        
        if ( m_Written == 8 ) {
            char num = 0;
            
            // Writing each bit
            for ( int i = 7; i >= 0; i-- )
                num += ( m_Write[7-i] << i );
            
            if ( !m_OS.write(&num, sizeof(num)) )
                return false;
            m_OS.flush();
            
            m_Written = 0;
        }
        
        return true;
        
    }
    
    bool writeByte ( const char b ) {
        
        bool success = 1;
        
        // Parse to bits
        for ( int i = 7; i >= 0; i-- ) {
            bool bit = (bool) ( ( b >> i ) & 1 );
            success = success && writeBit ( bit );
        }
        
        return success;
        
    }
    
    // Zero byte padding, must be called before the end !!!
    bool flush () {
        if ( m_Written == 0 ) // everything is written
            return true;
        
        bool success = true;
        
        for ( int i = 8 - m_Written; i > 0; i-- )
            success = success && writeBit ( 0 );
        
        return success;
        
    }
    
    // Get UTF-8 character size
    bool loadCharSize ( int & num, int & numSize, int & size ) {
        
        bool bit;
        if ( !loadBit(bit) )
            return false;
                
        // 8bit
        if ( !bit ) {
            size = 7;
            numSize = 1;
            return true;
        }
        
        // Invalid UTF-8
        if ( !loadBit(bit) || !bit || !loadBit(bit) )
            return false;

        // 16bit
        if ( !bit ) {
            num += PREFIX_16BIT;
            size = 13;
            numSize = 2;
            return true;
        }
        
        if ( !loadBit(bit) )
            return false;
        
        // 24bit
        if ( !bit ) {
            num += PREFIX_24BIT;
            size = 20;
            numSize = 3;
            return true;
        }
        
        // 32bit
        num += PREFIX_32BIT;
        size = 28;
        numSize = 4;

        return true;
        
    }
    
    // Load UTF-8 character to tree
    bool loadChar ( int & num, int & numSize, bool & err ) {
        
        bool bit;
        int size;
        num = numSize = 0;
        
        // Load type ( 8bit, 16bit, 24bit, 32bit )
        if ( !loadCharSize ( num, numSize, size ) )
            return false;
        
        for ( int i = size - 1; i >= 0; i-- ) {
            if ( !loadBit(bit) ) {
                err = true;
                return false;
            }
            
            num += ( bit << i );
        }
        
        unsigned char c1 = 0, c2 = 0, c3 = 0, c4 = 0;

        c1 = ( (num >> 24) & 0xFF );
        c2 = ( (num >> 16) & 0xFF );
        c3 = ( (num >> 8) & 0xFF );
        c4 = ( num & 0xFF ); // no need to validate
        
        if ( !validateUTF8( c1, 1, numSize ) || !validateUTF8( c2, 2, numSize ) || !validateUTF8( c3, 3, numSize ) || !validateUTF8( c4, 4, numSize ) ) {
            err = true;
            return false;
        }
        
        // Strange condition, UTF-8 size limit 1114111
        if ( c1 >= 244 && c2 >= 144 && c3 >= 128 && c4 >= 128 ) {
            err = true;
            return false;
        }
        
        return true;
        
    }
    
    // Write UTF-8 character
    bool writeChar ( const int num, const int size ) {
        
        unsigned char
        c1 = ( (num >> 24) & 0xFF ),
        c2 = ( (num >> 16) & 0xFF ),
        c3 = ( (num >> 8) & 0xFF ),
        c4 = ( num & 0xFF );
        
        // Strange condition, UTF-8 size limit 1114111
        if ( c1 >= 244 && c2 >= 144 && c3 >= 128 && c4 >= 128 )
            return false;
                            
        if ( !validateUTF8( c1, 1, size ) || !validateUTF8( c2, 2, size ) || !validateUTF8( c3, 3, size ) || !validateUTF8( c4, 4, size ) )
            return false;
        
        if ( size == 4 && !writeByte(c1) )
            return false;

        if ( size > 2 && !writeByte(c2) )
            return false;

        if ( size > 1 && !writeByte(c3) )
            return false;

        if ( !writeByte(c4) )
            return false;

        return true;
        
    }
    
    // Recursive tree loading
    bool loadChildren ( CNode * & node ) {
        
        bool bit, err;
        if ( !loadBit ( bit ) ) // value bit
            return false;
        
        node = new CNode;
        
        // Has value
        if ( bit )
            return loadChar ( node->m_Val, node->m_Size, err );
        
        // Load children
        return loadChildren ( node->m_Left ) &&
            loadChildren ( node->m_Right );
        
    }
    
    // Load chunk size (12 bits)
    bool loadChunkSize ( int & num ) {
        
        bool bit;
        num = 0;
        
        // Loading each bit
        for ( int i = 11; i >= 0; i-- ) {
            if ( !loadBit(bit) )
                return false;
            
            num += ( bit << i );
        }
        
        return true;
        
    }
    
    bool writeChunkSize ( const int num ) {
        
        bool success = 1;
        
        // Parse to bits
        for ( int i = 11; i >= 0; i-- ) {
            bool bit = (bool) ( ( num >> i ) & 1 );
            success = success && writeBit ( bit );
        }
        
        return success;
        
    }
    
    // Character loading
    bool letterFromTree ( int & num, int & size ) {
        
        CNode * node = m_Root;

        while ( node->m_Val == -1 ) {
            // bad tree
            if ( !node )
                return false;
            
            bool bit; // left/right children?
            if ( !loadBit ( bit ) )
                return false;
            
            node = bit ? node->m_Right : node->m_Left;
        }
        
        num = node->m_Val;
        size = node->m_Size;
        return true;
        
    }
    
    // Check UTF-8 format
    bool validateUTF8 ( unsigned char c, int pos, int size ) {
        
        // Masks
        int andMasks[4][4] = {
            {0b00000000, 0b00000000, 0b00000000, 0b00000000},
            {0b00000000, 0b00000000, 0b11000000, 0b10000000}, // size 2
            {0b00000000, 0b11100000, 0b10000000, 0b10000000}, // size 3
            {0b11110000, 0b10000000, 0b10000000, 0b10000000}  // size 4
        };
        int andResults[4][4] = {
            {0b00000000, 0b00000000, 0b00000000, 0b00000000},
            {0b00000000, 0b00000000, 0b11000000, 0b10000000},
            {0b00000000, 0b11100000, 0b10000000, 0b10000000},
            {0b11110000, 0b10000000, 0b10000000, 0b10000000}
        };
        int orMasks[4][4] = {
            {0b00000000, 0b00000000, 0b00000000, 0b11111111},
            {0b00000000, 0b00000000, 0b00011111, 0b00111111},
            {0b00000000, 0b00001111, 0b00111111, 0b00111111},
            {0b00000111, 0b00111111, 0b00111111, 0b00111111}
        };
        int orResults[4][4] = {
            {0b00000000, 0b00000000, 0b00000000, 0b11111111},
            {0b00000000, 0b00000000, 0b11011111, 0b10111111},
            {0b00000000, 0b11101111, 0b10111111, 0b10111111},
            {0b11110111, 0b10111111, 0b10111111, 0b10111111}
        };
        
        // Index change for easier manipulation
        size -= 1; pos -= 1;
        
        return ( ( ( c & andMasks[size][pos] ) == andResults[size][pos] )
                && ( ( c | orMasks[size][pos] ) == orResults[size][pos] ) );
                
    }
    
    // Chunk writing
    bool writeChunk ( int chunkSize ) {
        
        for ( int i = 0; i < chunkSize; i++ ) {
            
            // Read from tree
            int num, size;
            
            if ( !letterFromTree ( num, size ) )
                return false;
                        
            if ( !writeChar ( num, size ) )
                return false;
            
        }
        
        return true;
        
    }
    
    // ENCRYPTION
    
    bool writeTree ( CNode * node ) {
        
        bool bit = ( node->m_Val != -1 );
        
        if ( !writeBit ( bit ) )
            return false;
        
        if ( bit )
            return writeChar ( node->m_Val, node->m_Size );
        
        return writeTree ( node->m_Left ) && writeTree ( node->m_Right );
        
    }
        
    bool generateTree ( int & count ) {
        // Load every char and generate table
        std::map < int, CChar * > chars;
        
        count = 0;
        int val = 0, size = 0;
        bool err = false;
        
        while ( loadChar ( val, size, err ) && !err ) {
            
            count ++;
            
            if ( chars.find(val) != chars.end() ) {
                
                chars[chars.find(val)->first]->m_Freq++;
                continue;
                
            }
            
            CChar * c = new CChar;
            c->m_Val = val;
            c->m_Size = size;
            c->m_Freq = 1;
            chars[val] = c;
        }
                
        if ( count < 2 || err || chars.size() < 2 )
            return false;
                
        auto cmp = [](CNode * l, CNode * r) { return l->m_Priority > r->m_Priority; };
        std::priority_queue<CNode *, vector<CNode *>, decltype(cmp)> q(cmp);
        
        std::map < int, CChar * > ::iterator iterator = chars.begin();
        while ( iterator != chars.end() ) {
                        
            CNode * node = new CNode;
            node->m_Val = chars[iterator->first]->m_Val;
            node->m_Size = chars[iterator->first]->m_Size;
            node->m_Priority = chars[iterator->first]->m_Freq;
            
            q.push ( node );
            iterator++;
            
        }
        
        while ( q.size() > 1 ) {
            CNode * top1 = q.top();
            q.pop();
            CNode * top2 = q.top();
            q.pop();
            
            CNode * tmp = new CNode;
            tmp->m_Val = -1;
            tmp->m_Priority = top1->m_Priority + top2->m_Priority;
            tmp->m_Left = top1;
            tmp->m_Right = top2;
            
            q.push ( tmp );
        }

        m_Root = q.top();
        q.pop();
        
        return writeTree ( m_Root );
                
    }
    
    bool searchTree ( CNode * node, int val, int tmp, int depth, int & size, int & bitsize ) {
        
        if ( !node )
            return false;
        
        if ( node->m_Val == val ) {
            size = tmp;
            bitsize = depth;
            return true;
        }
        
        return searchTree ( node->m_Left, val, tmp, depth + 1, size, bitsize ) ||
            searchTree ( node->m_Right, val, tmp + (1 << depth), depth + 1, size, bitsize );
                
    }
    
    bool writeEncoded ( const int count ) {
        
        int val = -1, size = 0, cnt = 0;
        bool success = true, err = false;
        while ( cnt < count && loadChar ( val, size, err ) ) {

            int size, bitsize;
            if ( !searchTree ( m_Root, val, 0, 0, size, bitsize ) )
                return false;
            
            for ( int i = 0; i < bitsize; i++ ) {
                bool bit = (bool) ( ( size >> i ) & 1 );
                success = success && writeBit ( bit );
            }
            
            cnt++;

        }
        
        if ( err )
            return false;
        
        return success;
        
    }
    
public:
    
    CFile ( const char * inFileName, const char * outFileName ) {
        // Files
        m_IS = ifstream ( inFileName, ios::binary );
        m_OS = ofstream ( outFileName, ios::binary );
    }
    
    bool decompress () {
        // Bad file / tree
        if ( !m_IS || !m_OS || !loadChildren ( m_Root ) )
            return false;
                
        // Chunk loading
        bool loadingChunks = false;
        int chunkSize = 4096;

        do {
            // Chunk type ( 0 = LAST, 1 = 4096size )
            if ( !loadBit ( loadingChunks ) )
                return false;

            // Last chunk
            if ( !loadingChunks && !loadChunkSize ( chunkSize ) )
                return false;
            
            // Write chunk itself
            if ( !writeChunk ( chunkSize ) )
                return false;
        }
        while ( loadingChunks );

        // Returning
        return !m_IS.fail() && !m_OS.fail();
    }
    
    bool compress () {
        // Bad file
        if ( !m_IS || !m_OS )
            return false;
                
        // Tree generation
        int count = 0;
        if ( !generateTree(count) )
            return false;
                        
        m_IS.clear (); // clear status
        m_IS.seekg ( 0, ios::beg ); // reset
                
        while ( count >= 0 ) {
            
            if ( count >= 4096 ) {
                
                if ( !writeBit ( 1 ) )
                    return false;
                
                count -= 4096;
                                
                if ( !writeEncoded (4096) )
                    return false;
                
                continue;
            }
            
            if ( !writeBit ( 0 ) || !writeChunkSize ( count ) )
                return false;
            
            if ( !writeEncoded (count) )
                return false;
            
            count = -1;
            
        }
        
        return flush () && !m_IS.fail() && !m_OS.fail();
        
    }
    
};

// Decompression wrapper function
bool decompressFile ( const char * inFileName, const char * outFileName ) {
    CFile file ( inFileName, outFileName );
    return file.decompress();
}

// Compression wrapper function
bool compressFile ( const char * inFileName, const char * outFileName ) {
    CFile file ( inFileName, outFileName );
    return file.compress();
}

#ifndef __PROGTEST__
int main ( void ) {
        
    assert ( decompressFile ( "tests/test0.huf", "tests/test0.ref" ) );
    assert ( compressFile   ( "tests/test0.orig", "tests/test0.hud" ) );
    assert ( decompressFile ( "tests/test0.hud", "tests/test0.ori" ) );
    assert ( decompressFile ( "tests/test1.huf", "tests/test1.ref" ) );
    assert ( compressFile   ( "tests/test1.orig", "tests/test1.hud" ) );
    assert ( decompressFile ( "tests/test1.hud", "tests/test1.ori" ) );
    assert ( decompressFile ( "tests/test2.huf", "tests/test2.ref" ) );
    assert ( compressFile   ( "tests/test2.orig", "tests/test2.hud" ) );
    assert ( decompressFile ( "tests/test2.hud", "tests/test2.ori" ) );
    assert ( decompressFile ( "tests/test3.huf", "tests/test3.ref" ) );
    assert ( compressFile   ( "tests/test3.orig", "tests/test3.hud" ) );
    assert ( decompressFile ( "tests/test3.hud", "tests/test3.ori" ) );
    assert ( decompressFile ( "tests/test4.huf", "tests/test4.ref" ) );
    assert ( compressFile   ( "tests/test4.orig", "tests/test4.hud" ) );
    assert ( decompressFile ( "tests/test4.hud", "tests/test4.ori" ) );
    assert ( ! decompressFile ( "tests/test5.huf", "tempfile" ) );
    assert ( ! compressFile ( "tempflex", "tests/test5.hud" ) );

    assert ( decompressFile ( "tests/extra0.huf", "tests/extra0.ref" ) );
    assert ( compressFile   ( "tests/extra0.orig", "tests/extra0.hud" ) );
    assert ( decompressFile ( "tests/extra0.hud", "tests/extra0.ori" ) );
    assert ( decompressFile ( "tests/extra1.huf", "tests/extra1.ref" ) );
    assert ( compressFile   ( "tests/extra1.orig", "tests/extra1.hud" ) );
    assert ( decompressFile ( "tests/extra1.hud", "tests/extra1.ori" ) );
    assert ( decompressFile ( "tests/extra2.huf", "tests/extra2.ref" ) );
    assert ( compressFile   ( "tests/extra2.orig", "tests/extra2.hud" ) );
    assert ( decompressFile ( "tests/extra2.hud", "tests/extra2.ori" ) );
    assert ( decompressFile ( "tests/extra3.huf", "tests/extra3.ref" ) );
    assert ( compressFile   ( "tests/extra3.orig", "tests/extra3.hud" ) );
    assert ( decompressFile ( "tests/extra3.hud", "tests/extra3.ori" ) );
    assert ( decompressFile ( "tests/extra4.huf", "tests/extra4.ref" ) );
    assert ( compressFile   ( "tests/extra4.orig", "tests/extra4.hud" ) );
    assert ( decompressFile ( "tests/extra4.hud", "tests/extra4.ori" ) );
    assert ( decompressFile ( "tests/extra5.huf", "tests/extra5.ref" ) );
    assert ( compressFile   ( "tests/extra5.orig", "tests/extra5.hud" ) );
    assert ( decompressFile ( "tests/extra5.hud", "tests/extra5.ori" ) );
    assert ( decompressFile ( "tests/extra6.huf", "tests/extra6.ref" ) );
    assert ( compressFile   ( "tests/extra6.orig", "tests/extra6.hud" ) );
    assert ( decompressFile ( "tests/extra6.hud", "tests/extra6.ori" ) );
    assert ( decompressFile ( "tests/extra7.huf", "tests/extra7.ref" ) );
    assert ( compressFile   ( "tests/extra7.orig", "tests/extra7.hud" ) );
    assert ( decompressFile ( "tests/extra7.hud", "tests/extra7.ori" ) );
    assert ( decompressFile ( "tests/extra8.huf", "tests/extra8.ref" ) );
    assert ( compressFile   ( "tests/extra8.orig", "tests/extra8.hud" ) );
    assert ( decompressFile ( "tests/extra8.hud", "tests/extra8.ori" ) );
    assert ( decompressFile ( "tests/extra9.huf", "tests/extra9.ref" ) );
    assert ( compressFile   ( "tests/extra9.orig", "tests/extra9.hud" ) );
    assert ( decompressFile ( "tests/extra9.hud", "tests/extra9.ori" ) );
    
    return 0;
    
}
#endif /* __PROGTEST__ */
