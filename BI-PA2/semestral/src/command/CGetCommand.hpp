#pragma once

#include "../CSpreadSheet.hpp"
#include "CCommand.hpp"

#include <set>

/**
 * Get real cell content command ( get A1 ) <br>
 * the cell value is CALCULATED on each request
 */
class CGetCommand : public CCommand {

public:

    CGetCommand ( CSpreadSheet & sheet, bool print )
    : CCommand ( sheet, 1, print ) {}

    virtual bool execute ( std::string & message ) override;

};
