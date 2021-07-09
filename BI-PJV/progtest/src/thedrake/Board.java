package thedrake;

import java.io.PrintWriter;

public class Board implements JSONSerializable {

	private int dimension;
	private BoardTile[][] board;

	// Constructors
	public Board (int dimension) {
		this.dimension = dimension;
		this.board = new BoardTile[dimension][dimension];

		for ( int i = 0; i < dimension; i++ )
			for ( int j = 0; j < dimension; j++ )
				board[i][j] = BoardTile.EMPTY;
	}

	// Getters

	public int dimension () {
		return dimension;
	}

	public BoardTile at (BoardPos pos) {
		return board[pos.i()][pos.j()];
	}

	public Board withTiles (TileAt ... ats) {
		Board board = new Board(dimension);

		for ( int i = 0; i < dimension; i++ )
			System.arraycopy(this.board[i], 0, board.board[i], 0, dimension);

		for ( TileAt tile : ats ) {
			board.board[tile.pos.i()][tile.pos.j()] = tile.tile;
		}

		return board;
	}

	public PositionFactory positionFactory() {
		return new PositionFactory(dimension);
	}
	
	public static class TileAt {

		public final BoardPos pos;
		public final BoardTile tile;
		
		public TileAt (BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}

	}

	@Override
	public void toJSON (PrintWriter writer) {

		String json = "{";
		StringBuilder tileBuilder = new StringBuilder("[");

		for ( int i = 0; i < dimension * dimension; i++ )
			tileBuilder.append(board[i % dimension][i / dimension].toJSON()).append(",");

		String tiles = tileBuilder.substring(0, tileBuilder.length() - 1);
		tiles += "]";

		json += "\"dimension\":" + dimension + ",";
		json += "\"tiles\":" + tiles + "";

		json += "}";

		writer.write(json);
		writer.flush();

	}

}

