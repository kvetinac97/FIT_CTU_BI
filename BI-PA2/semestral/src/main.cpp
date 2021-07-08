#include "CSpreadSheet.hpp"
#include "CCommandLoader.hpp"

int main ( int argc, char ** argv ) {

    // Main spreadsheet, help argument for asserts
    CSpreadSheet sheet;
    bool printMessages = argc <= 1 || argv[1] != std::string("assert");

    // Load
    CCommandLoader loader ( sheet, printMessages );
    loader.start();

    if ( printMessages )
        std::cout << "Finished." << std::endl;

    return 0;

}
