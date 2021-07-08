#include "CExpressionCell.hpp"

// ===== EXPRESSION CELL =====

CParser * CExpressionCell::getParser ( std::istringstream & is, std::set<CCellName> & stack ) const {

    // Logical expression
    if ( m_Value.substr(0, 3) == "=IF" )
        return new CLogicParser ( m_Sheet, stack, is );

    // Default - try math parser
    return new CMathParser ( m_Sheet, stack, is );

}

std::string CExpressionCell::evaluate ( std::set<CCellName> & stack ) const {

    if ( m_Value.empty() || m_Value[0] != '=' )
        return CInvalidExpression::WHAT;

    // Prepare stream and stack for parsing
    std::istringstream is ( m_Value.substr ( 1 ) );
    stack.insert ( m_Name );

    CParser * parser = getParser ( is, stack );
    std::string result, error = CInvalidExpression::WHAT;

    // Try to parse expression (polymorph)
    if ( parser->getResult ( result, error ) ) {

        is >> std::ws;
        delete parser;

        if ( is.peek() == EOF ) // success, no extra brackets
            return result;
        else
            return CInvalidExpression::WHAT;

    }

    delete parser;

    // If a loop/ref blocked interpreting an expression, we cannot continue
    if ( error == CLoopExpression::WHAT || error == CRefExpression::WHAT )
        return error;

    // Try primitive string assignment ( A2 = A1 )
    bool r = false;
    CCellName cellName ( m_Value.substr ( 1 ), r );

    // Could not parse
    if ( !r )
        return error;

    return m_Sheet.getCellValue ( cellName, stack );

}
