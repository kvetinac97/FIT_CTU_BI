#include "CSetCommand.hpp"

bool CSetCommand::execute ( std::string & message ) {

    // Get cell name
    CCellName cellName ( '\0', 0 );
    if ( !getPosition( cellName, message ) )
        return false;

    // Insert cell
    if ( m_Args[1].length() > 0 && m_Args[1][0] == '=' )
        m_Sheet.setCell( std::make_unique<CExpressionCell> ( CExpressionCell ( m_Sheet, cellName, m_Args[1] ) ) );
    else
        m_Sheet.setCell( std::make_unique<CStringCell> ( CStringCell ( cellName, m_Args[1] ) ) );

    if ( m_Print )
        message = "The value of cell " + m_Args[0] + " set to \"" + m_Args[1] + "\".";
    return true;

}
