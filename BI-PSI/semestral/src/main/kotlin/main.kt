import java.io.*
import java.net.*
import kotlin.math.max
import kotlinx.coroutines.*

// === CONSTANTS ===
const val END = "${7.toChar()}${8.toChar()}"
const val SERVER_PORT = 19132
const val MESSAGE_TIMEOUT = 1000

const val RECHARGING_MESSAGE = "RECHARGING"
const val FULL_POWER_MESSAGE = "FULL POWER"
const val RECHARGING_TIMEOUT = 5000

// Allowed server-client keypair
val KEYS = arrayOf (
    Pair(23019, 32037),
    Pair(32037, 29295),
    Pair(18789, 13603),
    Pair(16443, 29533),
    Pair(18189, 21952),
)

// Obstacle move sequence
val OBSTACLE_MOVES_LEFT  = arrayOf(
    ServerResponse.TURN_LEFT,
    ServerResponse.MOVE,
    ServerResponse.TURN_RIGHT,
    ServerResponse.MOVE,
)
val OBSTACLE_MOVES_RIGHT = arrayOf(
    ServerResponse.TURN_RIGHT,
    ServerResponse.MOVE,
    ServerResponse.TURN_LEFT,
    ServerResponse.MOVE,
)

/**
 * Extension function allowing to hash string
 * @param key the server/client key used for hashing
 */
fun String.hash (key: Int) : Int {
    val sum = ( this.toCharArray().sumBy { it.toInt() } * 1000 ) % 65536
    return ( sum + key ) % 65536
}

/**
 * Helper enum class for robot phases
 * each phase includes maximum allowed message length
 */
enum class RobotPhase (val maxLength: Int) {
    // Login
    WAITING_FOR_NAME (20),
    WAITING_FOR_KEY_ID (5),
    WAITING_FOR_CONFIRMATION (7),
    // Moving
    WAITING_FOR_MOVE_RESPONSE (12),
    WAITING_FOR_SECRET (100),
    // Recharging
    RECHARGING (12)
}

/** Helper enum class for server response */
enum class ServerResponse (private val text: String) {
    // Move
    MOVE ("102 MOVE"),
    TURN_LEFT ("103 TURN LEFT"),
    TURN_RIGHT ("104 TURN RIGHT"),
    // General
    PICK_UP ("105 GET MESSAGE"),
    LOGOUT ("106 LOGOUT"),
    KEY_REQUEST ("107 KEY REQUEST"),
    SERVER_OK ("200 OK"),
    // Error
    LOGIN_FAILED ("300 LOGIN FAILED"),
    SYNTAX_ERROR ("301 SYNTAX ERROR"),
    LOGIC_ERROR ("302 LOGIC ERROR"),
    KEY_OUT_OF_RANGE_ERROR ("303 KEY OUT OF RANGE");

    override fun toString() = text
}

/** Helper enum class to hold direction information */
enum class Direction {
    NORTH, EAST, SOUTH, WEST;
    fun turnLeft () = when(this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }
}

/** Helper data class for robot position */
data class Position (val x: Int, val y: Int, val dir: Direction? = null) {
    fun obstacleMoves () : Array<ServerResponse> {
        val rightMoves = (x > 0 && y > 0 && dir == Direction.SOUTH) ||
                         (x > 0 && y < 0 && dir == Direction.WEST ) ||
                         (x < 0 && y < 0 && dir == Direction.NORTH) ||
                         (x < 0 && y > 0 && dir == Direction.EAST)
        return if (rightMoves) OBSTACLE_MOVES_RIGHT else OBSTACLE_MOVES_LEFT
    }
    fun detectPosition (diff: Position) : Position? {
        val direction = when (diff) {
            Position(1, 0)  -> Direction.EAST
            Position(-1, 0) -> Direction.WEST
            Position(0, 1)  -> Direction.NORTH
            Position(0, -1) -> Direction.SOUTH
            else -> return null
        }
        return Position(x, y, direction)
    }
    fun bestMove () : ServerResponse {
        val shouldMove = when (dir) {
            Direction.NORTH -> y < 0
            Direction.SOUTH -> y > 0
            Direction.WEST  -> x > 0
            Direction.EAST  -> x < 0
            else -> false
        }
        return if (shouldMove) ServerResponse.MOVE else ServerResponse.TURN_LEFT
    }
    fun turnLeft () = Position(x, y, dir?.turnLeft())
    operator fun minus (other: Position) = Position(x - other.x, y - other.y, dir)
}

