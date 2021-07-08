#pragma once

#include "../CSpreadSheet.hpp"
#include "CCommand.hpp"

#include <set>

/**
 * Print raw cell content command ( print A1 )
 */
class CPrintCommand : public CCommand {

public:

    CPrintCommand ( CSpreadSheet & sheet, bool print )
    : CCommand ( sheet, 1, print ) {}

    virtual bool execute ( std::string & message ) override;

};
