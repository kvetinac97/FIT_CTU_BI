#include <iostream>
#include <cstdlib>
#include <cassert>

#include "CMod.hpp"
#include "CSet.hpp"
#include "CDouble.hpp"

using namespace std;

int main ()
{
	srand( time( NULL ) );

	CSet a;
	for ( size_t i = 0; i < 10; i += 1 )
		a += CMod::createRandom( 130 );
	
	for ( size_t i = 0; i < a.size(); ++i )
		cout << a[ i ] << endl;
	cout << endl;
	
	CSet b = a;
	for ( size_t j = 0; j < 2; j += 1 )
		b += CMod::createRandom( 130 );
	
	assert( a << b );

	CMod sum { 130 };
	for ( size_t i = 0; i < a.size(); i += 1 )
		sum += a[ i ];
	cout << sum << endl;

	try {
		CMod tmp { 3 };
		sum += tmp;
	} catch ( const char * ex ) {
		cout << "[EXCEPTION] " << ex << endl;
	}
    
    CDouble l (0.0000000807766), r(0.0000000192234),
            w (0);
    
    l += 0.000464;
    CDouble x = l - -r;
    cout << x.get() << " " << (0.0000000807766 - (-0.0000000192234)) << endl;
    
    cout << (l == r) << (x == l) << (x == x) << endl;
    
    try {
        cout << (l/r).get() << endl;
        cout << (w/l).get() << endl;
        cout << (l/w).get() << endl;
    }
    catch ( const char * ex ) {
        cout << "Exception: " << ex << endl;
    }
    
    while ( !cin.fail() ) {
        CDouble a (0), b (0);
        cin >> a >> b;
        cout << a << b << " " << (a < b) << (a == b) << (a > b) << endl;;
    }

	return 0;
}
