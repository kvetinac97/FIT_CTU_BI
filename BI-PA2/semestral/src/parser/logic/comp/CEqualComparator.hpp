#pragma once

#include "CBinaryComparator.hpp"

/** LEFT == RIGHT */
class CEqualComparator : public CBinaryComparator {

public:

    CEqualComparator () : CBinaryComparator( '=', '=' ) {}

    virtual bool compare () const override {
        return m_Left == m_Right;
    }

};
