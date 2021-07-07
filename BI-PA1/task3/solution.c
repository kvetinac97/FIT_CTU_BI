#include<stdio.h>
#include<math.h>
#include<float.h>

int clipLine(double rx1, double ry1, double rx2, double ry2, double * ax, double * ay, double * bx, double *by);

int isLine(double ax, double ay, double bx, double by);
int isRectangle(double rx1, double ry1, double rx2, double ry2);

int almostEqual(double x, double y);
int inInterval(double low, double high, double x);

int pointInRectangle(double rx1, double ry1, double rx2, double ry2, double x, double y);

int partOfLine(double ax, double ay, double bx, double by, double x, double y);

int lineCrossesLine(double ax, double ay, double bx, double by, double cx, double cy, double dx, double dy, double * intersectX, double * intersectY, double *bonusX, double *bonusY);

int clipLine(double rx1, double ry1, double rx2, double ry2, double * ax, double * ay, double * bx, double *by){
    
    double _ax = *ax;
    double _ay = *ay;
    double _bx = *bx;
    double _by = *by;
    
    if (!isLine(_ax, _ay, _bx, _by) || !isRectangle(rx1, ry1, rx2, ry2))
        return 0;
    
    if (rx1 > rx2){
        int temp = rx2;
        rx2 = rx1;
        rx1 = temp;
    }
    
    if (ry1 > ry2){
        int temp = ry2;
        ry2 = ry1;
        ry1 = temp;
    }
    
    int is1, is2, is3, is4;
    double intX1, intY1, intX2, intY2, intX3, intY3, intX4, intY4, bonusX, bonusY;
    
    is1 = lineCrossesLine(_ax, _ay, _bx, _by, rx1, ry1, rx2, ry1, &intX1, &intY1, &bonusX, &bonusY);
    is2 = lineCrossesLine(_ax, _ay, _bx, _by, rx1, ry1, rx1, ry2, &intX2, &intY2, &bonusX, &bonusY);
    is3 = lineCrossesLine(_ax, _ay, _bx, _by, rx2, ry1, rx2, ry2, &intX3, &intY3, &bonusX, &bonusY);
    is4 = lineCrossesLine(_ax, _ay, _bx, _by, rx2, ry2, rx1, ry2, &intX4, &intY4, &bonusX, &bonusY);
    
    int suma = is1 + is2 + is3 + is4;
    
    //Úsečka AB je součástí jedné z úseček tvořících obdélník
    if (suma > 10){
        
        double intX = 0, intY = 0;
        int situation = 0;
        
        if (is1 > 10){
            intX = intX1;
            intY = intY1;
            situation = is1-10;
        }
        
        if (is2 > 10){
            intX = intX2;
            intY = intY2;
            situation = is2-10;
        }
        
        if (is3 > 10){
            intX = intX3;
            intY = intY3;
            situation = is3-10;
        }
        
        if (is4 > 10){
            intX = intX4;
            intY = intY4;
            situation = is4-10;
        }
        
        //1D:
        //1: R .. A .. B .. S
        //2: A .. R .. S .. B
        switch (situation){
            //1: nic nemusím řešit
            case 2:
                *ax = intX;
                *ay = intY;
                *bx = bonusX;
                *by = bonusY;
                break;
            case 3:
                *ax = intX;
                *ay = intY;
                break;
            case 4:
                *bx = intX;
                *by = intY;
                break;
        }
        
        return 1;
        
    }
    
    if (suma == 3){
        
        suma--;
        
        if (almostEqual(intX1, intX2) && !almostEqual(intX1, 0))
            is1 = 0;
        else if (almostEqual(intX2, intX3) && !almostEqual(intX2, 0))
            is2 = 0;
        else if (almostEqual(intX3, intX4) && !almostEqual(intX3, 0))
            is3 = 0;
        else if (almostEqual(intX4, intX1) && !almostEqual(intX4, 0))
            is4 = 0;
        else suma++;
        
    }
    
    switch (suma){
            
        case 0:
            //Buď je úsečka v obdélníku, nebo úplně mimo něj
            return pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay) && pointInRectangle(rx1, ry1, rx2, ry2, _bx, _by);
        
        case 1:
            if (is1){
                //Jeden z bodů je mimo trojúhelník
                if (pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                    *bx = intX1;
                    *by = intY1;
                }
                else {
                    *ax = intX1;
                    *ay = intY1;
                }
            }
            
            if (is2){
                //Jeden z bodů je mimo trojúhelník
                if (pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                    *bx = intX2;
                    *by = intY2;
                }
                else {
                    *ax = intX2;
                    *ay = intY2;
                }
            }
            
            if (is3){
                //Jeden z bodů je mimo trojúhelník
                if (pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                    *bx = intX3;
                    *by = intY3;
                }
                else {
                    *ax = intX3;
                    *ay = intY3;
                }
            }
            
            if (is4){
                //Jeden z bodů je mimo trojúhelník
                if (pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                    *bx = intX4;
                    *by = intY4;
                }
                else {
                    *ax = intX4;
                    *ay = intY4;
                }
            }
            
            break;
            
        case 2:
            
            if (is1 && is2) {
                
                if (almostEqual(intX1, intX2) && almostEqual(intY1, intY2) &&
                    pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                    
                    *bx = intX1;
                    *by = intY1;
                    
                }
                else if (almostEqual(intX1, intX2) && almostEqual(intY1, intY2) &&
                    pointInRectangle(rx1, ry1, rx2, ry2, _bx, _by)){
                    
                    *ax = intX1;
                    *ay = intY1;
                    
                }
                else if (intX1 < intX2){
                    *ax = intX1;
                    *ay = intY1;
                    *bx = intX2;
                    *by = intY2;
                }
                else {
                    *ax = intX2;
                    *ay = intY2;
                    *bx = intX1;
                    *by = intY1;
                }
                
            }
            
            if (is1 && is3) {
                
                if (almostEqual(intX1, intX3) && almostEqual(intY1, intY3) &&
                   pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                   
                   *bx = intX1;
                   *by = intY1;
                   
               }
               else if (almostEqual(intX1, intX3) && almostEqual(intY1, intY3) &&
                   pointInRectangle(rx1, ry1, rx2, ry2, _bx, _by)){
                   
                   *ax = intX1;
                   *ay = intY1;
                   
               }
               else if (intX1 < intX3){
                    *ax = intX1;
                    *ay = intY1;
                    *bx = intX3;
                    *by = intY3;
                }
                else {
                    *ax = intX3;
                    *ay = intY3;
                    *bx = intX1;
                    *by = intY1;
                }
                
            }
            
            if (is1 && is4) {
                
                if (almostEqual(intX1, intX4) && almostEqual(intY1, intY4) &&
                   pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                   
                   *bx = intX1;
                   *by = intY1;
                   
               }
               else if (almostEqual(intX1, intX4) && almostEqual(intY1, intY4) &&
                   pointInRectangle(rx1, ry1, rx2, ry2, _bx, _by)){
                   
                   *ax = intX1;
                   *ay = intY1;
                   
               }
               else if (intX1 < intX4){
                    *ax = intX1;
                    *ay = intY1;
                    *bx = intX4;
                    *by = intY4;
                }
                else {
                    *ax = intX4;
                    *ay = intY4;
                    *bx = intX1;
                    *by = intY1;
                }
                
            }
            
            if (is2 && is3) {
                
                if (almostEqual(intX2, intX3) && almostEqual(intY2, intY3) &&
                   pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                   
                   *bx = intX2;
                   *by = intY2;
                   
               }
               else if (almostEqual(intX2, intX3) && almostEqual(intY2, intY3) &&
                   pointInRectangle(rx1, ry1, rx2, ry2, _bx, _by)){
                   
                   *ax = intX2;
                   *ay = intY2;
                   
               }
               else if (intX2 < intX3){
                    *ax = intX2;
                    *ay = intY2;
                    *bx = intX3;
                    *by = intY3;
                }
                else {
                    *ax = intX3;
                    *ay = intY3;
                    *bx = intX2;
                    *by = intY2;
                }
                
            }
            
            if (is2 && is4) {
                
                if (almostEqual(intX2, intX4) && almostEqual(intY2, intY4) &&
                    pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                    
                    *bx = intX2;
                    *by = intY2;
                    
                }
                else if (almostEqual(intX2, intX4) && almostEqual(intY2, intY4) &&
                    pointInRectangle(rx1, ry1, rx2, ry2, _bx, _by)){
                    
                    *ax = intX2;
                    *ay = intY2;
                    
                }
                else if (intX2 < intX4){
                    *ax = intX2;
                    *ay = intY2;
                    *bx = intX4;
                    *by = intY4;
                }
                else {
                    *ax = intX4;
                    *ay = intY4;
                    *bx = intX2;
                    *by = intY2;
                }
                
            }
            
            if (is3 && is4) {
                
                if (almostEqual(intX3, intX4) && almostEqual(intY3, intY4) &&
                    pointInRectangle(rx1, ry1, rx2, ry2, _ax, _ay)){
                    
                    *bx = intX3;
                    *by = intY3;
                    
                }
                else if (almostEqual(intX3, intX4) && almostEqual(intY3, intY4) &&
                    pointInRectangle(rx1, ry1, rx2, ry2, _bx, _by)){
                    
                    *ax = intX3;
                    *ay = intY3;
                    
                }
                else if (intX3 < intX4){
                    *ax = intX3;
                    *ay = intY3;
                    *bx = intX4;
                    *by = intY4;
                }
                else {
                    *ax = intX4;
                    *ay = intY4;
                    *bx = intX3;
                    *by = intY3;
                }
                
            }
            
            break;
            
    }

    return 1;
}

