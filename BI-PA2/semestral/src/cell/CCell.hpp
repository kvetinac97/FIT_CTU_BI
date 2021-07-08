#pragma once

#include "CCellName.hpp"

#include <set>
#include <string>

/**
 * Basic cell class <br>
 * contains data and offers get/set (calculated) value interface
 */
class CCell {

protected:

    /** Cell name (A1, B5...) */
    CCellName m_Name;

    /** Internal value (expression, string...) */
    std::string m_Value;

    CCell ( const CCellName & name, const std::string & value )
    : m_Name ( name ), m_Value ( value ) {}

public:

    CCell () = delete;
    virtual ~CCell () = default;

    /**
     * Virtual method to get cell value <br>
     * @param[in,out] stack used for preventing loops (can be empty)
     */
    virtual std::string getValue ( std::set<CCellName> & stack ) const = 0;

    /** @return The actual cell name (A1, C15...) */
    const CCellName & getName () const {
        return m_Name;
    }

    /** @return Raw cell value (expression, string) */
    const std::string & getContent () const {
        return m_Value;
    }

};
