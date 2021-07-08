#pragma once

#include "../CBinaryOperator.hpp"

/**
 * Divide operator / <br>
 * Special cases such as 1/0 are handled by double (inf/nan)
 */
class CDivideOperator : public CBinaryOperator {

public:

    CDivideOperator ( int pos )
    : CBinaryOperator ( PRIORITY_MODERATE, pos ) {}

    virtual double calculate () const override {
        return m_Left / m_Right;
    }

};
