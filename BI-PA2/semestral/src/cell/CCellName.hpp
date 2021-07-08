#pragma once

#include "../parser/exceptions.hpp"

#include <stdexcept>
#include <string>
#include <tuple>

/**
 * Special class representing cell name <br>
 * offers easier sorting and manipulation
 */
class CCellName {

    /** Column representation ('A' - 'Z') */
    char m_Col;
    /** Row representation (1 - 1000000) */
    int  m_Row;

    /**
     * Tries to parse position from provided string <br>
     * @param name (matching [A-Z][0-9][1-9]*)
     * @return true if success, false otherwise
     */
    bool parsePosition ( const std::string & name );

public:

    /** Maximum row size */
    const static int MAX_ROW_SIZE = 1000000;

    /**
     * Tries to create new CCellName
     * @param name valid cell name
     * @param[out] res indicates parsing result
     */
    CCellName ( const std::string & name, bool & res ) { res = parsePosition(name); }

    /** Creates new CCellName */
    CCellName ( char col, int row )
    : m_Col ( col ), m_Row ( row ) {}

    /** @return cell column ('A' - 'Z') */
    char getCol () const {
        return m_Col;
    }
    /** @return cell row (1 - 1000000) */
    int getRow () const {
        return m_Row;
    }

    /** Overrided < operator for easier implementation whilst sorting */
    friend bool operator < ( const CCellName & l, const CCellName & r ) {
        return std::tie ( l.m_Row, l.m_Col ) < std::tie ( r.m_Row, r.m_Col );
    }
    /** Overrided == operator for possible comparing of two cell names */
    friend bool operator == ( const CCellName & l, const CCellName & r ) {
        return !( l < r ) && !( r < l );
    }

};
