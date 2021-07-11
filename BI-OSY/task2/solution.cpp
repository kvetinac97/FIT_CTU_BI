#ifndef __PROGTEST__
#include <cstdio>
#include <cstdlib>
#include <cstdint>
#include <cstring>
#include <pthread.h>
#include "common.h"
using namespace std;
#endif /* __PROGTEST__ */

// Helper function for setting bits
inline uint32_t addBits ( uint32_t address, bool writable = true ) {
    address |= ( CCPU::BIT_PRESENT | CCPU::BIT_USER | ( writable ? CCPU::BIT_WRITE : 0 ) );
    return address;
}

// Helper function to get page entry count
inline int pageEntryCount ( uint32_t pages, uint32_t i ) {
    int l = CCPU::PAGE_DIR_ENTRIES;
    int r = (int) ((int) pages - CCPU::PAGE_DIR_ENTRIES * i);
    return l < r ? l : r;
}

// Helper struct and class to run two parameter function on a thread
struct THelper {
    CCPU * m_Src;
    void (* m_EntryPoint) ( CCPU *, void * );
    void * m_Arg;
};

void HelperFun ( THelper * arg );

// Helper queue class
class CQueue {
    struct TItem {
        int m_Value;
        struct TItem * m_Next;
    };
    TItem * m_Begin = nullptr, * m_End = nullptr;
    size_t m_Size = 0;
public:
    size_t size () const { return m_Size; }
    void add ( int value ) {
        auto * it = new TItem {value, nullptr};
        m_Size ++;
        if ( m_Size == 1 ) {
            m_Begin = m_End = it;
            return;
        }
        m_End->m_Next = it;
        m_End = it;
    }
    int  pop () {
        if ( m_Size == 0 )
            return -1;
        int val = m_Begin->m_Value;
        if ( --m_Size == 0 ) {
            delete m_Begin;
            m_Begin = m_End = nullptr;
            return val;
        }
        TItem * tmp = m_Begin;
        m_Begin = m_Begin->m_Next;
        delete tmp;
        return val;
    }
};

// Main CPU class
class CMMU : public CCPU {
    uint32_t m_EntryCount = 0, m_ActivePages = 0, m_RootTable = 0;
    // Helper functions for working with page queue
    static size_t freePageCnt ( bool mutex = true ) {
        if ( mutex ) pthread_mutex_lock ( &m_Mtx );
        size_t size = m_Queue.size();
        if ( mutex ) pthread_mutex_unlock ( &m_Mtx );
        return size;
    }
    static int getFreePage    ( bool mutex = true ) {
        if ( mutex ) pthread_mutex_lock ( &m_Mtx );
        int val = m_Queue.pop();
        m_RefCnt[val] = 1;
        if ( mutex ) pthread_mutex_unlock ( &m_Mtx );
        return val;
    }
    static bool setFreePage   ( int pageId, bool mutex = true ) {
        bool free = false;
        if ( mutex ) pthread_mutex_lock ( &m_Mtx );
        if ( --m_RefCnt[pageId] == 0 ) {
            m_Queue.add(pageId);
            free = true;
        }
        if ( mutex ) pthread_mutex_unlock ( &m_Mtx );
        return free;
    }

    // Helper functions for writing and reading
    static void write    ( uint32_t page, uint32_t offset, uint32_t data, bool writable = true ) {
        auto * memory = (uint32_t *) m_MemoryStart;
        memory[PAGE_DIR_ENTRIES * page + offset] = ( data == 0 ? 0 : addBits(PAGE_SIZE * data, writable) );
    }
    static uint32_t read ( uint32_t page, uint32_t offset ) {
        auto * memory = (uint32_t *) m_MemoryStart;
        return memory[PAGE_DIR_ENTRIES * page + offset] / CCPU::PAGE_SIZE;
    }

