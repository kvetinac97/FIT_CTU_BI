#pragma once

#include "../../CBinarySymbol.hpp"

#include <tuple>

/**
 * Base class for all operators <br>
 * offers operator priority and calculation
 */
class CBinaryOperator : public CBinarySymbol {

    /** Operator priority ( '*' > '+' ) */
    int m_Priority;

    /** Position / priority in expression ( left > right ) */
    int m_Pos;

protected:

    /** Lowest priority: +, - */
    const static int PRIORITY_LOW = 0;
    /** Moderate priority: *, / */
    const static int PRIORITY_MODERATE = 1;

    CBinaryOperator ( int priority, int position )
    : CBinarySymbol ( 0, 0 ), m_Priority ( priority ), m_Pos ( position ) {}

public:

    /**
     * Operator comparison friend function <br>
     * firstly check operator priority, then position ( left -> right )
     */
    friend bool operator < ( const CBinaryOperator & l, const CBinaryOperator & r ) {
        int l_pos = -l.m_Pos, r_pos = -r.m_Pos; // help with reversing order
        return std::tie ( l.m_Priority, l_pos ) < std::tie ( r.m_Priority, r_pos );
    }

    /** Performs calculation depending on the operator */
    virtual double calculate () const = 0;

};
