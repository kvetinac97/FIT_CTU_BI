package thedrake;

import java.io.PrintWriter;

public enum PlayingSide implements JSONSerializable {

    ORANGE,
    BLUE;

    @Override
    public void toJSON ( PrintWriter writer ) {
        writer.write("\"" + name() + "\"");
        writer.flush();
    }

}