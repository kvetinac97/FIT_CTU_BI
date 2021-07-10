<?php

const DEAD = '.';
const ALIVE = 'X';

// Help class for game plan
class GamePlan {
    /** @var int[][] $game containing alive cells */
    public $game = [];
    /** @var int $rows number of rows */
    public $rows = 0;
    /** @var int $cols number of columns */
    public $cols = 0;

    // Sets the cell at current position to alive state
    public function setCellAlive () {
        $this->game[$this->rows][$this->cols] = ALIVE;
    }
}

/**
 * Parse data from input string
 * @param string $string game field
 * @return GamePlan
 */
function readInput($string) {
    // Init game plan
    $game = new GamePlan();

    foreach ( str_split($string) as $char ) {
        // Next line
        if ( $char == PHP_EOL ) {
            $game->rows++;
            $game->cols = 0;
            continue;
        }

        if ( $char == ALIVE )
            $game->setCellAlive();
        $game->cols++;
    }

    return $game;
}

/**
 * Print game plan into output string
 * @param GamePlan $matrix
 * @return string
 */
function writeOutput($matrix) {
    // Prepare result string
    $result = "";

    for ( $i = 0; $i < $matrix->rows; $i++ ) {
        for ( $j = 0; $j < $matrix->cols; $j++ )
            $result .= isset($matrix->game[$i][$j]) ? ALIVE : DEAD;
        $result .= PHP_EOL;
    }

    // Remove ending EOL and return result
    return substr($result, 0, -1);
}

/**
 * Perform one step in game evolution
 * @param GamePlan $matrix
 * @return GamePlan
 */
function gameStep($matrix) {
    // Create a help copy
    $matrix2 = clone $matrix; // deep-copy

    // Update alive state
    for ( $i = 0; $i < $matrix->rows; $i++ )
        for ( $j = 0; $j < $matrix->cols; $j++ ) {
            // Count neighbours, starting with negative matrix->game[i][j]
            // negates matrix->game[i+addI][j+addJ] for addI=addJ=0
            $aliveNeighbours = -isset($matrix->game[$i][$j]);
            for ( $addI = -1; $addI <= 1; $addI++ )
                for ( $addJ = -1; $addJ <= 1; $addJ++ )
                    $aliveNeighbours += isset($matrix->game[$i+$addI][$j+$addJ]);

            // Dead to alive
            if ( !isset($matrix->game[$i][$j]) && $aliveNeighbours == 3 )
                $matrix2->game[$i][$j] = 1;

            // Alive to dead
            if ( isset($matrix->game[$i][$j]) && $aliveNeighbours != 2 && $aliveNeighbours != 3 )
                unset($matrix2->game[$i][$j]);
        }

    return $matrix2;
}
