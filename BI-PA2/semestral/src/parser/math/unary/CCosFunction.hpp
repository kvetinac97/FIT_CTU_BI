#pragma once

#include "CUnaryFunction.hpp"

#include <cmath>

/** Trigonometric cosine function */
class CCosFunction : public CUnaryFunction {

public:

    CCosFunction ( double parameter )
    : CUnaryFunction ( "cos", parameter ) {}

    virtual double calculate () const override {
        return cos ( m_Param );
    }

};
