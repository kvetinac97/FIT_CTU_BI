#include "CSpreadSheet.hpp"
#include "cell/CCellName.hpp"
#include "cell/CExpressionCell.hpp"
#include "cell/CStringCell.hpp"

#include <set>

bool CSpreadSheet::removeCell ( const CCellName & position ) {

    auto it = m_Cells.find ( position );
    if ( it == m_Cells.end() )
        return false;

    m_Cells.erase ( it );
    return true;

}

void CSpreadSheet::exportSheet ( std::ostream & os ) const {

    char lastCol = 'A';
    int lastRow = 1;

    for ( auto it = m_Cells.begin(); it != m_Cells.end(); ++it ) {

        // indent
        if ( it->first.getRow() != lastRow ) {

            for ( int i = it->first.getRow() - lastRow; i > 0; i-- )
                os << std::endl;

            lastRow = it->first.getRow();
            lastCol = 'A';

        }

        // indent
        for ( int i = it->first.getCol() - lastCol; i > 0; i-- )
            os << ",";

        lastCol = it->first.getCol();
        std::set<CCellName> tmpStack;
        os << '"' << it->second->getValue(tmpStack) << '"';

    }

}

void CSpreadSheet::saveSheet   ( std::ostream & os ) const {

    for ( auto it = m_Cells.begin(); it != m_Cells.end(); ++it ) {
        os << it->first.getCol() << ' ' << it->first.getRow() <<
            ' ' << it->second->getContent() << std::endl;
    }

}

bool CSpreadSheet::loadSheet   ( std::istream & is ) {

    // Tmp properties
    char col, c1, c2;
    int row;

    std::string line;
    bool somethingLoaded = false, failed = false;

    // Backup cells
    std::map<CCellName, std::unique_ptr<CCell>> cells;
    std::swap ( cells, m_Cells );
    m_Cells.clear(); // clear garbage

    while ( is.peek() != EOF ) {

        // Read row and col (colName)
        if ( !( is >> std::noskipws >> col >> c1 >> row >> c2 ) || col < 'A' || col > 'Z' ||
                row <= 0 || row > CCellName::MAX_ROW_SIZE || c1 != ' ' || c2 != ' ' ) {

            failed = true;
            break;

        }

        CCellName cellName ( col, row );

        // Read content
        std::getline ( is, line );

        // Getline encountered error bit - error
        if ( is.eof() ) {
            failed = true;
            break;
        }

        // Load in memory
        if ( !is.fail() ) {
            if ( line[0] == '=' )
                setCell ( std::make_unique<CExpressionCell>( CExpressionCell (*this, cellName, line) ) );
            else
                setCell ( std::make_unique<CStringCell>( CStringCell ( cellName, line ) ) );

            somethingLoaded = true;
        }

    }

    bool success = ( somethingLoaded && !failed );

    // Restore backup
    if ( !success )
        std::swap ( m_Cells, cells );

    return success;

}
