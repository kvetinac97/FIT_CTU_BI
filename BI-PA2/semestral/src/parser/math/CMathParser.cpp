#include "CMathParser.hpp"

CBinaryFunction * CMathParser::fromNameBinary ( const std::string & name, double arg ) {
    
    if ( name == "min" )
        return new CMinFunction ( arg );
    if ( name == "max" )
        return new CMaxFunction ( arg );
    if ( name == "pow" )
        return new CPowFunction ( arg );

    return nullptr;
    
}
CUnaryFunction  * CMathParser::fromNameUnary  ( const std::string & name, double arg ) {
    
    if ( name == "abs" )
        return new CAbsFunction ( arg );
    if ( name == "sin" )
        return new CSinFunction ( arg );
    if ( name == "cos" )
        return new CCosFunction ( arg );

    return nullptr;
    
}
CBinaryOperator * CMathParser::loadOperator   ( int & pos ) {

    CBinaryOperator * op;
    m_IS >> std::ws;

    switch ( m_IS.peek() ) {
        case '+':
            op = new CPlusOperator ( pos++ );
            break;
        case '-':
            op = new CMinusOperator ( pos++ );
            break;
        case '*':
            op = new CTimesOperator ( pos++ );
            break;
        case '/':
            op = new CDivideOperator ( pos++ );
            break;
        default:
            return nullptr;
    }

    m_IS.get();
    return op;

}

int CMathParser::getSign () {

    int sign = 1;

    do {
        m_IS >> std::ws;
        char c = m_IS.peek();

        if ( c == '+' || c == '-' )
            m_IS.get(c);
        else
            break;

        sign *= ( c == '-' ) ? -1 : 1;
    }
    while ( true );

    return sign;

}

bool CMathParser::loadBracket  ( char c ) {

    m_IS >> std::ws;
    if ( m_IS.peek() == c ) {
        m_IS.get();
        return true;
    }

    return false;

}
bool CMathParser::loadNumber   ( double & number ) {

    m_IS >> std::ws;
    char c = m_IS.peek();

    if ( isdigit (c) && ( m_IS >> number ) )
        return true;

    m_IS.clear();

    // Cell
    m_IS >> std::ws;
    if ( isupper(m_IS.peek()) ) {
        loadCell(number); // throws if fails
        return true;
    }

    return false;

}
void CMathParser::loadCell     ( double & result ) {

    char cellId = m_IS.get();
    int rowId;

    if ( !(m_IS >> rowId) )
        throw CInvalidExpression();

    CCellName cellName ( cellId, rowId );

    if ( m_Cells.find(cellName) != m_Cells.end() )
        throw CLoopExpression(); // A1 = A1

    std::istringstream is ( m_Sheet.getCellValue(cellName, m_Cells) );

    if ( is.str() == CRefExpression::WHAT )
        throw CRefExpression();

    // Cell content not double/loop
    if ( !(is >> result) || is.peek() != EOF ) {
        // "Exception propagation"
        if ( is.str() == CLoopExpression::WHAT )
            throw CLoopExpression ();

        throw CInvalidExpression();
    }

}
void CMathParser::loadFunction ( double & result ) {

    double arg1 = 0, arg2 = 0;
    char funName[4];
    funName[3] = '\0';

    // XXX (
    if ( ! ( m_IS >> funName[0] >> funName[1] >> funName[2] ) || !loadBracket() )
        throw CInvalidExpression();

    loadExpression ( arg1 ); // throws if fails

    // The function itself
    CBinaryFunction * binary = fromNameBinary ( funName, arg1 );

    if ( binary ) {

        if ( !loadBracket(';') ) {
            delete binary;
            throw CInvalidExpression();
        }

        try {
            loadExpression ( arg2 );
        }
        catch ( const std::invalid_argument & exception ) {
            delete binary; // prevent memory leak
            throw exception; // rethrow
        }

        binary->replaceRight ( arg2 );
        result = binary->calculate();

    }
    else {

        CUnaryFunction * unary = fromNameUnary ( funName, arg1 );

        if ( !unary )
            throw CInvalidExpression();

        result = unary->calculate();
        delete unary;

    }
    delete binary;

    if ( !loadBracket(')') )
        throw CInvalidExpression();

}

