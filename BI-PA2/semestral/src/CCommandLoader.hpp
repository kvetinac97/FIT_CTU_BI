#pragma once

#include "command/CCommand.hpp"
#include "command/CClearCommand.hpp"
#include "command/CExportCommand.hpp"
#include "command/CGetCommand.hpp"
#include "command/CLoadCommand.hpp"
#include "command/CPrintCommand.hpp"
#include "command/CSaveCommand.hpp"
#include "command/CSetCommand.hpp"

#include <iostream>
#include <string>

class CCommandLoader {

    /** Sheet to execute commands on */
    CSpreadSheet & m_Sheet;
    /** Should all command output be printed? */
    bool           m_Print;

    /** @return command with given name if exists, nullptr otherwise */
    CCommand * getCommand ( const std::string & name ) const;

public:

    /**
     * Creates new instance of command loader
     * @param sheet to execute commands on
     * @param print should command debug messages be printed?
     */
    CCommandLoader ( CSpreadSheet & sheet, bool print )
    : m_Sheet ( sheet ), m_Print ( print ) {}

    /** Load commands from standard input in a loop */
    void start ();

};
