#pragma once

#include "../CSpreadSheet.hpp"
#include "../cell/CExpressionCell.hpp"
#include "../cell/CStringCell.hpp"
#include "CCommand.hpp"

/**
 * Set cell content command ( set A1 value ) <br>
 * detects expression/string cells based on first char ('=')
 */
class CSetCommand : public CCommand {

public:

    CSetCommand ( CSpreadSheet & sheet, bool print )
    : CCommand ( sheet, 2, print ) {}

    virtual bool execute ( std::string & message ) override;

};
