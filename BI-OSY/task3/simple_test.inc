#include <cassert>
#include <string>

//------------------------------------------ [ CONSTANTS ] ----------------------------------------
#define DISK_SECTORS 160000
const int BUFFER_SIZE = (1024 * 1024);
static FILE * g_Fp    = nullptr;
//------------------------------------------ [ CONSTANTS ] ----------------------------------------

//--------------------------------------- [ DISK UTILITIES ] --------------------------------------
static size_t diskRead  ( size_t sectorNr, void * data, size_t sectorCnt ) {
    if ( g_Fp == nullptr || sectorNr + sectorCnt > DISK_SECTORS )
        return 0;
    fseek ( g_Fp, (long) sectorNr * SECTOR_SIZE, SEEK_SET );
    return fread ( data, SECTOR_SIZE, sectorCnt, g_Fp );
}
//-------------------------------------------------------------------------------------------------
static size_t diskWrite ( size_t sectorNr, const void * data, size_t sectorCnt ) {
    if ( g_Fp == nullptr || sectorNr + sectorCnt > DISK_SECTORS )
        return 0;
    fseek ( g_Fp, (long) sectorNr * SECTOR_SIZE, SEEK_SET );
    return fwrite ( data, SECTOR_SIZE, sectorCnt, g_Fp );
}
//-------------------------------------------------------------------------------------------------
static TBlkDev createDisk () {
    char buffer[SECTOR_SIZE] = {0};
    g_Fp = fopen ( "test.file", "w+b" );
    if ( !g_Fp )
        throw runtime_error("Error creating backed block device");

    for ( int i = 0; i < DISK_SECTORS; i ++ )
        if ( fwrite ( buffer, sizeof ( buffer ), 1, g_Fp ) != 1 )
            throw runtime_error("Error creating backed block device");

    TBlkDev res;
    res . m_Sectors = DISK_SECTORS;
    res . m_Read    = diskRead;
    res . m_Write   = diskWrite;
    return res;
}
//-------------------------------------------------------------------------------------------------
static TBlkDev openDisk   () {
    g_Fp = fopen ( "test.file", "r+b" );
    if ( ! g_Fp )
        throw runtime_error("Error opening backend block device");
    fseek ( g_Fp, 0, SEEK_END );
    if ( ftell ( g_Fp ) != DISK_SECTORS * SECTOR_SIZE ) {
        fclose ( g_Fp );
        g_Fp = nullptr;
        throw runtime_error("Error opening backend block device");
    }

    TBlkDev  res;
    res . m_Sectors = DISK_SECTORS;
    res . m_Read    = diskRead;
    res . m_Write   = diskWrite;
    return res;
}
//-------------------------------------------------------------------------------------------------
static void doneDisk      () {
    if ( g_Fp ) {
        fclose ( g_Fp );
        g_Fp = nullptr;
    }
}
//--------------------------------------- [ DISK UTILITIES ] --------------------------------------

//-------------------------------------------- [ TESTS ] ------------------------------------------
static void testMkFs  () {
    printf("Testing mkfs...\n");
    assert ( CFileSystem::CreateFs ( createDisk () ) );
    doneDisk ();
    printf("Done mkfs.\n");
}
//-------------------------------------------------------------------------------------------------
static void testWrite () {
    // Mount
    printf("Testing Mount, Open, Write, FileSize, Read, Close...\n");
    CFileSystem *fs = CFileSystem::Mount(openDisk());
    assert (fs);

    // Create files
    for (int i = 0; i < 32; i++) {
        // Open for writing
        int fd = fs->OpenFile((std::string("hello") + to_string(i)).c_str(), true);
        assert (fd >= 0);

        // Write buffer
        char buffer[BUFFER_SIZE + 10];
        for (int j = 0; j < BUFFER_SIZE; j++)
            buffer[j] = (char) j;
        buffer[BUFFER_SIZE] = 69;

        // Test write
        for (int j = 0; j < BUFFER_SIZE; j += SECTOR_SIZE)
            assert (fs->WriteFile(fd, buffer + j, SECTOR_SIZE) == SECTOR_SIZE);
        assert (fs->WriteFile(fd, buffer + BUFFER_SIZE, 1) == 1);
        assert (fs->FileSize(("hello" + to_string(i)).c_str()) == BUFFER_SIZE + 1);
        assert (fs->CloseFile(fd));

        // Test immediate read
        fd = fs->OpenFile((std::string("hello") + to_string(i)).c_str(), false);
        assert (fd >= 0);
        assert (fs->FileSize(("hello" + to_string(i)).c_str()) == BUFFER_SIZE + 1);

        for (int j = 0; j < BUFFER_SIZE; j += SECTOR_SIZE)
            assert (fs->ReadFile(fd, buffer + j, SECTOR_SIZE) == SECTOR_SIZE);

        for (int j = 0; j < BUFFER_SIZE; j++)
            assert (buffer[j] == (char) j);
    }

    // Umount the filesystem, delete disk
    assert (fs->Umount());
    delete fs;
    doneDisk();
    printf("Done Mount, Open, Write, FileSize, Read, Close.\n");
}
//-------------------------------------------------------------------------------------------------
static void testRead  () {
    // Mount back after reboot
    printf("Testing Mount, Open, Write, FileSize, Read, Close (after reboot)...\n");
    CFileSystem * fs = CFileSystem::Mount ( openDisk () );
    assert ( fs );

    // Test file list
    TFile info {};
    for ( bool found = fs -> FindFirst ( info ); found; found = fs -> FindNext ( info )   )
        printf ( "%-30s %6zd\n", info . m_FileName, info . m_FileSize );

    char buffer [BUFFER_SIZE + 10000];
    int fd = fs->OpenFile("hello1", false);
    assert ( fd >= 0 );
    assert ( fs->FileSize("hello1") == BUFFER_SIZE + 1 );

    assert ( fs -> ReadFile ( fd, buffer, BUFFER_SIZE ) == BUFFER_SIZE );
    assert ( fs -> ReadFile ( fd, buffer + BUFFER_SIZE, 2 ) == 1 );

    for ( int i = 0; i < BUFFER_SIZE; i ++ )
        assert ( buffer[i] == (char)i );
    assert ( buffer[BUFFER_SIZE] == 69 );

    assert ( fs->CloseFile(fd) );

    fd = fs->OpenFile("hello", true);
    assert ( fd >= 0 );
    assert ( fs->FileSize("hello") == 0 );
    assert ( fs->CloseFile(fd) );

    assert ( fs -> Umount () );
    delete fs;
    doneDisk ();
    printf("Done Mount, Open, Write, FileSize, Read, Close (after reboot).\n");
}
//-------------------------------------------- [ TESTS ] ------------------------------------------

//--------------------------------------------- [ MAIN ] ------------------------------------------
int main ( int argc, char ** argv ) {
    if ( argc != 2 ) {
        printf("Usage: ./main <read|write>\n");
        return 1;
    }

    if ( strcmp(argv[1], "read") == 0 )
        testRead();
    else {
        testMkFs ();
        testWrite ();
    }

    return 0;
}
//-------------------------------------------- [ TESTS ] ------------------------------------------