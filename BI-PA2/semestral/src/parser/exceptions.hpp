#pragma once

#include <stdexcept>

/**
 * Exception thrown when there is syntax error <br>
 * such as =A, =5 + , =(4 + 2( - 5)), =3)
 */
class CInvalidExpression : public std::invalid_argument {

public:

    static constexpr const char * const WHAT = "!#EXPR";

    CInvalidExpression ()
    : std::invalid_argument ( WHAT ) {}

};

/**
 * Help class for stronger exceptions <br>
 * this exception has stronger priority in parsing
 */
class CStrongExpression : public std::invalid_argument {

public:

    CStrongExpression ( const char * name )
    : std::invalid_argument ( name ) {}

};

/**
 * Exception thrown when a cell tries to get its value <br>
 * includes also cyclic calling like A1 =A2, A2 =A1
 */
class CLoopExpression : public CStrongExpression {

public:

    static constexpr const char * const WHAT = "!#LOOP";

    CLoopExpression ()
    : CStrongExpression ( WHAT ) {}

};

/**
 * Exception thrown when trying to get unreferenced cell <br>
 * (cell which does not have defined value)
 */
class CRefExpression : public CStrongExpression {

public:

    static constexpr const char * const WHAT = "!#REF";

    CRefExpression ()
    : CStrongExpression ( WHAT ) {}

};
