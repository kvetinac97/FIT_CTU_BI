#pragma once

#include "../../CSpreadSheet.hpp"
#include "../CParser.hpp"
#include "../exceptions.hpp"

#include "binary/CBinaryOperator.hpp"
#include "binary/op/CPlusOperator.hpp"
#include "binary/op/CMinusOperator.hpp"
#include "binary/op/CTimesOperator.hpp"
#include "binary/op/CDivideOperator.hpp"

#include "binary/CBinaryFunction.hpp"
#include "binary/fun/CMinFunction.hpp"
#include "binary/fun/CMaxFunction.hpp"
#include "binary/fun/CPowFunction.hpp"

#include "unary/CUnaryFunction.hpp"
#include "unary/CAbsFunction.hpp"
#include "unary/CSinFunction.hpp"
#include "unary/CCosFunction.hpp"

#include <iomanip>
#include <set>
#include <queue>
#include <string>
#include <sstream>

/**
 * MathParser class <br>
 *  - handles all arithmetic expression parsing <br>
 *  - capable of parsing cell anchors, numbers, operators, functions <br>
 *  - detect loops, invalid expressions, bracket priority...
 */
class CMathParser : public CParser {

    /**
     * Get binary function
     * @param name searched function name
     * @param arg left argument of function
     * @return the function if found, nullptr otherwise
     */
    static CBinaryFunction * fromNameBinary ( const std::string & name, double arg );
    /**
     * Get unary function
     * @param name searched function name
     * @param arg the argument of function
     * @return the function if found, nullptr otherwise
     */
    static CUnaryFunction  * fromNameUnary  ( const std::string & name, double arg );

    /**
     * Get operator from input stream
     * @param pos operator position in expression
     * @return the operator if found, nullptr otherwise
     */
    CBinaryOperator * loadOperator   ( int & pos );

    /**
     * Custom sign loading function <br>
     * there can be 0 or more '+' or '-' symbols
     * @return 1 or -1 based on the plus and minus count
     */
    int getSign ();
    /**
     * Tries to get bracket or other character from input stream
     * @return true and consumes character if found, false otherwise
     */
    bool loadBracket ( char c = '(' );
    /**
     * Custom number and cell loading function <br>
     * this is due to C++ considering 'a1', '1d' as valid numbers <br>
     * for our purposes, numbers can start only with '+' / '-' / '0' to '9'
     *
     * @param[out] parsed number
     * @return value indicates parsing success
     * @throws invalid_argument if loop detected, cell name not valid or cell does not exist
     */
    bool loadNumber ( double & number );
    /**
     * Tries to parse cell value
     * @param[out] parsed cell value
     * @throws invalid_argument if loop detected, cell name not valid or cell does not exist
     */
    void loadCell ( double & result );
    /**
     * Loads unary or binary function <br>
     * determines function and its type by function name <br>
     * then, parses the rest (brackets, semicolon, second argument)
     *
     * @param[out] parsed function result
     * @throws invalid_argument if loop detected, cell name not valid or cell does not exist
     */
    void loadFunction ( double & result );

    /**
     * Loads expressions and operators as long as there are any <br>
     * in the end, the operator sequence is evaluated
     * @throws invalid_argument if loop detected, cell name not valid or cell does not exist
     *
     * @param left The initial operator left value
     * @param[out] result parsed operator sequence result
     * @return value indicates parsing result
     */
    bool operatorGreedy ( double left, double & result );

    /**
     * Tries to load expression in brackets <br>
     * consumes sign, brackets and expressions
     *
     * @param[out] result parsed expression result
     * @param greedy detects whether expression is on right side of operator
     * @throws invalid_argument if loop detected, cell name not valid or cell does not exist
     */
    void loadBracketExpression ( double & result, bool greedy = false );

    /**
     * Load complex expression <br>
     * expression can contain numbers, cells, operators and functions
     *
     * @param[out] result parsed expression result
     * @param greedy detects whether expression is on right side of operator
     * @throws invalid_argument if loop detected, cell name not valid or cell does not exist
     */
    void loadExpression ( double & result, bool greedy = false );

public:

    /**
     * Creates new parser
     * @param sheet to find cells on
     * @param cells set used to determine loops and prevent them
     * @param istream stream to parse from
     */
    CMathParser ( const CSpreadSheet & sheet, std::set<CCellName> & cells,
        std::istringstream & istream ) : CParser ( sheet, cells, istream ) {}

    /** Copy constructor (especially for creating from CLogicParser) */
    CMathParser ( const CParser & src ) : CParser ( src ) {}

    /** Overrided CParser method */
    virtual bool getResult ( std::string & result, std::string & error ) override;
    /**
     * Custom pretty-print of double result <br>
     * converts too big/little numbers to readable format
     * gets rid of extra zeroes in decimals ( 1.2500 => 1.25 )
     */
    bool getResult ( double & result, std::string & error );

};
