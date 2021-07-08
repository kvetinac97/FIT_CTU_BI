#pragma once

#include "CUnaryFunction.hpp"

#include <cmath>

/**
 * Basic math abs function <br>
 * if x < 0: return -x, otherwise, return x
 */
class CAbsFunction : public CUnaryFunction {

public:

    CAbsFunction ( double parameter )
    : CUnaryFunction ( "abs", parameter ) {}

    virtual double calculate () const override {
        return abs ( m_Param );
    }

};