/** Helper message reader class */
class Reader (private val reader: BufferedReader) {

    /**
     * Tries to read message from socket
     * valid: recharging / expected length + correct ending
     */
    private fun readBuffer (maxLength: Int) : String? {
        val builder = StringBuilder()
        while ( !builder.endsWith(END) && builder.length < max(maxLength, RECHARGING_MESSAGE.length + 2) )
            builder.append(reader.read().toChar())

        val valid = ( builder.length <= maxLength || "$RECHARGING_MESSAGE$END" == builder.toString() )
        return if ( builder.endsWith(END) && valid ) builder.substring(0, builder.length - 2) else null
    }

    /**
     * Reads message for robot
     * returns null and closes connection if read failed
     */
    fun readMessage (robot: Robot) : String? {
        val message : String?
        try { message = readBuffer(robot.phase.maxLength) }
        catch (timeout: SocketTimeoutException) {
            println("[SERVER] Error: Timeout, closing connection...")
            robot.socket.close()
            return null
        }
        if (message == null) {
            println("[SERVER] Syntax error, closing connection...")
            robot.endConnection(ServerResponse.SYNTAX_ERROR)
        }
        return message
    }

}

/** Robot move logic helper class */
class MoveLogic {

    // Current and last positions
    var position: Position? = null
    private var previous: Position? = null

    // Obstacle solving data
    private var initialObstacle = false
    private var obstacleSolving: Int? = null
    private var obstacleMoves: Array<ServerResponse> = OBSTACLE_MOVES_LEFT

    /**
     * Detects direction based on first two requests
     * schedules one more request if there is an obstacle
     */
    private fun detectDirection () : ServerResponse? {
        val position = position ?: return null
        val previous = previous ?: return null
        val diff = position - previous

        // obstacle at the beginning
        if (diff.x == 0 && diff.y == 0) {
            initialObstacle = !initialObstacle
            this.previous = this.position
            return if (initialObstacle) ServerResponse.TURN_LEFT else ServerResponse.MOVE
        }

        // update position based on diff
        this.position = position.detectPosition(diff)
        return null
    }

    /**
     * Handles moving when there is/was an obstacle
     * (continue in avoiding obstacle)
     */
    private fun avoidObstacle  () : ServerResponse? {
        val position = position ?: return null
        val osn: Int

        if (obstacleSolving == null) {
            obstacleMoves = position.obstacleMoves() // start avoiding
            osn = 0
        }
        else osn = (obstacleSolving ?: 0) + 1 // continue avoiding
        obstacleSolving = if (osn == obstacleMoves.size - 1) null else osn
        return obstacleMoves[osn]
    }

    /**
     * Finds the best move and returns it
     * 1. handles initial position and direction detecting
     * 2. handles classic moving
     * 3. detects obstacles and avoids it
     */
    fun execute () : ServerResponse? {
        // Position not set yet, move once again
        if (previous == null) {
            previous = position
            return ServerResponse.MOVE
        }

        // Direction is not known = detect it
        if (position?.dir == null) {
            val response = detectDirection()
            if (response != null) return response
        }

        // We know the direction and position, we can act
        val position = position ?: return null
        if ( obstacleSolving != null || previous == position )
            return avoidObstacle()

        // Find the best move and execute it
        val move = position.bestMove()
        this.position = if (move == ServerResponse.TURN_LEFT) position.turnLeft() else position
        this.previous = position
        return move
    }

}

/** Robot controller class */
class Robot (val socket: Socket) {

    // Socket utilities
    private val reader: Reader
    private val writer: BufferedWriter

    // Current phase information
    var phase = RobotPhase.WAITING_FOR_NAME
    private lateinit var backupPhase: RobotPhase

    // Current robot information
    private lateinit var name: String
    private lateinit var key: Pair<Int, Int>
    private val moveLogic = MoveLogic()

    init {
        println("[SERVER] Client login")
        reader = Reader(BufferedReader(InputStreamReader(socket.getInputStream())))
        writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        waitForResponse()
    }

    // Output tools
    private fun sendResponse (response: Any) {
        println("[SERVER] Sending $response")
        writer.write("$response$END")
        writer.flush()
    }
    fun endConnection (response: ServerResponse) {
        println("[SERVER] Ending connection (reason $response)")
        sendResponse(response)
        socket.close()
    }

