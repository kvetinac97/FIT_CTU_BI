#!/usr/bin/bash
cd test

# Invalid data test

echo "==================================="
./main xxx / cert.pem data.html 1>/dev/null && echo "Failure: Invalid host" || echo "Success: Invalid host"
echo "==================================="
./main javorovy-vrch.eu / cert.pem data.html 1>/dev/null && echo "Failure: SSL certificate missing" || echo "Success: SSL certificate missing"
echo "==================================="
echo "Readonly" > readonly.pem
chmod a-w readonly.pem
./main fit.cvut.cz /cs/fakulta/o-fakulte readonly.pem data.html 1>/dev/null && echo "Failure: Certificate file not writable" || echo "Success: Certificate file not writable"
echo "==================================="
echo "Readonly" > readonly.html
chmod a-w readonly.html
./main fit.cvut.cz /cs/fakulta/o-fakulte cert.pem readonly.html 1>/dev/null && echo "Failure: Output file not writable" || echo "Success: Output file not writable"
echo "==================================="
./main fit.cvut.cz /cs/fakulta/o-fakulte cert.pem data.html 1>/dev/null "" && echo "Failure: Invalid ciphers" || echo "Success: Invalid ciphers"
echo "==================================="
if [ -f "data.html" ]; then
    echo "Failure: Data file must not be created during invalid tests."
else
    echo "Success: Data file must not be created during invalid tests."
fi
echo "==================================="

# First run
./main fit.cvut.cz /cs/fakulta/o-fakulte cert.pem data.html 1>first.txt && echo "Success: First run" || echo "Failure: First run"
CIPHER=$(head first.txt -n 1 | sed 's/Used cipher: //')
echo "Cipher used in first run: $CIPHER"
echo "This cipher will be banned in the second run"

echo "==================================="

# Description of cipher meaning

echo "Meaning of cipher name:"
echo "TLS = transport layer of security (used protocol)"
echo "AES_256_GCM, CHACHA20_POLY1305 = block cipher used for encryption (AES/CHACHA, block size 256/20, mode GCM/CCM/POLY1305)"
echo "SHA256 = function used for hashing (SHA, block size 256/384/512)"

echo "==================================="

# Second run (use different cipher)

if [ $CIPHER = "TLS_AES_256_GCM_SHA384" ]; then
  CIPHER2="TLS_CHACHA20_POLY1305_SHA256"
else
  CIPHER2="TLS_AES_256_GCM_SHA384"
fi

./main fit.cvut.cz /cs/fakulta/o-fakulte cert2.pem data2.html "$CIPHER2" && echo "Success: Second run" || echo "Failure: Second run"

# Assert that we have same certificates and they matches FIT certificate
cmp cert.pem cert2.pem && echo "Success: Certificates are same" || echo "Failure: Certificates are same"
echo | openssl s_client -servername "fit.cvut.cz" -connect "fit.cvut.cz:443" 2>/dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > certx.pem
cmp cert.pem certx.pem && echo "Success: Certificate is correct" || echo "Failure: Certificate is correct"