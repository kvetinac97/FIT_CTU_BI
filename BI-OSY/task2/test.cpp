#include <cstdio>
#include <cstdint>
#include <cstdarg>
#include "pthread.h"

#include "common.h"

// === CCPU ===

bool CCPU::ReadInt  ( uint32_t address, uint32_t & value ) {
    if ( address & 0x3 ) return false; // not aligned
    uint32_t * addr = virtual2Physical ( address, false );
    if ( ! addr ) return false;
    value = *addr;
    return true;
}
bool CCPU::WriteInt ( uint32_t address, uint32_t value   ) {
    if ( address & 0x3 ) return false; // not aligned
    uint32_t * addr = virtual2Physical ( address, true );
    if ( ! addr ) return false;
    *addr = value;
    return true;
}
uint32_t * CCPU::virtual2Physical ( uint32_t address, bool write ) {
    const uint32_t reqMask = BIT_PRESENT | BIT_USER | (write ? BIT_WRITE : 0 );
    const uint32_t orMask = BIT_REFERENCED | (write ? BIT_DIRTY : 0);

    while ( true ) {
        uint32_t * level1 = (uint32_t *)(m_MemStart + (m_PageTableRoot & ADDR_MASK)) + (address >> 22);

        if ( (*level1 & reqMask ) != reqMask ) {
            if ( pageFaultHandler ( address, write ) ) continue;
            return nullptr;
        }

        uint32_t * level2 = (uint32_t *)(m_MemStart + (*level1 & ADDR_MASK )) + ((address >> OFFSET_BITS) & (PAGE_DIR_ENTRIES - 1));
        if ( (*level2 & reqMask ) != reqMask ) {
            if ( pageFaultHandler ( address, write ) ) continue;
            return nullptr;
        }

        *level1 |= orMask;
        *level2 |= orMask;
        return (uint32_t *)(m_MemStart + (*level2 & ADDR_MASK) + (address & ~ADDR_MASK));
    }
}

// === TEST ENVIRONMENT ===

const uint32_t PROCESS_MAX = 64;
static int g_Fail;
static pthread_mutex_t g_Mtx;

void        reportError ( const char * prefix, ... ) {
    va_list va;

    pthread_mutex_lock ( &g_Mtx );
    g_Fail ++;
    va_start ( va, prefix );
    vprintf (prefix, va );
    va_end ( va );
    pthread_mutex_unlock ( &g_Mtx );
}
void        checkResize ( CCPU * cpu, uint32_t limit ) {
    cpu -> SetMemLimit ( limit );
    if ( cpu -> GetMemLimit () != limit )
        reportError ( "Mem limit error: %d expected, %d returned\n", limit, cpu -> GetMemLimit () );
}

static void checkWrite ( CCPU * cpu, uint32_t addr, uint32_t val ) {
    if ( ! cpu -> WriteInt ( addr, val ) )
        reportError ( "failed: WriteInt ( %x, %u )\n", addr, val );
}
static void checkRead  ( CCPU * cpu, uint32_t addr, uint32_t expected ) {
    uint32_t val;
    if ( ! cpu -> ReadInt ( addr, val ) )
        reportError ( "failed: ReadInt ( %x, ... )\n", addr );
    else if ( val != expected )
        reportError ( "read mismatch, addr %x, read: %u, expected: %u\n", addr, val, expected );
}

