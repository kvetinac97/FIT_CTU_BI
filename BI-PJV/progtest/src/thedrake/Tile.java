package thedrake;

import java.io.PrintWriter;
import java.util.List;

public interface Tile extends JSONSerializable {

	public boolean canStepOn ();
	public boolean hasTroop ();

	public List<Move> movesFrom (BoardPos pos, GameState state);

	@Override
	void toJSON ( PrintWriter writer );

}
