#include "CClearCommand.hpp"

bool CClearCommand::execute ( std::string & message ) {

    // Get cell name
    CCellName cellName ( '\0', 0 );
    if ( !getPosition( cellName, message ) )
        return false;

    if ( !m_Sheet.removeCell ( cellName ) ) {
        message = "Could not remove cell " + m_Args[0] + " as it does not exist.";
        return false;
    }

    if ( m_Print )
        message = "Cell " + m_Args[0] + " has been removed successfully.";
    return true;

}
