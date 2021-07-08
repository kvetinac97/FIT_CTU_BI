#pragma once

#include "CCell.hpp"

/**
 * Base cell containing string data <br>
 * (can be date, number or "just a string")
 */
class CStringCell : public CCell {

public:

    /**
     * Creates new instance of CStringCell. <br>
     * removes all quote (") characters as they are forbidden.
     */
    CStringCell ( const CCellName & name, const std::string & value ) : CCell ( name, "" ) {
        for ( auto it = value.begin(); it != value.end(); ++it )
            if ( *it != '"' )
                m_Value += (*it);
    }

    /**
     * Get cell value
     * @param[in,out] stack not used as there can happen no loops
     * @return raw cell string value
     */
    virtual std::string getValue ( std::set<CCellName> & stack ) const override {
        return getContent ();
    }

};