void wTest   ( CCPU * cpu, uint32_t fromPage, uint32_t toPage ) {
    for ( uint32_t addr = fromPage * CCPU::PAGE_SIZE; addr < toPage * CCPU::PAGE_SIZE; addr += 32 )
        checkWrite ( cpu, addr, addr ^ fromPage );
}
void rTest   ( CCPU * cpu, uint32_t fromPage, uint32_t toPage ) {
    for ( uint32_t addr = fromPage * CCPU::PAGE_SIZE; addr < toPage * CCPU::PAGE_SIZE; addr += 32 )
        checkRead ( cpu, addr, addr ^ fromPage );
}
void iTest   ( CCPU * cpu, uint32_t pages ) {
    uint32_t val;

    for ( uint32_t i = pages * CCPU::PAGE_SIZE; i < 4 * pages * CCPU::PAGE_SIZE; i += 744 )
        if ( cpu -> ReadInt ( i, val ) )
            reportError ( "readInt (%d) succeeds, shall fail\n", i );

    for ( uint32_t i = pages * CCPU::PAGE_SIZE; i < 4 * pages * CCPU::PAGE_SIZE; i += 952 )
        if ( cpu -> WriteInt ( i, i - 300 ) )
            reportError ( "writeInt (%d) succeeds, shall fail\n", i );

    for ( uint32_t i = pages * CCPU::PAGE_SIZE; i < 0xfff00000; i += 500000 )
        if ( cpu -> ReadInt ( i, val ) )
            reportError ( "readInt (%d) succeeds, shall fail\n", i );

    for ( uint32_t i = pages * CCPU::PAGE_SIZE; i < 0xfff00000; i += 500000 )
        if ( cpu -> WriteInt ( i, i - 300 ) )
            reportError ( "writeInt (%d) succeeds, shall fail\n", i );
}
void rwTest  ( CCPU * cpu, uint32_t fromPage, uint32_t toPage ) {
    wTest ( cpu, fromPage, toPage );
    rTest ( cpu, fromPage, toPage );
}
void rwiTest ( CCPU * cpu, uint32_t fromPage, uint32_t toPage ) {
    wTest ( cpu, fromPage, toPage );
    rTest ( cpu, fromPage, toPage );
    iTest ( cpu, toPage );
}

void testStart () {
    pthread_mutex_init ( &g_Mtx, nullptr );
    g_Fail = 0;
}
void testEnd   ( const char * prefix ) {
    pthread_mutex_destroy ( &g_Mtx );
    printf ( "%s: %d failures\n", prefix, g_Fail );
}

// === INIT TEST ===

static void seqTest1 ( CCPU * cpu, void * arg ) {
    for ( uint32_t i = 0; i <= 2000; i ++ )
        checkResize ( cpu, i );

    checkResize ( cpu, 5000 );

    for ( int i = 2000; i >= 0; i -- )
        checkResize ( cpu, i );
}
static void seqTest2 ( CCPU * cpu, void * arg ) {
    checkResize ( cpu, 1000 );
    rwiTest ( cpu, 0, 1000 );

    checkResize ( cpu, 2345 );
    rwiTest ( cpu, 0, 2345 );

    checkResize ( cpu, 789 );
    rwiTest ( cpu, 0, 789 );
}

// === SEQUENTIAL TEST ===

namespace TestSequential {
    struct TChild {
        pthread_mutex_t  m_Mtx;
        pthread_cond_t   m_Cond;
        int              m_Pages;
        bool             m_Free;
    };
    static void childProcess ( CCPU * cpu, TChild * arg ) {
        checkResize ( cpu, arg -> m_Pages );
        rwiTest ( cpu, 0, arg -> m_Pages );
        if ( arg -> m_Free ) checkResize ( cpu, 0 );
        pthread_mutex_lock ( &arg -> m_Mtx );
        pthread_cond_signal ( &arg -> m_Cond );
        pthread_mutex_unlock ( &arg -> m_Mtx );
    }
    static void initProcess  ( CCPU * cpu, void * arg ) {
        TChild childData {};

        checkResize ( cpu, 500 );
        rwiTest ( cpu, 0, 500 );

        pthread_mutex_init ( &childData . m_Mtx, nullptr );
        pthread_cond_init ( &childData . m_Cond, nullptr );

        for ( int i = 0; i < 8; i ++ ) {
            childData . m_Pages = (i+1) * 600;
            childData . m_Free  = true;
            pthread_mutex_lock ( &childData . m_Mtx );
            cpu -> NewProcess ( &childData, (void (*)(CCPU *, void*)) childProcess,  false );
            pthread_cond_wait ( &childData . m_Cond, &childData . m_Mtx );
            pthread_mutex_unlock ( &childData . m_Mtx );
        }
        pthread_cond_destroy ( &childData . m_Cond );
        pthread_mutex_destroy ( &childData . m_Mtx );
    }
}

