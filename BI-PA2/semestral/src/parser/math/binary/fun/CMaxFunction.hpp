#pragma once

#include "../CBinaryFunction.hpp"

/** Returns greater of its two arguments */
class CMaxFunction : public CBinaryFunction {

public:

    CMaxFunction ( double left = 0, double right = 0 )
    : CBinaryFunction ( "max", left, right ) {}

    virtual double calculate () const override {
        return m_Left < m_Right ? m_Right : m_Left;
    }

};