    // Increase / decrease size
    void increaseSize ( uint32_t pages, uint32_t entryCount ) {
        for ( size_t i = m_EntryCount; i < entryCount; i++ ) {
            uint32_t entryPage = getFreePage();
            write(m_RootTable, i, entryPage);
            uint32_t minimum = pageEntryCount(pages, i);
            for ( size_t j = 0; j < minimum; j++ ) {
                uint32_t dataPage = getFreePage();
                write(entryPage, j, dataPage);
            }
            for ( size_t j = minimum; j < PAGE_DIR_ENTRIES; j++ )
                write(entryPage, j, 0);
        }

        if ( m_EntryCount > 0 ) {
            size_t i = m_EntryCount - 1;
            uint32_t lastMin   = pageEntryCount(m_ActivePages, i);
            uint32_t  nowMin   = pageEntryCount(pages, i);
            uint32_t entryPage = read(m_RootTable, i);
            for ( size_t j = lastMin; j < nowMin; j++ ) {
                uint32_t dataPage = getFreePage();
                write(entryPage, j, dataPage);
            }
        }

        // set new data
        m_ActivePages = pages;
        m_EntryCount = entryCount;
    }
    void decreaseSize ( uint32_t pages, uint32_t entryCount ) {
        for ( size_t i = entryCount; i < m_EntryCount; i++ ) {
            uint32_t entryPage = read(m_RootTable, i);
            uint32_t minimum   = pageEntryCount(m_ActivePages, i);
            for ( size_t j = 0; j < minimum; j++ ) {
                uint32_t dataPage = read(entryPage, j);
                if ( setFreePage(dataPage) )
                    write(entryPage, j, 0);
            }
            if ( setFreePage(entryPage) )
                write(m_RootTable, i, 0);
        }

        if ( entryCount > 0 ) {
            size_t i = entryCount - 1;
            uint32_t   lastMin = pageEntryCount(m_ActivePages, i);
            uint32_t    nowMin = pageEntryCount(pages, i);
            uint32_t entryPage = read(m_RootTable, i);
            for ( size_t j = nowMin; j < lastMin; j++ ) {
                uint32_t dataPage = read(entryPage, j);
                if ( setFreePage(dataPage) )
                    write(entryPage, j, 0);
            }
        }

        m_ActivePages = pages;
        m_EntryCount = entryCount;
    }

    // Copy-on-write
    void Copy ( CMMU * source ) {
        // Copy entries
        m_EntryCount = source->m_EntryCount;
        m_ActivePages = source->m_ActivePages;

        pthread_mutex_lock(&m_Mtx);
        for ( size_t i = 0; i < m_EntryCount; i++ ) {
            uint32_t entryPage = read(source->m_RootTable, i);
            uint32_t newPage   = getFreePage(false);
            write(m_RootTable, i, newPage);

            // Copy to new page and set WRITE bit to false
            uint32_t minimum = pageEntryCount(m_ActivePages, i);
            for ( size_t j = 0; j < minimum; j++ ) {
                uint32_t dataPage = read(entryPage, j);
                m_RefCnt[dataPage]++;
                write(entryPage, j, dataPage, false);
                write(newPage, j, dataPage, false);
            }
        }
        pthread_mutex_unlock(&m_Mtx);
    }
protected:
    // Handle copy on write
    bool pageFaultHandler ( uint32_t address, bool write ) override {
        if ( !write ) return false;
        pthread_mutex_lock(&m_Mtx);

        const uint32_t reqMask = BIT_PRESENT | BIT_USER | BIT_WRITE;
        const uint32_t reqMaskRead = BIT_PRESENT | BIT_USER;

        uint32_t * level1 = (uint32_t *)(m_MemStart + (m_PageTableRoot & ADDR_MASK)) + (address >> 22);
        uint32_t * level2 = (uint32_t *)(m_MemStart + (*level1 & ADDR_MASK )) + ((address >> OFFSET_BITS) & (PAGE_DIR_ENTRIES - 1));

        if ( (*level2 & reqMask) != reqMask ) {
            // Control whether we can read page or it is invalid
            if ( (*level2 & reqMaskRead) != reqMaskRead || freePageCnt(false) < 1 ) {
                pthread_mutex_unlock(&m_Mtx);
                return false;
            }

            uint32_t dataPage = ((*level2) / PAGE_SIZE);
            uint32_t newPage = getFreePage(false);
            auto * memory = (uint32_t *) m_MemoryStart;

            // Copy page data
            for ( uint32_t i = 0; i < PAGE_DIR_ENTRIES; i++ )
                memory[PAGE_DIR_ENTRIES * newPage + i] = memory[PAGE_DIR_ENTRIES * dataPage + i];

            // Set new page location
            setFreePage(dataPage, false);
            *level2 = addBits(newPage * PAGE_SIZE);
            pthread_mutex_unlock(&m_Mtx);
            return true;
        }

        pthread_mutex_unlock(&m_Mtx);
        return false;
    }
public:
    static CQueue m_Queue;
    static pthread_mutex_t m_Mtx;
    static uint8_t * m_MemoryStart;
    static uint32_t m_TotalPages;
    static uint8_t m_ProcessCnt;
    static pthread_cond_t m_Cond;
    static uint8_t * m_RefCnt;

