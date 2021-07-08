#include "CCommand.hpp"

bool CCommand::getPosition ( CCellName & name, std::string & message ) {

    bool r;
    name = CCellName ( m_Args[0], r );

    if ( !r )
        message = "Could not parse " + m_Args[0] + " as position.";

    return r;

}
