// Author: Ondřej Wrzecionko
#include <openssl/ssl.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

// Helper constant
const int BUFFER_SIZE = 4096;

// Helper data structure for easier memory free
typedef struct TData {
    int m_SocketID;
    SSL_CTX * m_Context;
    SSL * m_SSL;
} TDATA;

// Functions for managing the helper data structure
TDATA getDefaultData () {
    TDATA data;
    data.m_SocketID = -1;
    data.m_Context = NULL;
    data.m_SSL = NULL;
    return data;
}
void freeData ( TDATA data ) {
    close(data.m_SocketID);
    SSL_CTX_free(data.m_Context);
    SSL_free(data.m_SSL);
}

// Helper function to create a socket connection
// return 1 on success, 0 otherwise
// in case of success, socket file descriptor is written to socketFd
int connectToSocket  ( int * socketFd, const char * hostname ) {
    // Get IP from hostname
    struct addrinfo hints, * address;
    memset ( &hints, 0, sizeof(hints) );
    if ( getaddrinfo ( hostname, NULL, &hints, &address ) != 0 )
        return 0;

    // Initialize address
    struct sockaddr_in serverAddress;
    memset ( &serverAddress, 0, sizeof(serverAddress) );

    serverAddress.sin_family = address->ai_family;
    serverAddress.sin_addr.s_addr = * ( (uint32_t *) & ( ( (struct sockaddr_in *) address->ai_addr )->sin_addr ) );
    serverAddress.sin_port = htons(443);

    // Try to connect
    int sockFd = socket ( AF_INET, SOCK_STREAM, IPPROTO_TCP );
    int result = connect ( sockFd, (struct sockaddr *) &serverAddress, sizeof(serverAddress) );
    freeaddrinfo(address);

    // Connection failed
    if ( result != 0 ) {
        close ( sockFd );
        return 0;
    }

    *socketFd = sockFd;
    return 1;
}

// Helper function to create SSL context
// return 1 on success, 0 otherwise
// in case of success, SSL context is written to ctx
int createSSLContext ( SSL_CTX ** ctx ) {
    // Create context
    SSL_CTX * context = SSL_CTX_new ( TLS_client_method() );
    if ( !context )
        return 0;

    // Force TLS 1.3
    SSL_CTX_set_options ( context, SSL_OP_NO_SSLv2 | SSL_OP_NO_SSLv3 | SSL_OP_NO_TLSv1 | SSL_OP_NO_TLSv1_1 | SSL_OP_NO_TLSv1_2);

    // Set certificate verification paths
    if ( !SSL_CTX_set_default_verify_paths(context) )
        return 0;

    // Success
    *ctx = context;
    return 1;
}

// Helper function to create SSL object
// return 1 on success, 0 otherwise
// in case of success, SSL object is written to ssl
int createSSL ( int socketFd, SSL_CTX * ctx, const char * hostname, SSL ** ssl ) {
    // Create SSL object
    SSL * sslObject = SSL_new(ctx);
    if ( !sslObject )
        return 0;

    // Assign opened connection
    if ( !SSL_set_fd(sslObject, socketFd) )
        return 0;

    // Set hostname
    if ( !SSL_set_tlsext_host_name(sslObject, hostname) )
        return 0;

    // Success
    *ssl = sslObject;
    return 1;
}

// Connect to SSL with specified cipher
int connectSSL ( SSL * ssl, const char * allowedCiphers, const char ** usedCipher ) {
    // Set allowed ciphers (if not NULL)
    if ( allowedCiphers && SSL_set_ciphersuites(ssl, allowedCiphers) != 1 )
        return 0;

    // Init connection
    if ( SSL_connect(ssl) != 1 )
        return 0;

    // Success
    *usedCipher = SSL_get_cipher_name(ssl);
    return 1;
}

// Get certificate, verify it and save it to file
// in case of success, subjectNameStr and issuerNameStr are set
// warning: subjectNameStr and issuerNameStr have to be freed
int verifySaveCertificate ( SSL * ssl, const char * outputFile, char ** subjectNameStr, char ** issuerNameStr ) {
    // Verify certificate
    if ( SSL_get_verify_result(ssl) != X509_V_OK )
        return 0;

    // Load certificate
    X509 * certificate = SSL_get_peer_certificate(ssl);
    if ( !certificate )
        return 0;

    // Load names
    X509_NAME * subjectName = X509_get_subject_name(certificate);
    X509_NAME * issuerName = X509_get_issuer_name(certificate);
    if ( !subjectName || !issuerName )
        return 0;

    // Save to file
    FILE * output = fopen ( outputFile, "w" );
    if ( !output )
        return 0;

    int writeSuccess = PEM_write_X509 ( output, certificate );
    fclose (output);
    if ( !writeSuccess )
        return 0;

    // Success
    *subjectNameStr = X509_NAME_oneline(subjectName, NULL, 0);
    *issuerNameStr  = X509_NAME_oneline(issuerName, NULL, 0);
    return 1;
}

