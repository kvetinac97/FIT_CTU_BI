#pragma once

#include <string>

/**
 * Base class for all unary functions <br>
 * contains function name and parameter
 */
class CUnaryFunction {

    /** Function name (3 lowercase characters) */
    std::string m_Name;

protected:

    /** Function parameter */
    double m_Param;

    CUnaryFunction ( const std::string & name, double parameter )
    : m_Name ( name ), m_Param ( parameter ) {}

public:

    virtual ~CUnaryFunction () = default;

    /** Performs calculation depending on the function */
    virtual double calculate () const = 0;

};
