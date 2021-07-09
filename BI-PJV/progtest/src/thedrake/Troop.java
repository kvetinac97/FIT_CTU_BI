package thedrake;

import java.io.PrintWriter;
import java.util.List;

public class Troop implements JSONSerializable {

    // Properties
    private String name;
    private Offset2D aversPivot;
    private Offset2D reversPivot;

    private List<TroopAction> aversActions;
    private List<TroopAction> reversActions;

    public Troop ( String name, Offset2D aversPivot, Offset2D reversPivot,
                   List<TroopAction> aversActions, List<TroopAction> reversActions ) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop ( String name, Offset2D pivot, List<TroopAction> aversActions,
                   List<TroopAction> reversActions ) {
        this ( name, pivot, pivot.clone(), aversActions, reversActions );
    }

    public Troop ( String name, List<TroopAction> aversActions, List<TroopAction> reversActions ) {
        this ( name, new Offset2D(1, 1), aversActions, reversActions );
    }

    public String name () {
        return this.name;
    }

    public Offset2D pivot ( TroopFace face ) {
        return face == TroopFace.AVERS ? aversPivot : reversPivot;
    }

    public List<TroopAction> actions (TroopFace face) {
        return face == TroopFace.AVERS ? aversActions : reversActions;
    }

    public String toJSON () {
        return "\"" + name + "\"";
    }

    @Override
    public void toJSON ( PrintWriter writer ) {
        writer.write( toJSON() );
        writer.flush();
    }

    @Override
    public Troop clone() { // help method
        return new Troop ( name, aversPivot, reversPivot, aversActions, reversActions );
    }

}