bool CMathParser::operatorGreedy ( double left, double & result ) {

    // Variables
    CBinaryOperator * op = nullptr;
    double right;
    int pos = 0;

    // Order + list of operators
    auto cmp = []( CBinaryOperator * l, CBinaryOperator * r ) { return *l < *r; };
    std::priority_queue<CBinaryOperator *, std::vector<CBinaryOperator *>, decltype(cmp)> operations ( cmp );
    std::vector<CBinaryOperator *> operationVector;

    // Loading every operator
    while ( ( op = loadOperator ( pos ) ) ) {

        try {
            // Load either number/cell, or expression
            if ( !loadNumber ( right ) )
                loadBracketExpression ( right, true );
        }
        catch ( const std::invalid_argument & exception ) {
            delete op; // prevent leak
            throw exception; // rethrow
        }

        op->replaceLeft ( left );
        op->replaceRight ( right );

        operations.push ( op );
        operationVector.push_back ( op );

        left = right;

    }

    // No valid operations, finish evaluating
    if ( operations.size() == 0 )
        return false;

    // Calculate priorities
    auto it = operationVector.begin();

    while ( !operations.empty() ) {

        CBinaryOperator * op = operations.top();
        operations.pop();

        it = operationVector.begin();
        while ( *it != op )
            ++it;

        result = (*it)->calculate();

        if ( (it + 1) != operationVector.end() )
            (*(it + 1))->replaceLeft  ( result );

        if ( it != operationVector.begin() )
            (*(it - 1))->replaceRight ( result );

        delete *it;
        operationVector.erase ( it );
    }

    return true;

}

void CMathParser::loadBracketExpression ( double & result, bool greedy ) {

    // Properties
    int sign = getSign();
    bool bracket = loadBracket();

    loadExpression ( result, !bracket && greedy ); // throws if fails
    result *= sign;

    if ( bracket && !loadBracket ( ')' ) )
        throw CInvalidExpression();

}
void CMathParser::loadExpression        ( double & result, bool greedy ) {

    double left;
    int sign = getSign();

    // Literal (number | cell) | Binary operator
    if ( loadNumber ( left ) ) {

        left *= sign;

        // Binary math
        if ( !greedy && operatorGreedy ( left, result ) )
            return;

        // Number
        result = left;
        return;

    }

    // Binary operator
    m_IS >> std::ws;
    if ( m_IS.peek() == '(' ) {

        double left;
        loadBracketExpression ( left ); // throws if fails

        if ( !operatorGreedy ( left, result ) )
            result = left;

        result *= sign;
        return;

    }

    // There must be function, fail otherwise
    loadFunction ( left );

    // There could be another operator after the function
    if ( !greedy && operatorGreedy ( left, result ) ) {
        result *= sign;
        return;
    }

    result = left;
    result *= sign;

}

bool CMathParser::getResult ( double & result, std::string & error ) {

    // Error in parsing is unrepairable fault
    try {
        loadExpression ( result );
        return true;
    }
    catch ( const CStrongExpression & err ) {
        error = err.what();
        return false;
    }
    catch ( const std::invalid_argument & err ) {
        try {
            m_IS.clear();
            m_IS.seekg ( 0, std::ios::beg );

            loadBracketExpression ( result );
            return true;
        }
        catch ( const std::invalid_argument & ) {
            error = err.what();
            return false;
        }
    }

}
bool CMathParser::getResult ( std::string & result, std::string & error ) {

    double tmp;

    if ( !getResult (tmp, error) )
        return false;

    std::ostringstream os;
    if ( tmp > 1e6 || tmp < -1e6 )
        os << std::fixed << std::setprecision(0) << tmp << std::flush;
    else
        os << tmp << std::flush;

    result = os.str();
    return true;

}
