#include "CExportCommand.hpp"

bool CExportCommand::execute ( std::string & message ) {

    if ( m_Sheet.isEmpty() ) {
        message = "Spreadsheet is empty! (Nothing to export)";
        return false;
    }

    // Init file
    std::ofstream outFile ( m_Args[0] );
    m_Sheet.exportSheet ( outFile ); // perform export
    outFile.close();

    if ( !outFile ) {
        message = "File " + m_Args[0] + " couldn't be written to.";
        return false;
    }

    if ( m_Print )
        message = "Spreadsheet successfully exported to " + m_Args[0] + ".";
    return true;

}
