#ifndef __PROGTEST__
#include <cstring>
#include <cstdint>
#include <algorithm>
#include <functional>
using namespace std;

#define DIR_ENTRIES_MAX     128
#define SECTOR_SIZE         512

struct TFile {
    char m_FileName [32];
    size_t m_FileSize;
};

struct TBlkDev {
    size_t m_Sectors {};
    function<size_t(size_t, void *, size_t )> m_Read;
    function<size_t(size_t, const void *, size_t )> m_Write;
};
#endif /* __PROGTEST__ */

#define LINK_EOF 0xFFFFFFFF

// 128 * (4B + 4B + 32B) = 10 sectors
struct TFileInfo {
    uint32_t  m_FileLink;
    uint32_t  m_FileSize;
    char m_FileName [32];
};

class CFileSystem {
    TBlkDev   m_Dev;
    TFileInfo m_FileInfo  [DIR_ENTRIES_MAX];
    uint32_t  m_EndBlock  [DIR_ENTRIES_MAX];
    uint32_t  m_ReadPos   [DIR_ENTRIES_MAX] = {0};
    uint32_t  m_ReadBlock [DIR_ENTRIES_MAX];
    uint8_t   m_WriteBuffer [DIR_ENTRIES_MAX][SECTOR_SIZE];
    uint32_t  m_WriteBufferLen [DIR_ENTRIES_MAX] = {0};
    uint32_t  m_FreeLinkBlock;
    uint32_t  m_FreeLinkPos;
    int       m_FindPosition = -1;

    explicit CFileSystem ( const TBlkDev & dev ) : m_Dev (dev) {
        dev.m_Read (0, m_FileInfo, 10);
        uint32_t tmp [DIR_ENTRIES_MAX];
        dev.m_Read (10, tmp, 1);
        m_FreeLinkBlock = tmp[0];
        m_FreeLinkPos   = tmp[1];
    }

    uint32_t GetBlockID ( uint32_t sectorInfoBlock, uint32_t sectorInfoPos ) {
        return SectorInfoBlocks() + (sectorInfoBlock * DIR_ENTRIES_MAX + sectorInfoPos);
    }
    void GetSectorInfoDuo ( uint32_t block, uint32_t & sectorInfoBlock, uint32_t & sectorInfoPos ) {
        uint32_t tmp = block - SectorInfoBlocks();
        sectorInfoBlock = (tmp / DIR_ENTRIES_MAX);
        sectorInfoPos   = tmp % DIR_ENTRIES_MAX;
    }

    size_t SectorInfoBlocks () const { return SectorInfoBlocks(m_Dev.m_Sectors); }
    static size_t SectorInfoBlocks (size_t sectors) {
        return 11 + (sectors / DIR_ENTRIES_MAX) + 1;
    }