// === MULTITHREADING TEST

namespace TestMultithreading {
    struct TChild {
        int  m_Pages;
        bool m_Free;
    };
    static void childProcess ( CCPU * cpu, TChild * arg ) {
        checkResize ( cpu, arg -> m_Pages );
        rwiTest ( cpu, 0, arg -> m_Pages );
        if ( arg -> m_Free ) checkResize ( cpu, 0 );
    }
    static void initProcess  ( CCPU * cpu, void * arg ) {
        static TChild childData[10];
        checkResize ( cpu, 500 );
        rwiTest ( cpu, 0, 500 );

        for ( int i = 0; i < 10; i ++ ) {
            childData[i] . m_Pages = (i+1) * 100;
            childData[i] . m_Free = i & 1;
            cpu -> NewProcess ( &childData[i], (void (*)(CCPU *, void*))childProcess,  false );
        }
    }
}

// === MULTITHREADING TEST + COPY ADDRESS SPACE

namespace TestMultithreadingCopyAddressSpace {
    struct TChild {
        uint32_t            m_Pages;
        uint32_t            m_Share;
        pthread_barrier_t * m_Barrier;
    };
    static void childProcess ( CCPU * cpu, TChild * arg ) {
        checkResize ( cpu, arg -> m_Pages );
        rwTest ( cpu, arg -> m_Share, arg -> m_Pages );

        pthread_barrier_wait ( arg -> m_Barrier );

        rTest ( cpu, 0, arg -> m_Share );
        rTest ( cpu, arg -> m_Share, arg -> m_Pages );
    }
    static void initProcess  ( CCPU * cpu, void * arg ) {
        const int PROCESSES = 10;
        const int BASE_SIZE = 200;
        static TChild childData[PROCESSES];
        pthread_barrier_t  bar;

        checkResize ( cpu, BASE_SIZE );
        rwiTest ( cpu, 0, BASE_SIZE );
        pthread_barrier_init ( &bar, nullptr, PROCESSES + 1 );

        for ( int i = 0; i < PROCESSES; i ++ ) {
            childData[i] . m_Pages = BASE_SIZE + ( i - PROCESSES / 2 ) * 20;
            childData[i] . m_Share = BASE_SIZE / 2;
            childData[i] . m_Barrier = &bar;
            cpu -> NewProcess ( &childData[i], (void (*)(CCPU *, void*))childProcess, true );
        }
        pthread_barrier_wait ( &bar );
        rTest ( cpu, 0, BASE_SIZE );
        pthread_barrier_destroy ( &bar );
    }
}

// === MULTITHREADING TEST + COPY ON WRITE

namespace TestMultithreadingCopyOnWrite {
    struct TChild {
        uint32_t            m_Pages;
        uint32_t            m_Share;
        pthread_barrier_t * m_Barrier;
    };
    static void childProcess ( CCPU * cpu, TChild * arg ) {
        checkResize ( cpu, arg -> m_Pages );
        rwTest ( cpu, arg -> m_Share, arg -> m_Pages );
        pthread_barrier_wait ( arg -> m_Barrier );
        rTest ( cpu, 0, arg -> m_Share );
        rTest ( cpu, arg -> m_Share, arg -> m_Pages );
    }
    static void initProcess  ( CCPU * cpu, void * arg ) {
        const int PROCESSES = 10;
        const int BASE_SIZE = 1500;
        static TChild childData[PROCESSES];
        pthread_barrier_t bar;

        checkResize ( cpu, BASE_SIZE );
        rwiTest ( cpu, 0, BASE_SIZE );
        pthread_barrier_init ( &bar, nullptr, PROCESSES + 1 );

        for ( int i = 0; i < PROCESSES; i ++ ) {
            childData[i] . m_Pages = BASE_SIZE + ( i - PROCESSES / 2 ) * 20;
            childData[i] . m_Share = BASE_SIZE / 2;
            childData[i] . m_Barrier = &bar;
            cpu -> NewProcess ( &childData[i], (void (*)(CCPU *, void*))childProcess, true );
        }
        pthread_barrier_wait ( &bar );
        rTest ( cpu, 0, BASE_SIZE );
        pthread_barrier_destroy ( &bar );
    }
}

