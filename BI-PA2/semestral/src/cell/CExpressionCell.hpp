#pragma once

#include "../CSpreadSheet.hpp"
#include "../parser/logic/CLogicParser.hpp"
#include "../parser/math/CMathParser.hpp"

#include "CCell.hpp"

/**
 * Cell containing a logic/arithmetic expression
 */
class CExpressionCell : public CCell {

    /** Reference to sheet kept for cell evaluation */
    const CSpreadSheet & m_Sheet;

    /**
     * Determines parser type based on cell value
     * @param[in,out] is input stream for parsing
     * @param[in,out] stack prevents loop
     * @return the parser
     */
     CParser * getParser ( std::istringstream & is, std::set<CCellName> & stack ) const;

    /**
     * Inner evaluation method
     * checks for logical/arithmetic expression
     * if it is not valid, tries to resolve simple =CELL assignement
     *
     * @param[in,out] stack prevents loops
     * @return evaluated value
    */
    std::string evaluate ( std::set<CCellName> & stack ) const;

public:

    /** Creates new CExpressionCell */
    CExpressionCell ( const CSpreadSheet & sheet, const CCellName & name, const std::string & expression )
    : CCell ( name, expression ), m_Sheet ( sheet ) { }

    /**
     * Value getter used for evaluating
     * @param[in,out] stack prevents loops
     * @return evaluated cell value
     */
    virtual std::string getValue ( std::set<CCellName> & stack ) const override {
        return evaluate ( stack );
    }

};
