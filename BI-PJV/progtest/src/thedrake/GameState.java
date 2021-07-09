package thedrake;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

public class GameState implements JSONSerializable {

	private final Board board;
	private final PlayingSide sideOnTurn;
	private final Army blueArmy;
	private final Army orangeArmy;
	private final GameResult result;
	
	public GameState ( Board board, Army blueArmy, Army orangeArmy ) {
		this (board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
	}
	
	public GameState ( Board board, Army blueArmy, Army orangeArmy, PlayingSide sideOnTurn, GameResult result) {
		this.board = board;
		this.sideOnTurn = sideOnTurn;
		this.blueArmy = blueArmy;
		this.orangeArmy = orangeArmy;
		this.result = result;
	}
	
	public Board board () {
		return board;
	}
	
	public PlayingSide sideOnTurn () {
		return sideOnTurn;
	}
	
	public GameResult result () {
		return result;
	}
	
	public Army army ( PlayingSide side ) {
		return side == PlayingSide.BLUE ? blueArmy : orangeArmy;
	}
	
	public Army armyOnTurn () {
		return army(sideOnTurn);
	}
	
	public Army armyNotOnTurn () {
		return sideOnTurn == PlayingSide.BLUE ? orangeArmy : blueArmy;
	}

	public Tile tileAt (BoardPos pos) {
		Optional<TroopTile> blueTile = blueArmy.boardTroops().at(pos);
		if ( blueTile.isPresent() )
			return blueTile.get();

		Optional<TroopTile> orangeTile = orangeArmy.boardTroops().at(pos);
		if ( orangeTile.isPresent() )
			return orangeTile.get();

		return board.at(pos);
	}

	private boolean canStepFrom (TilePos origin) {
		if ( blueArmy.boardTroops().isPlacingGuards() || orangeArmy.boardTroops().isPlacingGuards() ||
				result != GameResult.IN_PLAY || origin == TilePos.OFF_BOARD )
			return false;

		Optional<TroopTile> blueTile = blueArmy.boardTroops().at(origin);
		Optional<TroopTile> orangeTile = orangeArmy.boardTroops().at(origin);

		if ( !blueTile.isPresent() && !orangeTile.isPresent() )
			return false;

		if ( sideOnTurn == PlayingSide.BLUE && blueTile.isPresent() )
			return true;

		return sideOnTurn == PlayingSide.ORANGE && orangeTile.isPresent();
	}

	private boolean canStepTo (TilePos target) {
		if (result != GameResult.IN_PLAY || target == TilePos.OFF_BOARD )
			return false;

		Optional<TroopTile> blueTile = blueArmy.boardTroops().at(target);
		if ( blueTile.isPresent() )
			return false;

		Optional<TroopTile> orangeTile = orangeArmy.boardTroops().at(target);
		if ( orangeTile.isPresent() )
			return false;

		return ( board.at((BoardPos) target) == BoardTile.EMPTY || board.at((BoardPos) target) == null );
	}

	private boolean canCaptureOn (TilePos target) {
		if ( result != GameResult.IN_PLAY || target == TilePos.OFF_BOARD )
			return false;

		Optional<TroopTile> blueTile = blueArmy.boardTroops().at(target);
		Optional<TroopTile> orangeTile = orangeArmy.boardTroops().at(target);

		if ( !blueTile.isPresent() && !orangeTile.isPresent() )
			return false;

		if ( sideOnTurn == PlayingSide.BLUE && blueTile.isPresent() )
			return false;

		return ! ( sideOnTurn == PlayingSide.ORANGE && orangeTile.isPresent() );
	}
	
	public boolean canStep (TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canStepTo(target);
	}
	
	public boolean canCapture (TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canCaptureOn(target);
	}

	public boolean canPlaceFromStack (TilePos target) {
		if ( result != GameResult.IN_PLAY || target == TilePos.OFF_BOARD || !canStepTo(target) )
			return false;

		if ( sideOnTurn == PlayingSide.BLUE && !blueArmy.boardTroops().isLeaderPlaced() && target.j() != 0 )
			return false;

		if ( sideOnTurn == PlayingSide.ORANGE && !orangeArmy.boardTroops().isLeaderPlaced() && target.j() != board.dimension() - 1 )
			return false;

		if ( sideOnTurn == PlayingSide.BLUE && blueArmy.boardTroops().isPlacingGuards() &&
				!target.neighbours().contains(blueArmy.boardTroops().leaderPosition()) )
			return false;

		if ( sideOnTurn == PlayingSide.ORANGE && orangeArmy.boardTroops().isPlacingGuards() &&
				!target.neighbours().contains(orangeArmy.boardTroops().leaderPosition()) )
			return false;

		if ( sideOnTurn == PlayingSide.BLUE && blueArmy.boardTroops().isLeaderPlaced() &&
				!blueArmy.boardTroops().isPlacingGuards() ) {

			boolean found = false;
			for ( TilePos pos : target.neighbours() )
				if ( blueArmy.boardTroops().at(pos).isPresent() && blueArmy.boardTroops().at(pos).get().hasTroop() ) {
					found = true;
					break;
				}

			if ( !found )
				return false;

		}

		if ( sideOnTurn == PlayingSide.ORANGE && orangeArmy.boardTroops().isLeaderPlaced() &&
				!orangeArmy.boardTroops().isPlacingGuards() ) {

			boolean found = false;
			for ( TilePos pos : target.neighbours() )
				if ( orangeArmy.boardTroops().at(pos).isPresent() && orangeArmy.boardTroops().at(pos).get().hasTroop() ) {
					found = true;
					break;
				}

			if ( !found )
				return false;

		}

		List<Troop> stack = ( sideOnTurn == PlayingSide.BLUE ) ? blueArmy.stack() : orangeArmy.stack();
		return ( !stack.isEmpty() );
	}
	
	public GameState stepOnly (BoardPos origin, BoardPos target) {
		if ( canStep(origin, target) )
			return createNewGameState ( armyNotOnTurn(), armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY );
		
		throw new IllegalArgumentException();
	}
	
	public GameState stepAndCapture (BoardPos origin, BoardPos target) {
		if ( canCapture(origin, target) ) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if ( armyNotOnTurn().boardTroops().leaderPosition().equals(target) )
				newResult = GameResult.VICTORY;
			
			return createNewGameState( armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopStep(origin, target).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState captureOnly (BoardPos origin, BoardPos target) {
		if ( canCapture(origin, target) ) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if ( armyNotOnTurn().boardTroops().leaderPosition().equals(target) )
				newResult = GameResult.VICTORY;
			
			return createNewGameState( armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopFlip(origin).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState placeFromStack(BoardPos target) {
		if ( canPlaceFromStack(target) )
			return createNewGameState( armyNotOnTurn(), armyOnTurn().placeFromStack(target), GameResult.IN_PLAY );
		
		throw new IllegalArgumentException();
	}
	
	public GameState resign () {
		return createNewGameState( armyNotOnTurn(), armyOnTurn(), GameResult.VICTORY );
	}
	
	public GameState draw () {
		return createNewGameState( armyOnTurn(), armyNotOnTurn(), GameResult.DRAW );
	}
	
	private GameState createNewGameState ( Army armyOnTurn, Army armyNotOnTurn, GameResult result ) {
		if ( armyOnTurn.side() == PlayingSide.BLUE )
			return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
		
		return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
	}

	@Override
	public void toJSON ( PrintWriter writer ) {

		String json = "{";

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		result.toJSON(printWriter);

		json += "\"result\":" + stringWriter + ",";

		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
		board.toJSON(printWriter);

		json += "\"board\":" + stringWriter + ",";

		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
		blueArmy.toJSON(printWriter);

		json += "\"blueArmy\":" + stringWriter + ",";

		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
		orangeArmy.toJSON(printWriter);

		json += "\"orangeArmy\":" + stringWriter + "";

		json += "}";

		writer.write(json);
		writer.flush();

	}

}
