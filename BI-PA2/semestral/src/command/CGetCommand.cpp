#include "CGetCommand.hpp"

bool CGetCommand::execute ( std::string & message ) {

    // Get cell name
    CCellName cellName ( '\0', 0 );
    if ( !getPosition( cellName, message ) )
        return false;

    std::set<CCellName> tmp;

    message = ( m_Print ? ( m_Args[0] + " = " ) : "" );
    message += "\"" + m_Sheet.getCellValue ( cellName, tmp ) + "\"";
    return true;

}
