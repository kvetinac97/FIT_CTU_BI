#pragma once

#include "../../CBinarySymbol.hpp"

#include <string>

/**
 * Base class for all binary functions <br>
 * contains function name and calculation
 */
class CBinaryFunction : public CBinarySymbol {

    /** Function name (3 lowercase characters) */
    std::string m_Name;

protected:

    CBinaryFunction ( const std::string & name, double left, double right )
    : CBinarySymbol ( left, right ), m_Name ( name ) {}

public:

    /** Performs calculation depending on the function */
    virtual double calculate () const = 0;

};
