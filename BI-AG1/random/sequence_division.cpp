#include <iostream>
#include <map>
#include <vector>

using namespace std;

class CAlg {
    
    const vector<int> & m_Numbers;
    map< int, map< int, int > > T;
    
    int sum ( int start, int length ) {
        int sum = 0;
        for ( int i = 0; i < length; i++ )
            sum += m_Numbers[start + i - 1];
        return sum;
    }
    
public:
    
    CAlg ( const vector<int> & numbers )
    : m_Numbers ( numbers ) {}
    
    int solve () {
        for ( int i = 1; i <= m_Numbers.size(); i++ )
            T[i][1] = 0;
        
        for ( int len = 2; len <= m_Numbers.size(); len++ ) {
            for ( int pos = 1; pos <= m_Numbers.size() - len + 1; pos++ ) {
                int best = -2147483648;
            
                for ( int split = 1; split <= len - 1; split++ ) {
                    int left = T[pos][split];
                    int right = T[pos + split][len - split];
                    
                    int add = ( sum(pos, split) == sum(pos + split, len - split) ) ? 3 : -1;
                    best = max ( max ( left, right ) + add, best );
                }
                
                T[pos][len] = best;
            }
        }
        
        /* for ( int i = 1; i <= m_Numbers.size(); i++ )
            for ( int j = 1; j <= m_Numbers.size() - i + 1; j++ )
                cout << "T[" << i << "][" << j << "] = " << T[i][j] << endl; výpis tabulky */
        
        return T[1][m_Numbers.size()];
    }
    
};


int main ( void ) {
    
    vector<int> numbers;
    
    cout << "Zadejte cisla: " << endl;
    int x;
    while ( cin >> x )
        numbers.push_back ( x );
    
    cout << "Cisla zadana." << endl;
    
    CAlg alg ( numbers );
    cout << "Nejlepsi skore je: " << alg.solve() << endl;
    
    return 0;
    
}
