#pragma once

#include "../CSpreadSheet.hpp"
#include "CCommand.hpp"

/**
 * Remove cell command ( clear A1 )
 */
class CClearCommand : public CCommand {

public:

    CClearCommand ( CSpreadSheet & sheet, bool print )
    : CCommand ( sheet, 1, print ) {}

    virtual bool execute ( std::string & message ) override;

};
