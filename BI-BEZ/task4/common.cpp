// Autor: Ondřej Wrzecionko (wrzecond)
#include <cstring>

#include "common.h"

bool cipherUpdateFinal ( EVP_CIPHER_CTX * ctx, std::istream & is, std::ostream & os,
    int & totalRead, int & totalWrote, int blockSize,
    int (*updateFun)(EVP_CIPHER_CTX *, unsigned char *, int *, const unsigned char *, int),
    int (*finalFun)(EVP_CIPHER_CTX *, unsigned char *, int *) ) {
    // Initialize buffers
    uint8_t inBuffer [BUFFER_SIZE], outBuffer [BUFFER_SIZE + blockSize];
    int inSize = BUFFER_SIZE, outSize = 0;

    // Read as long as we can
    while ( inSize == BUFFER_SIZE ) {
        // We read less than block size, fix inSize
        if ( !is.read ( (char *) inBuffer, BUFFER_SIZE ) && is.eof() ) {
            is.clear();
            inSize = is.gcount();
        }
        // Call decrypt/encrypt function
        if ( !updateFun(ctx, outBuffer, &outSize, inBuffer, inSize) )
            return false;
        // Write decrypted/encrypted data
        os.write ( (char *) outBuffer, outSize );
        totalRead += inSize; totalWrote += outSize;
    }

    // Finalize decryption/encryption
    if ( !finalFun(ctx, outBuffer, &outSize) )
        return false;

    os.write ( (char *) outBuffer, outSize );
    totalWrote += outSize;
    os.flush();
    return os.good();
}

int readCharArray ( std::istream & is, int & totalRead, char * & array, int & arrayLen ) {
    if ( !is.read ( (char *) &arrayLen, sizeof(arrayLen) ) )
        return false;
    array = new char [ arrayLen + 1 ];
    if ( !is.read ( array, arrayLen ) ) {
        delete [] array;
        return false;
    }
    array[arrayLen] = '\0';
    totalRead += (int) sizeof(arrayLen) + arrayLen;
    return true;
}

bool writeCharArray ( std::ostream & os, int & totalWrote, const char * array, int arrayLen ) {
    if ( arrayLen == -1 )
        arrayLen = (int) strlen(array);
    os.write ( (char *) &arrayLen, sizeof(arrayLen) );
    os.write ( (char *) array, arrayLen );
    totalWrote += (int) sizeof(arrayLen) + arrayLen;
    return os.good();
}

bool loadContextAndCipher ( EVP_CIPHER_CTX *& ctx, const EVP_CIPHER *& cipher, const char * CIPHER_NAME ) {
    ctx = EVP_CIPHER_CTX_new();
    if ( !ctx )
        return false;

    OpenSSL_add_all_ciphers();
    cipher = EVP_get_cipherbyname(CIPHER_NAME);
    if ( !cipher ) {
        EVP_CIPHER_CTX_free(ctx);
        return false;
    }

    return true;
}
