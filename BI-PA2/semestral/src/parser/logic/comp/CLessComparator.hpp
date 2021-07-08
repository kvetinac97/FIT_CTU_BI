#pragma once

#include "CBinaryComparator.hpp"

/**
 * Two variants: LEFT < RIGHT or LEFT <= RIGHT <br>
 * the variant is determined by second character
 */
class CLessComparator : public CBinaryComparator {

public:

    /** @param equal determines comparator type: <= or < */
    CLessComparator ( bool equal )
    : CBinaryComparator( '<', equal ? '=' : '\0' ) {}

    virtual bool compare () const override {
        return m_C2 == '=' ? m_Left <= m_Right : m_Left < m_Right;
    }

};