// Send HTTP request to given URL and page
// return <= 0 on failure / number of bytes written on success
int sendHttpRequest ( SSL * ssl, const char * hostname, const char * page ) {
    // Init data
    char data [64 + strlen(hostname) + strlen(page)];
    data[0] = '\0';
    strcat(data, "GET ");
    strcat(data, page);
    strcat(data, " HTTP/1.1\r\n"
                      "Host: ");
    strcat(data, hostname);
    strcat(data, "\r\nConnection: close\r\n\r\n");
    int dataLen = (int) strlen(data) + 1;

    // Send request
    return SSL_write(ssl, data, dataLen);
}

// Read HTTP response and write it to given file
// return 0 on failure / number of bytes read/written on success
int readHttpResponse ( SSL * ssl, const char * outputFile ) {
    // Try to open output file
    FILE * output = fopen (outputFile, "w");
    if ( !output )
        return 0;

    // Read and write data in chunks
    char buffer [BUFFER_SIZE];
    int read, totalRead = 0, totalWrote = 0;
    do {
        read = SSL_read(ssl, buffer, BUFFER_SIZE); // read from socket
        if (read <= 0) // error
            break;
        totalRead += read;
        totalWrote += (int) fwrite ( buffer, sizeof(*buffer), read, output ); // write to file
    }
    while ( read == BUFFER_SIZE );
    fclose(output);

    // Error whilst reading / writing
    if ( read <= 0 || totalWrote != totalRead ) {
        remove(outputFile);
        return 0;
    }

    // Success
    return totalRead;
}

int main ( int argc, const char ** argv ) {
    if ( argc < 5 || argc > 6 ) {
        fprintf ( stderr, "Usage: ./main <host> <page> <certificate> <html> (allowed_ciphers)" );
        return 1;
    }

    // (1) Create TCP connection to specified hostname
    TDATA data = getDefaultData();
    if ( !connectToSocket(&data.m_SocketID, argv[1]) ) {
        fprintf ( stderr, "Error: Could not connect to specified hostname.\n" );
        return 1;
    }

    SSL_library_init(); // (2) Init SSL library

    // (3) Create SSL context
    if ( !createSSLContext(&data.m_Context) ) {
        fprintf ( stderr,"Error: Could not create SSL context.\n" );
        freeData(data);
        return 1;
    }

    // (4-6) Create SSL structure, assign opened connection, set hostname
    if ( !createSSL(data.m_SocketID, data.m_Context, argv[1], &data.m_SSL) ) {
        fprintf ( stderr, "Error: Could not create SSL / assign connection and hostname.\n" );
        freeData(data);
        return 1;
    }

    // (7) Init SSL connection, find cipher name, ban it, open it again
    const char * allowedCiphers = ( argc == 5 ? NULL : argv[5] ), * cipher;
    if ( !connectSSL(data.m_SSL, allowedCiphers, &cipher) ) {
        fprintf ( stderr, "Error: Could not connect to specified hostname.\n" );
        freeData(data);
        return 1;
    }
    printf("Used cipher: %s\n", cipher);

    // Verify certificate, print information, save certificate to file
    char * subjectName, * issuerName;
    if ( !verifySaveCertificate(data.m_SSL, argv[3], &subjectName, &issuerName) ) {
        fprintf ( stderr, "Error: Certificate validation / saving failed.\n" );
        freeData(data);
        return 1;
    }
    printf ("Certificate validation was successfull.\n"
            "Certificate information:\n"
            "Subject name - %s\n"
            "Issuer name - %s\n", subjectName, issuerName );
    free (subjectName); free (issuerName);

    // (8) Send HTTP request
    int bytesSent;
    if ( ( bytesSent = sendHttpRequest (data.m_SSL, argv[1], argv[2]) ) <= 0 ) {
        fprintf ( stderr, "Error: Sending HTTP request failed.\n" );
        freeData(data);
        return 1;
    }
    printf ( "Bytes sent: %d\n", bytesSent );

    // (8) Read response and write it to output file
    int bytesRead;
    if ( ( bytesRead = readHttpResponse (data.m_SSL, argv[4]) ) == 0 ) {
        fprintf ( stderr, "Error: Reading HTTP response & writing to file failed.\n" );
        freeData(data);
        return 1;
    }
    printf ( "Bytes read: %d\n", bytesRead );

    // (9) Shut down the connection, free memory
    SSL_shutdown(data.m_SSL);
    freeData(data);
    printf("Success, exiting now...\n");
    return 0;
}
