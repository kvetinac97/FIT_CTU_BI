#pragma once

#include "CUnaryFunction.hpp"

#include <cmath>

/** Trigonometric sine function */
class CSinFunction : public CUnaryFunction {

public:

    CSinFunction ( double parameter )
    : CUnaryFunction ( "sin", parameter ) {}

    virtual double calculate () const override {
        return sin ( m_Param );
    }

};
