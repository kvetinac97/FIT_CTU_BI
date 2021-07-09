package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.BoardPos;
import thedrake.Move;
import thedrake.Tile;

/**
 * Class representing one board tile
 */
public class TileView extends Pane {

    private final BoardPos pos;
    private Move move;
    private final ImageView moveImage;

    private Tile tile;
    private final TileBackgrounds bg = new TileBackgrounds();
    private final TileViewContext context;

    public TileView ( BoardPos pos, Tile tile, TileViewContext context ) {
        this.pos = pos;
        this.context = context;
        ( this.moveImage = new ImageView(getClass().getResource("/assets/move.png").toString()) ).setVisible(false);

        setPrefSize(100, 100);
        setTile(tile);
        getChildren().add(this.moveImage);
        setOnMouseClicked( e -> addFocus() );
    }

    BoardPos position () { return pos; }
    Move move () { return move; }
    Tile tile () {
        return tile;
    }
    ImageView moveImage () { return moveImage; }

    private static final Border border = new Border( new BorderStroke( Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3) ) );

    public void addFocus () {
        if ( move != null ) // move
            context.executeMove(move);
        else if ( tile.hasTroop() ) { // choose
            setBorder(border);
            context.tileViewSelected(this);
        }
        else { // try place from stack
            context.tileViewSelected(this);
        }
    }
    public void removeFocus () {
        setBorder(null);
    }

    public void setMove ( Move move ) {
        this.moveImage.setVisible( ( this.move = move ) != null );
    }

    public void setTile ( Tile tile ) {
        setBackground( bg.get(this.tile = tile) );
    }

}
