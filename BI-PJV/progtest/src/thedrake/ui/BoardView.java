package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import thedrake.BoardPos;
import thedrake.GameState;
import thedrake.Move;

/**
 * Class representing the game board
 */
public class BoardView extends GridPane implements TileViewContext {

    private GameState state;
    private final BoardViewContext context;
    private TileView selected;
    private ValidMoves moves;

    public GameState state () { return state; }

    public BoardView ( GameState state, BoardViewContext context ) {
        this.state = state;
        this.context = context;
        this.moves = new ValidMoves(state);

        for ( int y = 0; y < TheDrakeApp.DIMENSION; y++ )
            for ( int x = 0; x < TheDrakeApp.DIMENSION; x++ ) {
                BoardPos pos = new BoardPos( TheDrakeApp.DIMENSION, x, TheDrakeApp.DIMENSION - 1 - y );
                add ( new TileView( pos, state.tileAt (pos), this ), x, y );
            }

        // Space between elements, padding, aligning
        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);
    }

    private TileView tileAt ( BoardPos target ) {
        int index = ( TheDrakeApp.DIMENSION - 1 - target.j() ) * TheDrakeApp.DIMENSION + target.i();
        return (TileView) getChildren().get(index);
    }

    @Override
    public void tileViewSelected ( TileView view ) {
        // There is something already selected, deselect
        if ( selected != null && selected != view )
            selected.removeFocus();

        // Placing from stack
        if ( !view.tile().hasTroop() && state.canPlaceFromStack(view.position()) ) {
            state = state.placeFromStack(view.position());
            moves = new ValidMoves(state);
            redraw(); // update design
            return;
        }

        selected = view;

        // Clear moves
        for ( Node node : getChildren() )
            ( (TileView) node ).setMove(null);

        for ( Move move : moves.boardMoves(view.position()) )
            tileAt ( move.target() ).setMove ( move );
    }

    @Override
    public void executeMove ( Move move ) {
        selected.removeFocus();
        selected = null;

        // Clear moves
        for ( Node node : getChildren() )
            ( (TileView) node ).setMove(null);

        state = move.execute(state);
        moves = new ValidMoves(state);

        // Update design
        redraw();
    }

    private void redraw () {
        for ( Node node : getChildren() ) {
            TileView tileView = (TileView) node;
            tileView.setTile(state.tileAt(tileView.position()));
        }
        context.redrawStack(this);
    }

}
