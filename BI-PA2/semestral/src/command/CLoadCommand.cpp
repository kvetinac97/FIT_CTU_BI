#include "CLoadCommand.hpp"

bool CLoadCommand::execute ( std::string & message ) {

    // Init file
    std::ifstream inFile ( m_Args[0] );

    if ( !inFile ) {
        message = "File does not exist / has no read permission.";
        inFile.close();
        return false;
    }

    if ( !m_Sheet.loadSheet ( inFile ) ) {
        message = "Invalid or empty input file.";
        inFile.close();
        return false;
    }

    inFile.close();

    if ( m_Print )
        message = "Spreadsheet successfully loaded from " + m_Args[0] + ".";
    return true;

}