int pointInRectangle(double rx1, double ry1, double rx2, double ry2, double x, double y){
    return inInterval(rx1, rx2, x) && inInterval(ry1, ry2, y);
}

int lineCrossesLine(double ax, double ay, double bx, double by, double cx, double cy, double dx, double dy, double * intersectX, double * intersectY, double *bonusX, double *bonusY){
    
    double a1 = by - ay, a2 = dy - cy;
    double b1 = ax - bx, b2 = cx - dx;
    double c1 = (a1 * ax) + (b1 * ay);
    double c2 = (a2 * cx) + (b2 * cy);
    
    double diff = (a1 * b2 - a2 * b1);
    
    if (fabs(diff) < (DBL_EPSILON * 128)){
        
        int aInsideRS = partOfLine(cx, cy, dx, dy, ax, ay);
        int bInsideRS = partOfLine(cx, cy, dx, dy, bx, by);
        
        int rInsideAB = partOfLine(ax, ay, bx, by, cx, cy);
        int sInsideAB = partOfLine(ax, ay, bx, by, dx, dy);
        
        if (aInsideRS && bInsideRS)
            return 11;
        
        if (rInsideAB && sInsideAB) {
            *intersectX = cx;
            *intersectY = cy;
            
            *bonusX = dx;
            *bonusY = dy;
            return 12;
        }
        
        if (bInsideRS && rInsideAB) {
            *intersectX = cx;
            *intersectY = cy;
            return 13;
        }
        
        if (aInsideRS && sInsideAB) {
            *intersectX = dx;
            *intersectY = dy;
            return 14;
        }
        
        return 0;
    }
    
    double intX = ((b2 * c1) - (b1 * c2)) / diff;
    double intY = ((a1 *c2) - (a2 * c1)) / diff;
    
    if (partOfLine(ax, ay, bx, by, intX, intY) && partOfLine(cx, cy, dx, dy, intX, intY)){
        *intersectX = intX;
        *intersectY = intY;
        return 1;
    }
    
    //Průsečík sice je, ale ne na úsečce AB
    return 0;
}

