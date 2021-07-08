#include "CSaveCommand.hpp"

bool CSaveCommand::execute ( std::string & message ) {

    if ( m_Sheet.isEmpty() ) {
        message = "Spreadsheet is empty! (Nothing to save)";
        return false;
    }

    // Init file
    std::ofstream outFile ( m_Args[0] );
    m_Sheet.saveSheet ( outFile ); // perform save
    outFile.close();

    if ( !outFile ) {
        message = "File " + m_Args[0] + " couldn't be written to.";
        return false;
    }

    if ( m_Print )
        message = "Spreadsheet successfully saved to " + m_Args[0] + ".";
    return true;

}
