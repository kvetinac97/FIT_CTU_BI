#include "CCommandLoader.hpp"

CCommand * CCommandLoader::getCommand ( const std::string & name ) const {

    // Cell manipulation
    if ( name == "set" )
        return new CSetCommand    ( m_Sheet, m_Print );
    if ( name == "clear" )
        return new CClearCommand  ( m_Sheet, m_Print );
    if ( name == "print" )
        return new CPrintCommand  ( m_Sheet, m_Print );
    if ( name == "get" )
        return new CGetCommand    ( m_Sheet, m_Print );

    // Sheet manipulation
    if ( name == "save" )
        return new CSaveCommand   ( m_Sheet, m_Print );
    if ( name == "export" )
        return new CExportCommand ( m_Sheet, m_Print );
    if ( name == "load" )
        return new CLoadCommand   ( m_Sheet, m_Print );

    return nullptr;

}

void CCommandLoader::start () {

    // Loading in a loop
    while ( (bool) std::cin ) {

        CCommand * command = nullptr;
        std::string name, text;

        if ( !(std::cin >> name) || name == "stop" || name == "end" ) // EOF, end
            break;

        // Get command
        if ( ! ( command = getCommand ( name ) ) ) {
            std::cerr << "Error: No command with name " << name << " exists." << std::endl;
            continue;
        }

        // Get arguments
        int i = 0;

        for ( i = 0; i < command->getExpectedArgc(); i++ ) {

            text = "";
            std::cin >> std::ws;

            // Argument in quotes x no quotes (delimiter ' ')
            if ( i == command->getExpectedArgc() - 1 && std::cin.peek() == '"' ) {

                std::cin.get(); // quote
                std::getline ( std::cin, text );

                if ( text.back() != '"' ) {
                    std::cerr << "Error: Argument is not ended by quote." << std::endl;
                    break;
                }

                text = text.substr ( 0, text.length() - 1 );

            }
            else if ( ! (std::cin >> text) ) {
                std::cerr << "Error: Argument not found." << std::endl;
                break;
            }

            command->addArgument ( text );

        }

        // There has been an error
        if ( i != command->getExpectedArgc() ) {
            delete command;
            continue;
        }

        // Execute
        text = "";
        if ( !command->execute ( text ) )
            std::cerr << "Error: " << text << std::endl;
        else if ( !text.empty() )
            std::cout << ( m_Print ? "Success: " : "" ) << text << std::endl;

        delete command;

    }

}
