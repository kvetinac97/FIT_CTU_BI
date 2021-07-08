#pragma once

#include "../CSpreadSheet.hpp"
#include "CCommand.hpp"

#include <fstream>

/**
 * Save spreadsheet to file ( save file.txt ) <br>
 * keeps cell values including expressions
 */
class CSaveCommand : public CCommand {

public:

    CSaveCommand ( CSpreadSheet & sheet, bool print )
    : CCommand ( sheet, 1, print ) {}

    virtual bool execute ( std::string & message ) override;

};
