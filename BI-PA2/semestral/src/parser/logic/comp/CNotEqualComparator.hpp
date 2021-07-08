#pragma once

#include "CBinaryComparator.hpp"

/** LEFT != RIGHT */
class CNotEqualComparator : public CBinaryComparator {

public:

    CNotEqualComparator () : CBinaryComparator( '!', '=' ) {}

    virtual bool compare () const override {
        return m_Left != m_Right;
    }

};
