#include <iostream>
#include <fstream>

using namespace std;

int main () {

  const char * nazev1 = "sunflower.ppm";
  const char * nazev2 = "test.ppm";

  ifstream fin (nazev1, ios::binary);
  ofstream fout (nazev2, ios::binary);

  if ( !fin || !fout ) {
    cout << "Chyba nacitani souboru." << endl;
    return 1;
  }

  const int START = -1;
  const int MODE_P6_START = 0;
  const int MODE_P6_END = 1;
  const int MODE_WIDTH_START = 2;
  const int MODE_WIDTH_END = 3;
  const int MODE_HEIGHT_START = 4;
  const int MODE_HEIGHT_END = 5;
  const int MODE_MAXVAL_START = 6;
  const int MODE_PIXEL_R = 7;
  const int MODE_PIXEL_G = 8;
  const int MODE_PIXEL_B = 9;
  const int MODE_PIXEL_A1 = 10;

  char c, x;
  int mode = -1, width, height, maxval, r, g, b, pixels = 0;
  double tmp;

  while ( fin.read(&c, sizeof(c)) ) {
     switch ( mode ) {
       case START:
	 fout.write(&c, sizeof(c));
         if ( c == 'P' )
           mode = MODE_P6_START;
         break;
       case MODE_P6_START:
	 fout.write(&c, sizeof(c));
         if ( c == '6' )
           mode = MODE_P6_END;
         break;
       case MODE_P6_END:
	 fout.write(&c, sizeof(c));
         if ( c == '\n' || c == '\t' || c == '\r' || c == ' ' )
           continue;
         width = c - 48;
         mode = MODE_WIDTH_START;
         break;
       case MODE_WIDTH_START:
	 fout.write(&c, sizeof(c));
         if ( c == '\n' || c == '\t' || c == '\r' || c == ' ' ) {
	   mode = MODE_WIDTH_END;
           continue;
	 }
         width *= 10;
         width += c - 48;
         break;
       case MODE_WIDTH_END:
	 fout.write(&c, sizeof(c));
         if ( c == '\n' || c == '\t' || c == '\r' || c == ' ' )
           continue;
         height = c - 48;
         mode = MODE_HEIGHT_START;
         break;
       case MODE_HEIGHT_START:
	 fout.write(&c, sizeof(c));
         if ( c == '\n' || c == '\t' || c == '\r' || c == ' ' ) {
	   mode = MODE_HEIGHT_END;
           continue;
	 }
         height *= 10;
         height += c - 48;
         break;
       case MODE_HEIGHT_END:
	 fout.write(&c, sizeof(c));
         if ( c == '\n' || c == '\t' || c == '\r' || c == ' ' )
           continue;
         maxval = c - 48;
         mode = MODE_MAXVAL_START;
         break;
       case MODE_MAXVAL_START:
	 fout.write(&c, sizeof(c));
         if ( c == '\n' || c == '\t' || c == '\r' || c == ' ' ) {
	   if ( maxval != 255 )
	     return 1;
	   mode = MODE_PIXEL_R;
           continue;
	 }
         maxval *= 10;
         maxval += c - 48;
         break;
       case MODE_PIXEL_R:
	 r = c;
	 mode = MODE_PIXEL_G;
	 break;
       case MODE_PIXEL_G:
	 g = c;
	 mode = MODE_PIXEL_B;
	 break;
       case MODE_PIXEL_B:
	 b = c;
	 mode = MODE_PIXEL_A1;
	 break;
       case MODE_PIXEL_A1:
	 tmp = ((double) (r + g + b))/3;
         x = (char) tmp;
	 fout.write(&x, sizeof(x));
	 fout.write(&x, sizeof(x));
	 fout.write(&x, sizeof(x));
	 fout.write(&x, sizeof(x));
	pixels++;
	 //cout << "Complete pixel: " << hex << r << " " << g << " " << b << " " << a1 << " " << a2 << endl;
	 mode = MODE_PIXEL_R;
	 break;


     }
  }

  cout << dec;
  cout << "Width: " << width << endl;
  cout << "Height: " << height << endl;
  cout << "Maxval: " << maxval << endl;
  cout << "Read pixels: " << pixels << endl;
  return 0;

}
