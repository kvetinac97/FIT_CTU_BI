package thedrake;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class TroopTile implements Tile {

    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    // Constructor

    public TroopTile (Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    // Getters

    public PlayingSide side () {
        return side;
    }

    public TroopFace face () {
        return face;
    }

    public Troop troop () {
        return troop;
    }

    @Override
    public boolean canStepOn () {
        return false;
    }

    @Override
    public boolean hasTroop () {
        return true;
    }

    public TroopTile flipped () {
        return new TroopTile(troop, side, face.opposite());
    }

    @Override
    public List<Move> movesFrom (BoardPos pos, GameState state) {
        List<Move> actions = new ArrayList<>();

        for ( TroopAction action : troop.actions(face) )
            actions.addAll(action.movesFrom(pos, side, state));

        return actions;
    }

    @Override
    public void toJSON ( PrintWriter writer ) {

        String json = "{";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        troop.toJSON(printWriter);

        json += "\"troop\":" + stringWriter + ",";

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        side.toJSON(printWriter);

        json += "\"side\":" + stringWriter + ",";

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        face.toJSON(printWriter);

        json += "\"face\":" + stringWriter + "";

        json += "}";

        writer.write(json);
        writer.flush();

    }

}
