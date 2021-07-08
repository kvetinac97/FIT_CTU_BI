#pragma once

#include "../CSpreadSheet.hpp"
#include "CCommand.hpp"

#include <fstream>

/**
 * Load spreadsheet from file ( load file.txt ) <br>
 * takes care of situations such as non-existing file
 */
class CLoadCommand : public CCommand {

public:

    CLoadCommand ( CSpreadSheet & sheet, bool print )
    : CCommand ( sheet, 1, print ) {}

    virtual bool execute ( std::string & message ) override;

};
