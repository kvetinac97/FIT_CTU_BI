#pragma once

/**
 * Base class for binary operators/functions <br>
 * offers base interface for getting/setting left & right operand
 */
class CBinarySymbol {

protected:

    /** Left operand | function parameter */
    double m_Left;
    /** Right operand | function parameter */
    double m_Right;

    virtual ~CBinarySymbol () = default;

    CBinarySymbol ( double left, double right )
    : m_Left ( left ), m_Right ( right ) {}

public:

    /** Setter for left operand | function parameter */
    void replaceLeft  ( double left  ) {
        m_Left = left;
    }
    /** Setter for right operand | function parameter */
    void replaceRight ( double right ) {
        m_Right = right;
    }

};
