#pragma once

#include "../CBinaryFunction.hpp"

#include <cmath>

/**
 * Returns first argument to power of the second one <br>
 * special cases such as 0<sup>-1</sup> are handled by double (inf, nan)
 */
class CPowFunction : public CBinaryFunction {

public:

    CPowFunction ( double left = 0, double right = 0 )
    : CBinaryFunction ( "pow", left, right ) {}

    virtual double calculate () const override {
        return pow ( m_Left, m_Right );
    }

};
