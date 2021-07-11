// Author: Ondřej Wrzecionko (wrzecond)
#include <cctype>
#include <fstream>
#include <iostream>
#include <string>
#include <openssl/evp.h>

using namespace std;

template<typename T>
T copyInt ( istream & is, ostream & os ) {
    T tmp = 0;
    is.read ( (char *) &tmp, sizeof(tmp) );
    os.write( (char *) &tmp, sizeof(tmp) );
    return tmp;
}

const int BUFFER_SIZE = 1024;

void copyCharArray ( istream & is, ostream & os, size_t length ) {
    size_t rmd = length;
    while ( rmd > 0 ) {
        char buffer [BUFFER_SIZE];
        size_t blockSize = rmd > BUFFER_SIZE ? BUFFER_SIZE : rmd;
        rmd -= blockSize;
        is.read ( buffer, sizeof(char) * blockSize );
        os.write( buffer, sizeof(char) * blockSize );
    }
    os.flush();
}
bool copyCharArray ( istream & is, ostream & os, const string & mode, bool encrypt ) {
    EVP_CIPHER_CTX * ctx;
    const EVP_CIPHER * cipher;
    unsigned char key[EVP_MAX_KEY_LENGTH] = "RANDOM_KEY";
    unsigned char iv[EVP_MAX_IV_LENGTH]   = "ILOVEBIBEZ";

    OpenSSL_add_all_ciphers();
    cipher = EVP_get_cipherbyname( ("AES-128-" + mode).c_str() );
    ctx = EVP_CIPHER_CTX_new();
    if ( !cipher || !ctx || !EVP_CipherInit_ex(ctx, cipher, nullptr, key, iv, encrypt) )
        return false;

    uint8_t in_buffer [BUFFER_SIZE], out_buffer [BUFFER_SIZE + EVP_CIPHER_block_size(cipher)];
    int inSize = BUFFER_SIZE, outSize = 0, totalRead = 0, totalWrote = 0;

    while ( inSize == BUFFER_SIZE ) {
        if ( !is.read ( (char *) in_buffer, BUFFER_SIZE ) && is.eof() ) {
            is.clear();
            inSize = is.gcount();
        }
        if ( !EVP_CipherUpdate(ctx, out_buffer, &outSize, in_buffer, inSize) )
            return false;
        os.write ( (char *) out_buffer, sizeof(char) * outSize );
        totalRead += inSize; totalWrote += outSize;
    }

    if ( !EVP_CipherFinal_ex(ctx, out_buffer, &outSize) )
        return false;

    os.write ( (char *) out_buffer, sizeof(char) * outSize );
    totalWrote += outSize;
    os.flush();

    cout << "Read " << totalRead << " bytes, wrote " << totalWrote << " bytes." << endl;
    EVP_CIPHER_CTX_free(ctx);
    return true;
}

/**
 * (De)ciphers TGA image
 * @param is input stream to read image from
 * @param os output stream to write image to
 * @param mode which will be used for (de/en)crypting [EBC|CBC]
 * @param encrypt shall we encrypt or decrypt
 * @return boolean value indicating (de/en)crypting success
 */
bool cipherTGA ( istream & is, ostream & os, const string & mode, bool encrypt ) {
    auto pcidLn = copyInt<uint8_t >(is, os);
    copyInt<uint8_t >(is, os);        // color map type (pointless)
    copyInt<uint8_t >(is, os);        // image type (pointless)
    copyInt<uint16_t>(is, os);        // color map beggining (pointless)
    auto cmEnNm = copyInt<uint16_t>(is, os);
    auto cmEnSz = copyInt<uint8_t >(is, os);
    // skip picture id + 10 (image specification) + color map entry number * ( size / 8 )
    copyCharArray(is, os, pcidLn + 10 + cmEnNm * ( cmEnSz / 8 ));
    return copyCharArray(is, os, mode, encrypt);
}

int main ( int argc, char * argv[] ) {
    if ( argc != 5 ) {
        cout << "Usage: /main <in_file> <out_file> <ECB|CBC> <encrypt>" << endl;
        return 1;
    }

    ifstream is (argv[1], ios::binary | ios::in);
    if ( !is.is_open() ) {
        cout << "Neexistujici vstupni soubor." << endl;
        return 1;
    }

    ofstream os (argv[2], ios::binary | ios::out);
    if ( !os.is_open() ) {
        cout << "Do vystupniho souboru nelze zapisovat." << endl;
        return 1;
    }

    string mode = argv[3];
    bool encrypt = (bool) ( argv[4][0] - '0' );
    if ( ( mode != "ECB" && mode != "CBC" ) ) {
        cout << "Nespravny operacni mod sifry." << endl;
        return 1;
    }

    if ( !cipherTGA(is, os, mode, encrypt) || is.fail() || !os ) {
        cout << "Chyba pri (de)sifrovani / cteni / zapisu." << endl;
        return 1;
    }

    cout << "Akce uspesna!" << endl;
    return 0;
}