#include <iostream>
#include <algorithm>
#include <iomanip>

using namespace std;

struct date_t {
	int year;
	int month;
	int day;
};

const char * MESSAGE_INVALID_INPUT = "Nespravny vstup.";

bool date_scan ( date_t & d ) {
    char c1, c2;
    cin >> setw(4) >> d.year >> c1 >> setw(2) >> d.month >> c2 >> setw(2) >> d.day;
    return ( c1 == '-' && c2 == '-' && !cin.fail() );
}

void date_print ( const date_t & d ) {
    cout << d.year << "-" << setw(2) << setfill('0') << d.month << "-" << setw(2) << setfill('0') << d.day;
}

bool date_compare ( const date_t & d1, const date_t & d2 )
{
    return ( d1.year < d2.year || ( d1.year == d2.year && d1.month < d2.month ) ||
            ( d1.year == d2.year && d1.month == d2.month && d1.day < d2.day ));
}

struct datelist_t {
	size_t n;
	date_t * data;
};

void datelist_free ( datelist_t & dl )
{
    delete [] dl.data;
	dl.n = 0;
	dl.data = nullptr;
}

bool datelist_scan ( datelist_t & dl )
{
	if ( !( cin >> dl.n ) || dl.n <= 0 )
		return false;

    dl.data = new date_t[dl.n];
	for ( size_t i = 0; i < dl.n; ++i ) {
		if ( !date_scan( dl.data[ i ] ) ) {
			datelist_free( dl );
			return false;
		}
	}
	
	return true;
}

void datelist_print ( const datelist_t & dl )
{
	for ( size_t i = 0; i < dl.n; ++i ) {
		date_print( dl.data[ i ] );
		cout << endl;
	}
}

void datelist_sort ( datelist_t & dl )
{
	sort( dl.data, dl.data + dl.n, date_compare );
}

int main ()
{
    datelist_t dl;
    dl.n = 0;
    dl.data = nullptr;

	if ( !datelist_scan( dl ) ) {
		cout << MESSAGE_INVALID_INPUT << endl;
		return 1;
	}

	datelist_print( dl );

	datelist_sort( dl );
    cout << endl;
	datelist_print( dl );

	datelist_free( dl );
	return 0;
}
