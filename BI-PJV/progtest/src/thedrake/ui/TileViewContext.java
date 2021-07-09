package thedrake.ui;

import thedrake.Move;

/**
 * Allows for better BoardView & TileView synchronization
 */
public interface TileViewContext {

    void tileViewSelected ( TileView view );
    void executeMove ( Move move );

}
