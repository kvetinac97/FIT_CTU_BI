#pragma once

#include <fstream>
#include <openssl/evp.h>

// Magic constant for reading/writing data
const int BUFFER_SIZE = 4096;

// Tries to cipher with given functions from istream to ostream
bool cipherUpdateFinal ( EVP_CIPHER_CTX * ctx, std::istream & is, std::ostream & os,
    int & totalRead, int & totalWrote, int blockSize,
    int (*updateFun)(EVP_CIPHER_CTX *, unsigned char *, int *, const unsigned char *, int),
    int (*finalFun)(EVP_CIPHER_CTX *, unsigned char *, int *) );

// Reads int length + char array from given input stream
int readCharArray ( std::istream & is, int & totalRead, char * & array, int & arrayLen );

// Writes int array length + char array to given output stream
// array length equal to -1 indicates length should be calculated with strlen(array)
bool writeCharArray ( std::ostream & os, int & totalWrote, const char * array, int arrayLen = -1 );

// Easy loading function
bool loadContextAndCipher ( EVP_CIPHER_CTX *& ctx, const EVP_CIPHER *& cipher, const char * CIPHER_NAME );
