#pragma once

#include "../CSpreadSheet.hpp"
#include "CCommand.hpp"

#include <fstream>

/**
 * Exports spreadsheet to CSV file ( export file.csv ) <br>
 * works with calculated values ( = (5 + 3) => 8 )
 */
class CExportCommand : public CCommand {

public:

    CExportCommand ( CSpreadSheet & sheet, bool print )
    : CCommand ( sheet, 1, print ) {}

    virtual bool execute ( std::string & message ) override;

};
