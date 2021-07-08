#pragma once

#include "cell/CCell.hpp"

#include <iostream>
#include <map>
#include <memory>
#include <set>
#include <string>

/**
 * Main Spreadsheet class <br>
 * used for holding and manipulating with cell data
 */
class CSpreadSheet {

    /** Save all cells in a map */
    std::map<CCellName, std::unique_ptr<CCell>> m_Cells;

public:

    /** Creates an empty spreadsheet (no cells) */
    CSpreadSheet () = default;
    /** @return does the spreadsheet have no cells? */
    bool isEmpty () const {
        return m_Cells.size() == 0;
    }

    /**
     * @param position of cell to get content of
     * @param[in,out] stack used to prevent loops
     * @return evaluated cell value
     */
    std::string getCellValue ( const CCellName & position, std::set<CCellName> & stack ) const {
        if ( m_Cells.find(position) == m_Cells.end() ) // not found
            return CRefExpression::WHAT;

        return m_Cells.at(position)->getValue( stack ); // get real value
    }
    /** @return raw content of cell at given position */
    std::string getCellContent ( const CCellName & position ) const {
        if ( m_Cells.find(position) == m_Cells.end() ) // not found
            return CRefExpression::WHAT;

        return m_Cells.at(position)->getContent(); // get content
    }

    /**
     * @param position on which we should remove cell <br>
     * @return value indicates removal success
     */
    bool removeCell ( const CCellName & position );
    /** Sets cell at given position (no CCellName validity check) */
    void setCell ( std::unique_ptr<CCell> && cell ) noexcept {
        std::swap ( m_Cells[cell->getName()], cell );
    }

    /**
     * Export spreadsheet (evaluated values) to CSV format
     * @param os the output stream to export to
     */
    void exportSheet ( std::ostream & os ) const;

    /**
     * Saves spreadsheet (raw cell values) <br>
     * save file format: COL ROW DATA
     * @param os the output stream to save to
     */
    void saveSheet   ( std::ostream & os ) const;

    /**
     * Tries to load spreadsheet from given input stream
     * @return false if file not open for reading, does not exist <br>
     * or there is syntax error in the save file, true otherwise
     */
    bool loadSheet   ( std::istream & is );

};