int partOfLine(double ax, double ay, double bx, double by, double x, double y){
    
    if (almostEqual(ax, bx) && almostEqual(ay, by))
        return 0;
    
    if (almostEqual(ax, bx))
        return almostEqual(ax, x) && inInterval(0, 1, (y - ay) / (by - ay));
    
    if (almostEqual(ay, by))
        return almostEqual(ay, y) && inInterval(0, 1, (x - ax) / (bx - ax));
    
    double t1 = (x - ax) / (bx - ax);
    double t2 = (y - ay) / (by - ay);
    
    if (!almostEqual(t1, t2))
        return 0;
    
    return inInterval(0, 1, t1);
    
}

int isLine(double ax, double ay, double bx, double by){
    return (!almostEqual(ax, bx) || !almostEqual(ay, by));
}

int isRectangle(double rx1, double ry1, double rx2, double ry2) {
    return (!almostEqual(rx1, rx2) && !almostEqual(ry1, ry2));
}

int almostEqual(double x, double y) {
    if (x == 0)
        return fabs(y) <= DBL_EPSILON * 128;

    if (y == 0)
        return fabs(x) <= DBL_EPSILON * 128;
    
    return fabs(x - y) <= (DBL_EPSILON * 128);
}

int inInterval(double low, double high, double x){
    
    double eps1 = DBL_EPSILON * 128;
    double eps2 = DBL_EPSILON * 128;
        
    return ((low - x) <= eps1 && (x - high) <= eps2);
    
}


