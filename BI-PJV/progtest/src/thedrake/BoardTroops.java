package thedrake;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {

	private final PlayingSide playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	private final int guards;
	
	public BoardTroops (PlayingSide playingSide) {
		this ( playingSide, Collections.emptyMap(), TilePos.OFF_BOARD, 0 );
	}
	
	public BoardTroops (PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, TilePos leaderPosition, int guards) {
		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at (TilePos pos) {
		for ( BoardPos p : troopMap.keySet() )
			if ( p.equalsTo(pos.i(), pos.j()) )
				return Optional.ofNullable(troopMap.get(p));

		return Optional.empty();
	}
	
	public PlayingSide playingSide () {
		return playingSide;
	}
	
	public TilePos leaderPosition () {
		return leaderPosition;
	}

	public int guards () {
		return guards;
	}
	
	public boolean isLeaderPlaced() {
		return leaderPosition != null && leaderPosition != TilePos.OFF_BOARD;
	}
	
	public boolean isPlacingGuards () {
		return isLeaderPlaced() && guards < 2;
	}	
	
	public Set<BoardPos> troopPositions () {
		Set<BoardPos> trPos = new HashMap<>(troopMap).keySet();
		trPos.removeIf( boardPos -> !at(boardPos).isPresent() );
		return trPos;
	}

	public BoardTroops placeTroop (Troop troop, BoardPos target) {
		Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
		if ( at(target).isPresent() )
			throw new IllegalArgumentException();

		TilePos leaderPos = newTroopMap.isEmpty() ? target : leaderPosition;
		int guards = this.guards;

		if ( leaderPosition != TilePos.OFF_BOARD && guards < 2 )
			guards++;

		newTroopMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
		return new BoardTroops( playingSide, newTroopMap, leaderPos, guards );
	}
	
	public BoardTroops troopStep (BoardPos origin, BoardPos target) {

		if ( !at(leaderPosition).isPresent() || guards < 2 )
			throw new IllegalStateException();

		if ( at(target).isPresent() || !at(origin).isPresent() )
			throw new IllegalArgumentException();

		BoardTroops troops = troopFlip(origin);
		Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troops.troopMap);
		newTroopMap.put(target, newTroopMap.get(origin));
		newTroopMap.remove(origin);

		TilePos leaderPos;
		if ( troops.leaderPosition != null && origin.equalsTo(troops.leaderPosition.i(), troops.leaderPosition.j()) )
			leaderPos = target;
		else
			leaderPos = leaderPosition;

		return new BoardTroops( playingSide, newTroopMap, leaderPos, guards );

	}
	
	public BoardTroops troopFlip(BoardPos origin) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException("Cannot move troops before the leader is placed.");
		}
		
		if(isPlacingGuards()) {
			throw new IllegalStateException("Cannot move troops before guards are placed.");
		}
		
		if(!at(origin).isPresent())
			throw new IllegalArgumentException();
		
		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {

		if ( !at(leaderPosition).isPresent() || guards < 2 )
			throw new IllegalStateException();

		if ( !at(target).isPresent() )
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
		newTroopMap.remove(target);

		TilePos leaderPos;
		if ( leaderPosition != null && target.equalsTo(leaderPosition.i(), leaderPosition.j()) )
			leaderPos = TilePos.OFF_BOARD;
		else
			leaderPos = leaderPosition;

		return new BoardTroops( playingSide, newTroopMap, leaderPos, guards );

	}

	@Override
	public void toJSON ( PrintWriter writer ) {

		String json = "{";

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		playingSide.toJSON(printWriter);

		json += "\"side\":" + stringWriter + ",";

		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
		leaderPosition.toJSON(printWriter);

		json += "\"leaderPosition\":" + stringWriter + ",";
		json += "\"guards\":" + guards + ",";

		json += "\"troopMap\":{";

		List<BoardPos> positions = new ArrayList<>(troopMap.keySet());
		Collections.sort(positions);

		for ( BoardPos boardPos : positions ) {

			StringWriter strWr = new StringWriter();
			PrintWriter prtWr = new PrintWriter(strWr);

			boardPos.toJSON(prtWr);
			prtWr.write(":");
			troopMap.get(boardPos).toJSON(prtWr);
			prtWr.write(",");

			json += strWr;

		}

		json = positions.isEmpty() ? json : json.substring(0, json.length() - 1);
		json += "}}";

		writer.write(json);
		writer.flush();

	}

}
