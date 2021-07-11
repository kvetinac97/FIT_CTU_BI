#include <cstdio>
#include <cstring>
#include <ctime>
#include <algorithm>
#include <bitset>
#include <iostream>
#include <string>
#include <openssl/evp.h>

// C++ function to generate random string of given length
// Source: https://stackoverflow.com/a/12468109
std::string random_string ( size_t length ) {
    auto randchar = []() -> char {
        const char * charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return charset [ rand() % ( sizeof(charset) - 1 ) ];
    };
    std::string str(length,0);
    std::generate_n ( str.begin(), length, randchar );
    return str;
}

// Function to check whether first $prefix bytes of string $x are 0
bool matchPrefix ( const unsigned char * x, int prefix, bool print = false ) {
    int xLen = prefix / 8; // we must compare by bytes
    for ( int i = 0; i <= xLen; ++i ) {
        std::bitset<8> bits (x[i]); // init byte array from char
        int len = ((i == xLen) ? (prefix % 8) : 8); // we compare 8 bytes / prefix%8 (rest) on last
        if ( print ) std::cout << "Matching " << bits << " against " << len << std::endl; // debug
        // Bit comparison
        for ( int j = 0; j < len; ++j )
            if ( bits[j] != 0 )
                return false;
    }
    if ( !print ) matchPrefix(x, prefix, true); // debug
    return true;
}

int main (int argc, char ** argv) {
    // Try load
    int prefix;
    if ( argc != 2 || sscanf(argv[1], "%d", &prefix) != 1 || prefix < 0 ) {
        printf("Invalid prefix.\n");
        return 1;
    }
    if ( prefix > 24 ) {
        printf("Prefix is too big, message search would take a long time\n"
               "(and we're not mining Bitcoin. In order to save your processing\n"
               "power, the program will now shut down...\n");
        return 1;
    }

    // Context, type, hash field and resulting length
    EVP_MD_CTX * ctx;
    const EVP_MD * type;
    unsigned char hash[EVP_MAX_MD_SIZE];
    unsigned int length;

    // Init function and load hash type
    OPENSSL_init_crypto(OPENSSL_INIT_ADD_ALL_DIGESTS, NULL);
    type = EVP_get_digestbyname("sha384");
    ctx = EVP_MD_CTX_create();

    std::string s;
    srand (time(NULL));
    do {
        // Init string with a random value (length 0 to 16)
        s = random_string(rand() % 16);

        // Try hashing
        if ( !EVP_DigestInit_ex ( ctx, type, NULL ) || !EVP_DigestUpdate ( ctx, s.c_str(), s.size() ) ||
            !EVP_DigestFinal_ex ( ctx, hash, &length ) )
            return 1;
    }
    while ( !matchPrefix(hash, prefix) );

    // Destroy context
    EVP_MD_CTX_destroy(ctx);

    /* Vypsani vysledneho hashe */
    printf("Message: ");
    for (unsigned int i = 0; i < s.size(); i++)
        printf("%02x", s[i]);
    printf("\nHash: ");
    for (unsigned int i = 0; i < length; i++)
        printf("%02x", hash[i]);
    printf("\n");
    return 0;
}
