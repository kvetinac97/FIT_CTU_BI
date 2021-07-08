#include "CPrintCommand.hpp"

bool CPrintCommand::execute ( std::string & message ) {

    // Print cell name
    CCellName cellName ( '\0', 0 );
    if ( !getPosition( cellName, message ) )
        return false;

    message = ( m_Print ? ( m_Args[0] + " = " ) : "" );
    message += "\"" + m_Sheet.getCellContent ( cellName ) + "\"";
    return true;

}
