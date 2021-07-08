#include <iostream>

using namespace std;

class UltimeOverride {

public:
    int x;
    
    UltimeOverride () {
        this->x = 1;
    }
    
    friend UltimeOverride operator + ( UltimeOverride a, UltimeOverride b ) {
        a.x += b.x;
        return a;
    }
    
    friend std::ostream & operator << ( std::ostream & stream, UltimeOverride a ) {
        stream << a.x;
        return stream;
    }
    
};

int main () {
    
    UltimeOverride ultime0, ultime1, ultime2, ultime3, ultime4;
    ultime1.x = 5;
    ultime2.x = 10;
    ultime3 = ultime2;
    ultime4 = ultime1 + ultime2;
    
    cout << ultime4 << endl;
    
    return 0;
    
}
