#pragma once

#include "../CBinaryOperator.hpp"

/** Minus operator - */
class CMinusOperator : public CBinaryOperator {

public:

    CMinusOperator ( int pos )
    : CBinaryOperator ( PRIORITY_LOW, pos ) {}

    virtual double calculate () const override {
        return m_Left - m_Right;
    }

};
