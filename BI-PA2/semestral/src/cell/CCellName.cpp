#include "CCellName.hpp"

bool CCellName::parsePosition ( const std::string & name ) {

    // Invalid name / col
    if ( name.length() < 2 || name[0] < 'A' || name[0] > 'Z' )
        return false;

    m_Col = name[0];

    // Invalid row start
    if ( !isdigit(name[1]) )
        return false;

    try {
        m_Row = std::stoi ( name.substr ( 1 ) );
    }
    catch ( const std::logic_error & ) {
        return false;
    }

    // Invalid row
    if ( m_Row > CCellName::MAX_ROW_SIZE || m_Row <= 0 ||
        ( m_Col + std::to_string(m_Row) ) != name )
        return false;

    return true;

}
