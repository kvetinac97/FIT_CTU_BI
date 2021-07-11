// Autor: Ondřej Wrzecionko (wrzecond)
#include "common.h"
#include <iostream>
#include <openssl/pem.h>
#include <openssl/rand.h>

using namespace std;

// Constants used in program
unsigned char iv [EVP_MAX_IV_LENGTH] = "HAPPINESS";
const char * CIPHER_NAME = "AES-128-CBC";

// Easy function for key loading
EVP_PKEY * loadPublicKey ( const char * path ) {
    FILE * fp = fopen(path, "r");
    if ( !fp ) return nullptr;
    return PEM_read_PUBKEY(fp, nullptr, nullptr, nullptr);
}

int main ( int argc, const char ** argv ) {
    // Check input arguments
    if ( argc != 4 ) {
        cerr << "Usage: /encrypt <data> <output> <publicKey>" << endl;
        return 1;
    }

    // Try to load public key
    EVP_PKEY * publicKey = loadPublicKey(argv[3]);
    if ( !publicKey ) {
        cerr << "Invalid public key!" << endl;
        return 1;
    }

    // Try to initiate random generator
    if ( RAND_load_file("/dev/random", 32) != 32 ) {
        cerr << "Error: Cannot seed the random generator!" << endl;
        return 1;
    }

    // Try to load cipher and context
    EVP_CIPHER_CTX * ctx; const EVP_CIPHER * cipher;
    if ( !loadContextAndCipher(ctx, cipher, CIPHER_NAME) ) {
        EVP_PKEY_free(publicKey);
        cerr << "Invalid cipher name!" << endl;
        return 1;
    }

    // Start encrypting - generate random key
    auto * secretKey = new unsigned char [EVP_PKEY_size(publicKey)]; int secretKeyLen;
    if ( !EVP_SealInit ( ctx, cipher, &secretKey, &secretKeyLen, iv, &publicKey, 1) ) {
        EVP_PKEY_free(publicKey);
        EVP_CIPHER_CTX_free(ctx);
        delete [] secretKey;

        cerr << "Unknown error whilst trying to initialize secret key." << endl;
        return 1;
    }

    // Initiate input stream
    ifstream is (argv[1], ios::binary | ios::in);
    if ( !is.is_open() ) {
        EVP_PKEY_free(publicKey);
        EVP_CIPHER_CTX_free(ctx);
        delete [] secretKey;

        cerr << "Error: input file could not be opened!" << endl;
        return 1;
    }

    // Initiate output stream
    ofstream os (argv[2]);
    if ( !os.is_open() ) {
        EVP_PKEY_free(publicKey);
        EVP_CIPHER_CTX_free(ctx);
        delete [] secretKey;

        cerr << "Error: output file could not be opened!" << endl;
        return 1;
    }

    // Save information about encrypted data (cipher type, iv, secretKey)
    int totalRead = 0, totalWrote = 0;
    if (!writeCharArray(os, totalWrote, (char *) CIPHER_NAME) ||
        !writeCharArray(os, totalWrote, (char *) iv, EVP_MAX_IV_LENGTH) ||
        !writeCharArray(os, totalWrote, (char *) secretKey, secretKeyLen ) ) {
        EVP_PKEY_free(publicKey);
        EVP_CIPHER_CTX_free(ctx);
        delete [] secretKey;

        cerr << "An error happened whilst trying to write into the output file." << endl;
        return 1;
    }

    // Perform encryption
    if ( !cipherUpdateFinal (ctx, is, os, totalRead, totalWrote,
         EVP_CIPHER_CTX_block_size(ctx), &EVP_EncryptUpdate, &EVP_SealFinal ) ) {
        EVP_PKEY_free(publicKey);
        EVP_CIPHER_CTX_free(ctx);
        delete [] secretKey;
        remove(argv[2]); // delete output file

        cerr << "An error happened whilst trying to encrypt the data." << endl;
        return 1;
    }

    cout << "Encrypting successful: read " << totalRead << " bytes, wrote " << totalWrote << " bytes." << endl;
    EVP_PKEY_free(publicKey);
    EVP_CIPHER_CTX_free(ctx);
    delete [] secretKey;
    return 0;
}
