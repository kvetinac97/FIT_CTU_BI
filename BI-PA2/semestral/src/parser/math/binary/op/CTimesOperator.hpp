#pragma once

#include "../CBinaryOperator.hpp"

/** Times operator * */
class CTimesOperator : public CBinaryOperator {

public:

    CTimesOperator ( int pos )
    : CBinaryOperator ( PRIORITY_MODERATE, pos ) {}

    virtual double calculate () const override {
        return m_Left * m_Right;
    }

};
