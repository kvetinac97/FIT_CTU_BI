#pragma once

#include "../CBinaryFunction.hpp"

/** Returns smaller of its two arguments */
class CMinFunction : public CBinaryFunction {

public:

    CMinFunction ( double left = 0, double right = 0 )
    : CBinaryFunction ( "min", left, right ) {}

    virtual double calculate () const override {
        return m_Left < m_Right ? m_Left : m_Right;
    }

};