    /** Set recharging phase and backup current phase */
    private fun startRecharging () {
        backupPhase = phase
        phase = RobotPhase.RECHARGING
        waitForResponse()
    }

    /** Remove recharging phase and return backup phase */
    private fun stopRecharging (message: String) {
        if (message != FULL_POWER_MESSAGE) {
            endConnection(ServerResponse.LOGIC_ERROR)
            return
        }

        phase = backupPhase
        waitForResponse()
    }

    /** Initial login setup (get robot's name) */
    private fun waitingForName      (message: String) {
        name = message
        phase = RobotPhase.WAITING_FOR_KEY_ID
        sendResponse(ServerResponse.KEY_REQUEST)
        waitForResponse()
    }

    /** Initial key setup (get and validate robot key) */
    private fun waitingForKeyID     (message: String) {
        val keyId = message.toIntOrNull()
        if (keyId == null || keyId !in KEYS.indices) {
            endConnection( if (keyId == null) ServerResponse.SYNTAX_ERROR else ServerResponse.KEY_OUT_OF_RANGE_ERROR )
            return
        }

        key = KEYS[keyId]
        phase = RobotPhase.WAITING_FOR_CONFIRMATION
        sendResponse(name.hash(key.first).toString())
        waitForResponse()
    }

    /** Finish key setup (get and validate key confirmation) */
    private fun waitingConfirmation (message: String) {
        val keyConfirmation = message.toIntOrNull()
        if ( keyConfirmation != name.hash(key.second) ) {
            endConnection( if (keyConfirmation == null) ServerResponse.SYNTAX_ERROR else ServerResponse.LOGIN_FAILED )
            return
        }

        sendResponse(ServerResponse.SERVER_OK)
        sendResponse(ServerResponse.MOVE)
        phase = RobotPhase.WAITING_FOR_MOVE_RESPONSE
        waitForResponse()
    }

    /** Handle robot moving */
    private fun waitingMoveResponse (message: String) {
        val position = if (message.startsWith("OK ")) parseMoveResponse(message) else null
        if (position == null) {
            endConnection(ServerResponse.SYNTAX_ERROR)
            return
        }

        // We're on center coordinates, pick it up
        if (position == Position(0, 0)) {
            sendResponse(ServerResponse.PICK_UP)
            phase = RobotPhase.WAITING_FOR_SECRET
            waitForResponse()
            return
        }

        // Update position coordinates
        val movePos = moveLogic.position
        moveLogic.position = if (movePos == null) position else Position(position.x, position.y, movePos.dir)

        // Get move command and execute it
        val command = moveLogic.execute() ?: return
        sendResponse(command)
        waitForResponse()
    }
    private fun parseMoveResponse (message: String) : Position? {
        val nums = message.substring(3).split(" ")
        if (nums.size == 2) {
            val x = nums[0].toIntOrNull()
            val y = nums[1].toIntOrNull()
            if (x != null && y != null)
                return Position(x, y)
        }
        return null
    }

    /** Main waiting function, reacts based on current phase */
    private fun waitForResponse () {
        // Read message, if there was an error, skip
        socket.soTimeout = if (phase == RobotPhase.RECHARGING) RECHARGING_TIMEOUT else MESSAGE_TIMEOUT
        val message = reader.readMessage(this) ?: return
        println("[SERVER] Read $message whilst in phase $phase")

        // Recharging
        if (message == RECHARGING_MESSAGE) {
            startRecharging()
            return
        }

        // Execute commands based on current phase
        when (phase) {
            RobotPhase.WAITING_FOR_NAME          -> waitingForName(message)
            RobotPhase.WAITING_FOR_KEY_ID        -> waitingForKeyID(message)
            RobotPhase.WAITING_FOR_CONFIRMATION  -> waitingConfirmation(message)
            RobotPhase.RECHARGING                -> stopRecharging(message)
            RobotPhase.WAITING_FOR_MOVE_RESPONSE -> waitingMoveResponse(message)
            RobotPhase.WAITING_FOR_SECRET        -> endConnection(ServerResponse.LOGOUT)
        }
    }

}

/**
 * Server class listening for incoming connections
 * for each connection, new coroutine created
 */
class Server (private val server: ServerSocket = ServerSocket(SERVER_PORT)) {
    fun run () {
        println("[SERVER] Listening on port $SERVER_PORT")
        while (true) {
            val socket = server.accept()
            GlobalScope.launch { Robot(socket) }
        }
    }
}

fun main () = Server().run()