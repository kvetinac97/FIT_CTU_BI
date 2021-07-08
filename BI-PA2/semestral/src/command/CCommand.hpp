#pragma once

#include "../CSpreadSheet.hpp"
#include "../cell/CCellName.hpp"

#include <vector>
#include <string>

/**
 * Basic command class <br>
 * interface for setting arguments + execution
 */
class CCommand {

protected:

    /** Reference to sheet on which operations will be performed */
    CSpreadSheet & m_Sheet;
    /** Command argument list */
    std::vector<std::string> m_Args;

    /** Expected command argument count */
    int m_ExpectedArgc;
    /** Should the command print everything / only errors + print/get? */
    bool m_Print;

    /** Tries to parse cell position from first argument */
    bool getPosition ( CCellName & name, std::string & message );

    CCommand ( CSpreadSheet & sheet, int argc, bool print )
    : m_Sheet ( sheet ), m_ExpectedArgc ( argc ), m_Print ( print ) {}

public:

    virtual ~CCommand () = default;

    /** Constant getter for expected argument count */
    int getExpectedArgc () const {
        return m_ExpectedArgc;
    }
    void addArgument ( const std::string & argument ) {
        m_Args.push_back ( argument );
    }

    /**
     * Executes the command.
     * @param[out] message string description of execution result
     * @return value indicates execution success
     */
    virtual bool execute ( std::string & message ) = 0;

};
