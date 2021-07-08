#pragma once

#include "../../CBinarySymbol.hpp"

/**
 * Basic class for comparators <br>
 * comparator has at least one character ( <, >, =, ! ) <br>
 * and possibly a second one ( = )
 */
class CBinaryComparator : public CBinarySymbol {

protected:

    /** First, determining comparator character */
    char m_C1;
    /** Second character ( '=' or '\0' ) */
    char m_C2;

    CBinaryComparator ( char c1, char c2 = '\0' )
    : CBinarySymbol ( 0, 0 ), m_C1 ( c1 ), m_C2 ( c2 ) {};

public:

    /** Compares arguments set to class with replaceLeft, replaceRight */
    virtual bool compare () const = 0;

};