// === RUN TESTS

int main () {
    // Init test
    {
        const int PAGES = 8 * 1024;
        auto * mem = new uint8_t [ PAGES * CCPU::PAGE_SIZE + CCPU::PAGE_SIZE ];
        auto * memAligned = (uint8_t *) (( ((uintptr_t) mem) + CCPU::PAGE_SIZE - 1) & ~(uintptr_t) ~CCPU::ADDR_MASK );

        testStart ();
        MemMgr ( memAligned, PAGES, nullptr, seqTest1 );
        testEnd ( "test #1" );

        testStart ();
        MemMgr ( memAligned, PAGES, nullptr, seqTest2 );
        testEnd ( "test #2" );

        delete [] mem;
    }
    // Sequential test
    {
        const int PAGES = 8 * 1024;
        auto * mem = new uint8_t [ PAGES * CCPU::PAGE_SIZE + CCPU::PAGE_SIZE ];
        auto * memAligned = (uint8_t *) (( ((uintptr_t) mem) + CCPU::PAGE_SIZE - 1) & ~(uintptr_t) ~CCPU::ADDR_MASK );

        testStart ();
        MemMgr ( memAligned, PAGES, nullptr, TestSequential::initProcess );
        testEnd ( "test #3" );

        delete [] mem;
    }
    // Multithreading test
    {
        const int PAGES = 10 * 1024;
        auto * mem = new uint8_t [ PAGES * CCPU::PAGE_SIZE + CCPU::PAGE_SIZE ];
        auto * memAligned = (uint8_t *) (( ((uintptr_t) mem) + CCPU::PAGE_SIZE - 1) & ~(uintptr_t) ~CCPU::ADDR_MASK );

        testStart ();
        MemMgr ( memAligned, PAGES, nullptr, TestMultithreading::initProcess );
        testEnd ( "test #4" );

        delete [] mem;
    }
    // Multithreading test + copy address space
    {
        const int PAGES = 3072;
        auto * mem = new uint8_t [ PAGES * CCPU::PAGE_SIZE + CCPU::PAGE_SIZE ];
        auto * memAligned = (uint8_t *) (( ((uintptr_t) mem) + CCPU::PAGE_SIZE - 1) & ~(uintptr_t) ~CCPU::ADDR_MASK );

        testStart ();
        MemMgr ( memAligned, PAGES, nullptr, TestMultithreadingCopyAddressSpace::initProcess );
        testEnd ( "test #5" );

        delete [] mem;
    }
    // Multithreading test + copy on write
    {
        const int PAGES = 10 * 1024;
        auto * mem = new uint8_t [ PAGES * CCPU::PAGE_SIZE + CCPU::PAGE_SIZE ];
        auto * memAligned = (uint8_t *) (( ((uintptr_t) mem) + CCPU::PAGE_SIZE - 1) & ~(uintptr_t) ~CCPU::ADDR_MASK );

        testStart ();
        MemMgr ( memAligned, PAGES, nullptr, TestMultithreadingCopyOnWrite::initProcess );
        testEnd ( "test #6" );
        delete [] mem;
    }
    return 0;
}