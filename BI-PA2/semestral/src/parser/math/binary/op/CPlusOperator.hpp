#pragma once

#include "../CBinaryOperator.hpp"

/** Plus operator + */
class CPlusOperator : public CBinaryOperator {

public:

    CPlusOperator ( int pos )
    : CBinaryOperator ( PRIORITY_LOW, pos ) {}

    virtual double calculate () const override {
        return m_Left + m_Right;
    }

};
