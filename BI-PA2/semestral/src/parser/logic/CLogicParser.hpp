#pragma once

#include "../../CSpreadSheet.hpp"

#include "../CParser.hpp"
#include "../exceptions.hpp"
#include "../math/CMathParser.hpp"

#include "comp/CBinaryComparator.hpp"
#include "comp/CLessComparator.hpp"
#include "comp/CGreaterComparator.hpp"
#include "comp/CEqualComparator.hpp"
#include "comp/CNotEqualComparator.hpp"

#include <string>
#include <sstream>

/**
 * LogicParser class <br>
 * this class is capable of parsing the IF () expression
 */
class CLogicParser : public CParser {

    /**
     * Tries to get character from top of the stream
     * @param c the character to get
     * @param skipWS should whitespaces be skipped?
     * @return true and consumes character if read, false otherwise
     */
    bool checkChar ( char c, bool skipWS = true );

    /**
     * Loads comparator instance from the stream
     * @param[out] comparator set if parsing successfull
     * @return value indicates parsing success
     */
    bool loadComparator ( CBinaryComparator * & comparator );

    /**
     * Tries to load string (beggining in ") from the stream
     * @param[out] str string being loaded
     * @return value indicates parsing success
     */
    bool loadString ( std::string & str );

public:

    // Constructor
    CLogicParser ( const CSpreadSheet & sheet, std::set<CCellName> & cells,
        std::istringstream & istream ) : CParser ( sheet, cells, istream ) {}

    /**
     * Parses logical expression <br>
     *  - firstly, loads the IF ( introduction <br>
     *  - then loads the expression, comaprator, expression <br>
     *  - finally, determines the yes and no value (string/expression) <br>
     *    with semicolons and closing bracket
     */
    virtual bool getResult ( std::string & result, std::string & error ) override;

};
