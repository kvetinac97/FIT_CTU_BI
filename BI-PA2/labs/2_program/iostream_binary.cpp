#include <fstream>
#include <iostream>
#include <cstdint>
#include <string>

// === Konstanty =========================================================
const std::string FILE_NAME  = "image.ppm";
const int         MIN_WIDTH  = 100;
const int         MIN_HEIGHT = 100;
const int         MAX_WIDTH  = 1920;
const int         MAX_HEIGHT = 1080;

// === Barva (struktura) =================================================
struct COLOR {
    uint8_t red;
    uint8_t green;
    uint8_t blue;
};

// === Pomocné funkce (zápis do souboru) =================================
void write ( std::ofstream & outputFileStream, const uint8_t data ) {
    outputFileStream.write ( (const char *) & data, sizeof( uint8_t ) );
}

void write ( std::ofstream & outputFileStream, const COLOR & color ) {
    write ( outputFileStream, color . red   );
    write ( outputFileStream, color . green );
    write ( outputFileStream, color . blue  );
}

void writeAscii ( std::ofstream & outputFileStream, const int number ) {
    // TODO: itos
    std::string asciiNumber = std::to_string ( number );
    for ( size_t i = 0; i < asciiNumber.size(); ++i )
        write ( outputFileStream, (uint8_t) asciiNumber[ i ] );
}

void writePpmHeader ( std::ofstream & outputFileStream, int width, int height ) {
    write      ( outputFileStream, (uint8_t) 'P'    ); // Typ
    write      ( outputFileStream, (uint8_t) '6'    ); // Typ
    write      ( outputFileStream, (uint8_t) ' '    );
    writeAscii ( outputFileStream,           width  );
    write      ( outputFileStream, (uint8_t) ' '    );
    writeAscii ( outputFileStream,           height );
    write      ( outputFileStream, (uint8_t) ' '    );
    writeAscii ( outputFileStream,           255    ); // Maxvalue
    write      ( outputFileStream, (uint8_t) '\n'   );
}

bool readColor( COLOR & color ) {
    int byte;
    if ( ! ( std::cin >> byte ) || byte < 0 || byte > 255 )
        return false;
    color . red = byte;

    if ( ! ( std::cin >> byte ) || byte < 0 || byte > 255 )
        return false;
    color . green = byte;

    if ( ! ( std::cin >> byte ) || byte < 0 || byte > 255 )
        return false;
    color . blue = byte;

    return true;
}

bool readInput ( int & width, int & height, COLOR & startColor, COLOR & endColor ) {
    std::cout << "Mozne velikosti obrazku: min. "
              << MIN_WIDTH << " x " << MIN_HEIGHT
              << ", max. "
              << MAX_WIDTH << " x " << MAX_HEIGHT
              << std::endl;

    std::cout << "Pozadavana sirka:" << std::endl;
    if ( !( std::cin >> width ) || width < MIN_WIDTH || width > MAX_WIDTH )
        return false;

    std::cout << "Pozadavana vyska:" << std::endl;
    if ( !( std::cin >> height ) || height < MIN_HEIGHT || height > MAX_HEIGHT )
        return false;

    std::cout << "Zadejte RGB hodnotu pocatecni barvy jako 3 cisla:" << std::endl;
    if ( !readColor( startColor ) )
        return false;

    std::cout << "Zadejte RGB hodnotu koncove barvy jako 3 cisla:" << std::endl;
    if ( !readColor( endColor ) )
        return false;

    return true;
}

void paint ( const int width, const int height, const COLOR & startColor, const COLOR & endColor ) {
    std::ofstream outputFileStream ( FILE_NAME, std::ios::out | std::ios::binary );
    // TODO: kontrola souboru
    
    writePpmHeader ( outputFileStream, width, height );
    
    int redDiff   = ( (int) ( endColor . red   ) ) - startColor . red;
    int greenDiff = ( (int) ( endColor . green   ) ) - startColor . green;
    int blueDiff  = ( (int) ( endColor . blue   ) ) - startColor . blue;
    
    COLOR currentColor;
    for ( int h = 0; h < height; ++h ) {
        currentColor . red   = startColor . red   + h * redDiff   / (double)height;
        currentColor . green = startColor . green + h * greenDiff / (double)height;
        currentColor . blue  = startColor . blue  + h * blueDiff  / (double)height;
        
        for ( int w = 0; w < width; ++w ) {
            write ( outputFileStream, currentColor );
        }
    }

    outputFileStream . close ();
}

int main ( void ) {
    int   width,
          height;
    COLOR startColor,
          endColor;

    if ( !readInput( width, height, startColor, endColor ) ) {
        std::cout << "Neplatna volba. Program bude ukoncen." << std::endl;
        return 1;
    }

    paint ( width, height, startColor, endColor );
    return 0;
}
