#pragma once

#include "../cell/CCellName.hpp"

#include <set>
#include <string>
#include <sstream>

/**
 * Abstract parser class <br>
 * offers basic interface for parsing
 */
class CParser {

protected:

    /** Spreadsheet to read cell values from */
    const CSpreadSheet &  m_Sheet;

    /** Reference to stream to parse from */
    std::istringstream &     m_IS;

    /** Cells in current call (prevents loop) */
    std::set<CCellName> & m_Cells;

    CParser ( const CSpreadSheet & sheet, std::set<CCellName> & cells,
              std::istringstream & istream ) : m_Sheet ( sheet ),
              m_IS ( istream ), m_Cells ( cells ) {}

public:

    virtual ~CParser () = default;

    /**
     * Parses value from input stream
     * @param[out] result of parsing if successfull
     * @param[out] error connected with parsing if not successfull
     * @return value indicates parsing success
     */
    virtual bool getResult ( std::string & result, std::string & error ) = 0;

};