    int FindFile ( const char * name ) {
        for ( int i = 0; i < DIR_ENTRIES_MAX; ++i )
            if ( strcmp(m_FileInfo[i].m_FileName, name) == 0 )
                return i;
        return -1;
    }
    int FindFreeFileDescriptor () {
        for ( int i = 0; i < DIR_ENTRIES_MAX; ++i )
            if ( m_FileInfo[i].m_FileLink == 0 )
                return i;
        return -1;
    }
    uint32_t FindFreeLink () {
        uint32_t helperLink [DIR_ENTRIES_MAX];
        if ( m_Dev.m_Read (11 + m_FreeLinkBlock, helperLink, 1) != 1 )
            return 0;
        uint32_t freeDataBlock = GetBlockID(m_FreeLinkBlock, m_FreeLinkPos);
        uint32_t nextFreeBlock = helperLink[m_FreeLinkPos];

        helperLink[m_FreeLinkPos] = LINK_EOF; // update
        if ( m_Dev.m_Write (11 + m_FreeLinkBlock, helperLink, 1) != 1 )
            return 0;

        GetSectorInfoDuo(nextFreeBlock, m_FreeLinkBlock, m_FreeLinkPos);
        return freeDataBlock;
    }
public:
    static bool CreateFs ( const TBlkDev & dev ) {
        // All links are null (no file, empty size, empty name)
        TFileInfo emptyFiles [DIR_ENTRIES_MAX] = {{0}};
        if ( dev.m_Write (0, emptyFiles, 10) != 10 )
            return false;

        // Create link structure
        size_t sectorInfoBlocks = SectorInfoBlocks(dev.m_Sectors);
        size_t usedSectors = sectorInfoBlocks;
        uint32_t helperLink [DIR_ENTRIES_MAX];
        for ( size_t i = 0; i < sectorInfoBlocks; ++i ) {
            for ( size_t j = 0; j < DIR_ENTRIES_MAX; ++j )
                helperLink[j] = (++usedSectors >= dev.m_Sectors) ? LINK_EOF : usedSectors;
            if ( dev.m_Write (11 + i, helperLink, 1) != 1 )
                return false;
        }

        // Special sector 10 contains only one int - first free link position
        helperLink[0] = 0;
        helperLink[1] = 0;
        if ( dev.m_Write (10, helperLink, 1) != 1 )
            return false;

        return true;
    }
    static CFileSystem * Mount ( const TBlkDev & dev ) {
        return new CFileSystem (dev);
    }
    bool Umount () {
        // Flush write buffers
        for ( int fd = 0; fd < DIR_ENTRIES_MAX; ++fd )
            CloseFile(fd);

        uint32_t tmp [DIR_ENTRIES_MAX] {0};
        tmp[0] = m_FreeLinkBlock;
        tmp[1] = m_FreeLinkPos;
        return m_Dev.m_Write ( 0, m_FileInfo, 10 ) == 10 && m_Dev.m_Write (10, tmp, 1) == 1;
    }
    size_t FileSize ( const char * fileName ) {
        int fd = FindFile(fileName);
        if ( fd == -1 )
            return SIZE_MAX;
        return m_FileInfo[fd].m_FileSize;
    }
    int OpenFile ( const char * fileName, bool writeMode ) {
        // Find searched file
        int filePos = FindFile(fileName);

        // Reading - return file position serving as "file descriptor"
        if ( !writeMode ) {
            if (filePos == -1)
                return -1;

            m_ReadBlock[filePos] = m_FileInfo[filePos].m_FileLink;
            m_ReadPos[filePos] = 0;
            return filePos;
        }

        // Writing - if the file existed, delete it
        if ( filePos != -1 )
            DeleteFile(fileName);

        // Now, we need to create the file
        int fd = FindFreeFileDescriptor();
        if ( fd == -1 )
            return -1; // cannot create - no free space

        strcpy ( m_FileInfo[fd].m_FileName, fileName );
        m_FileInfo[fd].m_FileSize = 0;

        uint32_t freeLink = FindFreeLink();
        if ( freeLink == 0 )
            return -1; // cannot create - no free blocks

        m_FileInfo[fd].m_FileLink = freeLink;
        m_EndBlock[fd] = freeLink;
        m_ReadBlock[fd] = freeLink;
        m_ReadPos[fd] = 0;
        m_WriteBufferLen[fd] = 0;
        return fd;
    }
    bool CloseFile ( int fd ) {
        // Flush write buffer
        if ( m_WriteBufferLen[fd] > 0 ) {
            // Finish the buffer
            uint32_t fileBlock = m_EndBlock[fd], sectorInfoBlock, sectorInfoPos;
            m_Dev.m_Write (fileBlock, m_WriteBuffer[fd], 1);
            m_WriteBufferLen[fd] = 0;

            // Create new block, update fileBlock and fileSize
            uint32_t newBlock = FindFreeLink();
            GetSectorInfoDuo(fileBlock, sectorInfoBlock, sectorInfoPos);
            uint32_t tmp [DIR_ENTRIES_MAX];
            m_Dev.m_Read  (11 + sectorInfoBlock, tmp, 1);
            tmp[sectorInfoPos] = newBlock;
            m_Dev.m_Write (11 + sectorInfoBlock, tmp, 1);
        }

        m_ReadPos[fd] = 0; // reset read position
        m_ReadBlock[fd] = m_FileInfo[fd].m_FileLink;
        return m_FileInfo[fd].m_FileLink != 0;
    }
    size_t ReadFile ( int fd, void * data, size_t len ) {
        // File does not exist, cannot read
        uint32_t fileBlock = m_ReadBlock[fd], sectorInfoBlock, sectorInfoPos;
        if ( fileBlock == 0 )
            return 0;

        uint32_t readBytes  = m_ReadPos[fd] % SECTOR_SIZE;

        // Find file end
        uint32_t tmp [DIR_ENTRIES_MAX];
        if ( fileBlock == LINK_EOF ) // tried to read past file
            return 0;

        // Read as long as we can
        uint8_t tmpBuffer [SECTOR_SIZE], * readBuffer = (uint8_t *) data;
        size_t remainToRead = len;

        while ( remainToRead > 0 ) {
            // We can read immediately
            if ( remainToRead + readBytes < SECTOR_SIZE ) {
                m_Dev.m_Read  (fileBlock, tmpBuffer, 1);
                for ( uint32_t i = 0; i < remainToRead; ++i )
                    readBuffer[i] = tmpBuffer[readBytes + i];
                break;
            }

            // Read what we can
            size_t canRead = SECTOR_SIZE - readBytes;
            m_Dev.m_Read (fileBlock, tmpBuffer, 1);
            for ( uint32_t i = 0; i < canRead; ++i )
                readBuffer[i] = tmpBuffer[readBytes + i];
            readBuffer   += canRead;
            remainToRead -= canRead;

            // Go to next block, update fileBlock and fileSize
            GetSectorInfoDuo(fileBlock, sectorInfoBlock, sectorInfoPos);
            m_Dev.m_Read  (11 + sectorInfoBlock, tmp, 1);
            fileBlock = tmp[sectorInfoPos];
            m_ReadBlock[fd] = fileBlock;
            readBytes = 0;
        }

        int read = min ((int) (m_FileInfo[fd].m_FileSize - m_ReadPos[fd]), (int) len);
        m_ReadPos[fd] += read;
        return read;
    }
    size_t WriteFile ( int fd, const void * data, size_t len ) {
        // File does not exist, cannot write
        uint32_t fileBlock = m_EndBlock[fd], sectorInfoBlock, sectorInfoPos;
        if ( fileBlock == 0 )
            return 0;

        // Find file end
        uint32_t tmp [DIR_ENTRIES_MAX];

        // Write as long as we can
        auto * writeBuffer = (uint8_t *) data;
        size_t remainToWrite = len;

        while ( remainToWrite > 0 ) {
            // Just write to buffer and close
            if ( remainToWrite + m_WriteBufferLen[fd] < SECTOR_SIZE ) {
                for ( uint32_t i = 0; i < remainToWrite; ++i )
                    m_WriteBuffer[fd][m_WriteBufferLen[fd] + i] = writeBuffer[i];
                m_WriteBufferLen[fd] += remainToWrite;
                break;
            }

            // Finish the buffer firstly
            size_t canWrite = SECTOR_SIZE - m_WriteBufferLen[fd];
            for ( uint32_t i = 0; i < canWrite; ++i )
                m_WriteBuffer[fd][m_WriteBufferLen[fd] + i] = writeBuffer[i];
            m_Dev.m_Write (fileBlock, m_WriteBuffer[fd], 1);
            m_WriteBufferLen[fd] = 0;
            writeBuffer   += canWrite;
            remainToWrite -= canWrite;

            // Create new block, update fileBlock and fileSize
            uint32_t newBlock = FindFreeLink();
            GetSectorInfoDuo(fileBlock, sectorInfoBlock, sectorInfoPos);
            m_Dev.m_Read  (11 + sectorInfoBlock, tmp, 1);
            tmp[sectorInfoPos] = newBlock;
            m_Dev.m_Write (11 + sectorInfoBlock, tmp, 1);
            fileBlock = newBlock;
            m_EndBlock[fd] = newBlock;
        }

        m_FileInfo[fd].m_FileSize += len;
        return len;
    }
    bool DeleteFile ( const char * fileName ) {
        // Find file
        int fd = FindFile(fileName);
        if ( fd == -1 )
            return false; // does not exist

        // File does not exist, cannot delete
        uint32_t fileBlock = m_FileInfo[fd].m_FileLink, sectorInfoBlock, sectorInfoPos;
        if ( fileBlock == 0 )
            return false; // not opened

        // Find file end
        uint32_t tmp [DIR_ENTRIES_MAX];
        while ( true ) {
            GetSectorInfoDuo(fileBlock, sectorInfoBlock, sectorInfoPos);
            m_Dev.m_Read (11 + sectorInfoBlock, tmp, 1);
            uint32_t nextBlock = tmp[sectorInfoPos];
            if ( nextBlock == LINK_EOF ) { // we're at the last block
                tmp[sectorInfoPos] = GetBlockID(m_FreeLinkBlock, m_FreeLinkPos);
                m_Dev.m_Write (11 + sectorInfoBlock, tmp, 1);
                break;
            }
            fileBlock = nextBlock;
        }

        GetSectorInfoDuo(m_FileInfo[fd].m_FileLink, m_FreeLinkBlock, m_FreeLinkPos);
        memset(m_FileInfo + fd, 0, sizeof(m_FileInfo[fd]));
        return true;
    }
    bool FindFirst ( TFile & file ) {
        m_FindPosition = -1;
        return FindNext(file);
    }
    bool FindNext ( TFile & file ) {
        for ( int i = m_FindPosition + 1; i < DIR_ENTRIES_MAX; ++i ) {
            if ( m_FileInfo[i].m_FileLink == 0 )
                continue;

            m_FindPosition = i;
            strcpy(file.m_FileName, m_FileInfo[i].m_FileName);
            file.m_FileSize = m_FileInfo[i].m_FileSize;
            return true;
        }
        return false;
    }
};


#ifndef __PROGTEST__
#include "simple_test.inc"
#endif /* __PROGTEST__ */
