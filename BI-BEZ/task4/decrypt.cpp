// Autor: Ondřej Wrzecionko (wrzecond)
#include "common.h"
#include <iostream>
#include <openssl/pem.h>

using namespace std;

// Easy function for private key loading
EVP_PKEY * loadPrivateKey ( const char * path ) {
    FILE * fp = fopen(path, "r");
    if ( !fp ) return nullptr;
    return PEM_read_PrivateKey(fp, nullptr, nullptr, nullptr);
}

int main ( int argc, const char ** argv ) {
    // Check input arguments
    if ( argc != 4 ) {
        cerr << "Usage: /decrypt <data> <output> <privateKey>" << endl;
        return 1;
    }

    // Initiate input stream
    ifstream is (argv[1], ios::binary | ios::in);
    if ( !is.is_open() ) {
        cerr << "Error: input file could not be opened!" << endl;
        return 1;
    }

    // Load used constants
    int totalRead = 0, totalWrote = 0;

    const char * CIPHER_NAME; int cipherNameLen;
    if ( !readCharArray(is, totalRead, (char *&) CIPHER_NAME, cipherNameLen) ) {
        cout << "Error: could not read cipher name from input file." << endl;
        return 1;
    }

    unsigned char * iv; int ivLen;
    if ( !readCharArray(is, totalRead, (char *&) iv, ivLen) ) {
        delete [] CIPHER_NAME;
        cerr << "Error: could not read initial vector from input file." << endl;
        return 1;
    }

    // Try to load cipher & context
    EVP_CIPHER_CTX * ctx; const EVP_CIPHER * cipher;
    if ( !loadContextAndCipher(ctx, cipher, CIPHER_NAME) ) {
        delete [] CIPHER_NAME;
        delete [] iv;
        cerr << "Error: invalid cipher name or invalid initial vector!" << endl;
        return 1;
    }

    // Try to load private key
    EVP_PKEY * privateKey = loadPrivateKey(argv[3]);
    if ( !privateKey ) {
        delete [] CIPHER_NAME;
        delete [] iv;
        EVP_CIPHER_CTX_free(ctx);

        cerr << "Error: invalid private key." << endl;
        return 1;
    }

    // Try to load secretKey from input
    unsigned char * secretKey; int secretKeyLen;
    if ( !readCharArray(is, totalRead, (char *&) secretKey, secretKeyLen) ) {
        delete [] CIPHER_NAME;
        delete [] iv;
        EVP_CIPHER_CTX_free(ctx);
        EVP_PKEY_free(privateKey);

        cerr << "Error: could not load secretKey from input file." << endl;
        return 1;
    }

    // Try to init (set mode, secretKey)
    if ( !EVP_OpenInit(ctx, cipher, secretKey, secretKeyLen, iv, privateKey) ) {
        delete [] CIPHER_NAME;
        delete [] iv;
        delete [] secretKey;
        EVP_CIPHER_CTX_free(ctx);
        EVP_PKEY_free(privateKey);

        cerr << "Error: could not initiate decrypting." << endl;
        return 1;
    }

    // Initiate output stream
    ofstream os (argv[2]);
    if ( !os.is_open() ) {
        delete [] CIPHER_NAME;
        delete [] iv;
        delete [] secretKey;
        EVP_CIPHER_CTX_free(ctx);
        EVP_PKEY_free(privateKey);

        cerr << "Error: output file could not be opened!" << endl;
        return 1;
    }

    // Do the final decrypting
    if ( !cipherUpdateFinal ( ctx, is, os, totalRead, totalWrote,
        EVP_CIPHER_CTX_block_size(ctx), &EVP_DecryptUpdate, &EVP_OpenFinal ) ) {
        delete [] CIPHER_NAME;
        delete [] iv;
        delete [] secretKey;
        EVP_CIPHER_CTX_free(ctx);
        EVP_PKEY_free(privateKey);
        remove(argv[2]); // delete output file

        cerr << "Error: could not perform decrypting!" << endl;
        return 1;
    }

    cout << "Decryption successful, read " << totalRead << " bytes, wrote " << totalWrote << " bytes." << endl;

    delete [] CIPHER_NAME;
    delete [] iv;
    delete [] secretKey;

    EVP_CIPHER_CTX_free(ctx);
    EVP_PKEY_free(privateKey);
    return 0;
}