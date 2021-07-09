package thedrake;

import java.io.PrintWriter;

public enum TroopFace implements JSONSerializable {

    AVERS,
    REVERS;

    public TroopFace opposite () {
        return (this == AVERS ? REVERS : AVERS);
    }

    @Override
    public void toJSON ( PrintWriter writer ) {
        writer.write("\"" + name() + "\"");
        writer.flush();
    }

}
