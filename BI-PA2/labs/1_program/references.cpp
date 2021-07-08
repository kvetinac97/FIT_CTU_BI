#include <fstream>
#include <iostream>
#include <algorithm>

using namespace std;

/** Defines minimum size of array. */
const size_t N_MIN = 5;

/**
 * Struct represents array.
 */
struct array_t {
    /** Number of elements in array. */
    size_t n;
    /** Pointer to memory with numbers. */
    double * data;
};

/**
 * Scans numbers from standard input into array.
 * @param[out] arr Pointer to array, which is used for reading numbers from input.
 * @returns 0 for error during reading standard input, 1 if everything is OK
 */
bool array_scan ( array_t & arr, istream & inStream = cin );

/**
 * Prints numbers from array to standard output.
 * @param[in] arr Pointer to array with numbers.
 */
bool array_print ( const array_t & arr, ostream & outStream = cout );

/**
 * Sorts numbers in the passed array.
 * @param[in] arr Pointer to array (to be sorted).
 */
void array_sort ( array_t & arr );

/**
 * Free array allocated memory.
 * @param[in] arr Pointer to array (to be freed).
 */
void array_free ( array_t & arr );

/**
 * Save array to file
 * @param[in] arr Pointer to array (to be saved)
 * @param[in] string File name
 * @return
 */
bool array_save ( array_t & arr, const string & fileName );

/**
 * Loads array from file
 * @param[in] arr Pointer to array (to be written to)
 * @param[in] string File name
 * @return
 */
bool array_load ( array_t & arr, const string & fileName );

int main ( )
{
    array_t arr{};

    if ( !array_scan(arr) ) {
        cout << "Nespravny vstup." << endl;
        return 1;
    }

    array_print(arr);
    array_sort(arr);
    array_print(arr);

    const string fileName = "f.txt";
    if ( !array_save(arr, fileName) ) {
        cout << "Zapis do souboru selhal." << endl;
        array_free(arr);
        return 1;
    }

    array_free(arr);

    if ( !array_load(arr, fileName) ) {
        cout << "Cteni ze souboru selhalo." << endl;
        array_free(arr);
        return 1;
    }

    array_print(arr);

    array_free(arr);

    return 0;
}

/*****************************************************************************/

bool array_scan ( array_t & arr, istream & inStream )
{

    if ( !( inStream >> arr.n ) || arr.n < N_MIN )
        return false;

    arr.data = new double[arr.n];
    for ( size_t i = 0; i < arr.n; ++i )
        if ( ! ( inStream >> arr.data[i] ) )
            return false;

    return true;

}

bool array_print ( const array_t & arr, ostream & out )
{
    out << arr.n << endl;

    for ( size_t i = 0; i < arr.n; ++i ) {
        if ( i != 0 )
            if ( ! ( out << " " ) )
                return false;

        if ( ! ( out << arr.data[i] ) )
            return false;
    }
    return !(!( out << endl ));
}

void array_sort ( array_t & arr )
{
    sort(arr.data, arr.data + arr.n);
}

void array_free ( array_t & arr )
{
    delete [] arr.data;
    arr.n = 0;
    arr.data = nullptr;
}

bool array_save ( array_t & arr, const string & fileName ) {

    ofstream outStream (fileName);
    if ( !outStream )
        return false;

    ofstream outBinary ("f2.txt", ios::binary);
    if ( !outBinary )
        return false;

    outBinary.write((const char *) &arr.n, sizeof(int));

    return array_print(arr, outStream);

}

bool array_load ( array_t & arr, const string & fileName ) {

    ifstream inStream (fileName);
    if ( !inStream )
        return false;

    return array_scan(arr, inStream);

}