    // Init & Destroy CPU
    CMMU () : CCPU ( m_MemoryStart, -1 ) {
        m_RootTable = getFreePage();
        CCPU::m_PageTableRoot = m_RootTable * PAGE_SIZE;
        for ( uint32_t i = 0; i < PAGE_DIR_ENTRIES; i++ )
            write(m_RootTable, i, 0);
    }
    ~CMMU () override {
        for ( size_t i = 0; i < m_EntryCount; i++ ) {
            uint32_t entryPage = read(m_RootTable, i);
            int minim = pageEntryCount(m_ActivePages, i);
            for ( int j = 0; j < minim; j++ ) {
                uint32_t dataPage = read(entryPage, j);
                write(entryPage, j, 0);
                setFreePage(dataPage);
            }
            write(m_RootTable, i, 0);
            setFreePage(entryPage);
        }
        setFreePage(m_RootTable);
        m_RootTable = 0;

        pthread_mutex_lock(&m_Mtx);
        m_ProcessCnt --;
        pthread_cond_signal(&m_Cond);
        pthread_mutex_unlock(&m_Mtx);
    }

    // Memory limit getter & setter
    uint32_t GetMemLimit () const override { return m_ActivePages; }
    bool     SetMemLimit ( uint32_t pages ) override {
        uint32_t entryCount = pages == 0 ? 0 : (pages - 1) / PAGE_DIR_ENTRIES + 1;
        if ( pages == m_TotalPages ) // nothing changed
            return true;

        // decrease size
        if ( pages < m_ActivePages ) {
            decreaseSize(pages, entryCount);
            return true;
        }

        // increase size
        if ( entryCount + ((int) pages - (int) m_EntryCount - (int) m_ActivePages) > freePageCnt() )
            return false; // not enough space

        increaseSize(pages, entryCount);
        return true;
    }

    // Dummy function to create new process
    bool NewProcess ( void * processArg, void (* entryPoint) ( CCPU *, void * ), bool copyMem ) override {
        pthread_mutex_lock(&m_Mtx);
        if ( m_ProcessCnt > 64 ) { // too many processes
            pthread_mutex_unlock(&m_Mtx);
            return false;
        }

        // Check process count
        pthread_t thread;
        m_ProcessCnt ++;
        pthread_mutex_unlock(&m_Mtx);
        CMMU * cmmu = new CMMU;

        // Copy-on-write memory
        if ( copyMem )
            cmmu->Copy(this);

        // Create new thread
        auto * param = new THelper {cmmu, entryPoint, processArg};
        pthread_create(&thread, nullptr, (void *(*)(void *))(HelperFun), (void *) param);
        return true;
    }
};

// Global variable initialization
CQueue CMMU::m_Queue;
uint8_t * CMMU::m_MemoryStart = nullptr;
uint32_t CMMU::m_TotalPages = 0;
pthread_mutex_t CMMU::m_Mtx;
uint8_t CMMU::m_ProcessCnt = 0;
pthread_cond_t CMMU::m_Cond;
uint8_t * CMMU::m_RefCnt = nullptr;

// Helper function to be able to run two parameter function on thread
void HelperFun ( THelper * arg ) {
    arg->m_EntryPoint(arg->m_Src, arg->m_Arg);
    delete arg->m_Src;
    delete arg;
}

void MemMgr ( void * mem, uint32_t totalPages, void * processArg, void (* mainProcess) ( CCPU *, void * )) {
    // Init global variables
    pthread_mutex_init ( &CMMU::m_Mtx, nullptr  );
    pthread_cond_init  ( &CMMU::m_Cond, nullptr );

    CMMU::m_MemoryStart = (uint8_t *) mem;
    CMMU::m_TotalPages  = totalPages;
    CMMU::m_ProcessCnt  = 1;
    CMMU::m_RefCnt      = new uint8_t [ totalPages ];

    for ( size_t i = 0; i < totalPages; i++ ) {
        CMMU::m_Queue.add(i);
        CMMU::m_RefCnt[i] = 0;
    }

    // Init CCPU
    CMMU * mmu = new CMMU;
    mainProcess(mmu, processArg);
    delete mmu;

    pthread_mutex_lock(&CMMU::m_Mtx);

    // Wait until all threads finish
    while ( CMMU::m_ProcessCnt > 0 )
        pthread_cond_wait(&CMMU::m_Cond, &CMMU::m_Mtx);

    // Clear queue and reference count for repetitive use
    while ( CMMU::m_Queue.pop() != -1 );
    delete [] CMMU::m_RefCnt;

    pthread_mutex_unlock(&CMMU::m_Mtx);
    pthread_cond_destroy(&CMMU::m_Cond);
    pthread_mutex_destroy(&CMMU::m_Mtx);

    // Finish
    printf("Main thread exit!\n");
}
