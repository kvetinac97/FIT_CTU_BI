#include "CLogicParser.hpp"

bool CLogicParser::checkChar ( char c, bool skipWS ) {

    if ( skipWS )
        m_IS >> std::ws;

    if ( m_IS.peek() != c )
        return false;

    m_IS.get(c);
    return true;

}

bool CLogicParser::loadComparator ( CBinaryComparator * & comparator ) {

    m_IS >> std::ws;
    char f = m_IS.get();
    bool hasEqual = false;

    if ( m_IS.peek() == '=' ) {
        hasEqual = true;
        m_IS.get();
    }

    switch ( f ) {
        case '<':
            comparator = new CLessComparator ( hasEqual );
            return true;
        case '>':
            comparator = new CGreaterComparator ( hasEqual );
            return true;
        case '=':
            if ( !hasEqual )
                return false;
            comparator = new CEqualComparator ();
            return true;
        case '!':
            if ( !hasEqual )
                return false;
            comparator = new CNotEqualComparator ();
            return true;
        default:
            return false;
    }

}

bool CLogicParser::loadString ( std::string & str ) {

    if ( !checkChar('"') ) // beginning
        return false;

    std::getline ( m_IS, str, '"' ); // consumes the '"' at the end
    return true;

}

bool CLogicParser::getResult ( std::string & result, std::string & error ) {

    // IF (
    if ( !checkChar('I') || !checkChar('F', false) || !checkChar('(') ) {
        error = CInvalidExpression::WHAT;
        return false;
    }

    CMathParser parser ( *this ); // create math parser
    CBinaryComparator * comp = nullptr;

    double left, right;
    std::string yes, no;

    // left ? right ; yes ; no )
    if ( !parser.getResult(left, error) || !loadComparator(comp) || !parser.getResult(right, error) ||
         !checkChar(';') || ( !loadString(yes) && !parser.getResult(yes, error)) ||
         !checkChar(';') || ( !loadString(no) && !parser.getResult(no, error)) ||
         !checkChar(')') ) {

        delete comp;
        if ( error.empty() )
            error = CInvalidExpression::WHAT;

        return false;

    }

    // Do the comparison
    comp->replaceLeft ( left );
    comp->replaceRight ( right );

    bool res = comp->compare();
    delete comp;

    result = ( res ? yes : no );
    return true;

}
