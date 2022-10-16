package com.binar.binarch4.manager

import android.util.Log
import com.binar.binarch4.enum.GameState
import com.binar.binarch4.enum.PlayerSide
import com.binar.binarch4.enum.PlayerState
import com.binar.binarch4.enum.PlayerType
import com.binar.binarch4.model.Player
import kotlin.random.Random

interface RPSGameManager {
    fun initGame()
    fun movePlayerOne(type: String)
    fun movePlayerTwo(type: String)
    fun restartGame(isUsingMultiplayerMode: Boolean)
    fun startGame(isUsingMultiplayerMode: Boolean)
    fun resetGame()
}

interface RPSGameListener {
    fun onPlayerStatusChanged(player: Player)
    fun onResetImage()
    fun onGameStateChanged(gameState: GameState)
    fun onGameFinished(gameState: GameState, winner: Player)
}

open class RPSComputerEnemyGameManager(
    private val listener: RPSGameListener
) : RPSGameManager {

    private lateinit var playerOne: Player

    private lateinit var playerTwo: Player

    private lateinit var draw: Player

    protected lateinit var state: GameState

    override fun initGame() {
        setGameState(GameState.IDLE)
        playerOne = Player(PlayerSide.PLAYER_ONE, PlayerState.IDLE, PlayerType.PAPER)
        playerTwo = Player(PlayerSide.PLAYER_TWO, PlayerState.IDLE, PlayerType.PAPER)
        draw = Player(PlayerSide.DRAW, PlayerState.IDLE, PlayerType.PAPER)
        notifyPlayerDataChanged()
        setGameState(GameState.STARTED)
    }

    private fun notifyPlayerDataChanged() {
        if (playerOne.playerState == PlayerState.IDLE) {
            listener.onResetImage()
        }
        listener.onPlayerStatusChanged(
            playerTwo
        )
    }

    override fun movePlayerOne(type: String) {
        Log.d("Testing4", "movePlayerOne: $type")
        when (type) {
            "ROCK" -> setPlayerOneMovement(getPlayerTypeByOrdinal(0), PlayerState.CHOOSED)
            "PAPER" -> setPlayerOneMovement(getPlayerTypeByOrdinal(1), PlayerState.CHOOSED)
            "SCISSORS" -> setPlayerOneMovement(getPlayerTypeByOrdinal(2), PlayerState.CHOOSED)
        }
    }

    override fun movePlayerTwo(type: String) {
        when (type) {
            "ROCK" -> setPlayerTwoMovement(getPlayerTypeByOrdinal(0), PlayerState.CHOOSED)
            "PAPER" -> setPlayerTwoMovement(getPlayerTypeByOrdinal(1), PlayerState.CHOOSED)
            "SCISSORS" -> setPlayerTwoMovement(getPlayerTypeByOrdinal(2), PlayerState.CHOOSED)
        }
    }

    override fun restartGame(isUsingMultiplayerMode: Boolean) {
    }


    private fun setPlayerOneMovement(
        playerType: PlayerType = playerOne.playerType,
        playerState: PlayerState = playerOne.playerState
    ) {
        playerOne.apply {
            this.playerType = playerType
            this.playerState = playerState
        }
    }

    private fun setPlayerTwoMovement(
        playerPosition: PlayerType = playerTwo.playerType,
        playerState: PlayerState = playerTwo.playerState
    ) {
        playerTwo.apply {
            this.playerType = playerPosition
            this.playerState = playerState
        }
        listener.onPlayerStatusChanged(
            playerTwo
        )
    }

    private fun getPlayerTypeByOrdinal(index: Int): PlayerType {
        return PlayerType.values()[index]
    }

    protected fun setGameState(newGameState: GameState) {
        state = newGameState
        listener.onGameStateChanged(state)
    }

    override fun startGame(isUsingMultiplayerMode: Boolean) {
        if (!isUsingMultiplayerMode) {
            playerTwo.apply {
                playerType = getPlayerTwoType()
            }
        }
        checkPlayerWinner()
    }

    override fun resetGame() {
        initGame()
    }

    private fun checkPlayerWinner() {
        val winner = when {
            playerOne.playerType.ordinal == playerTwo.playerType.ordinal -> {
                setPlayerTwoMovement(playerOne.playerType, PlayerState.CHOOSED)
                draw
            }
            playerOne.playerType.ordinal == 2 && playerTwo.playerType.ordinal == 1 -> {
                setPlayerTwoMovement(PlayerType.PAPER, PlayerState.CHOOSED)
                playerOne
            }
            playerOne.playerType.ordinal == 2 && playerTwo.playerType.ordinal == 0 -> {
                setPlayerTwoMovement(PlayerType.ROCK, PlayerState.CHOOSED)
                playerTwo
            }
            playerOne.playerType.ordinal == 1 && playerTwo.playerType.ordinal == 2 -> {
                setPlayerTwoMovement(PlayerType.SCISSORS, PlayerState.CHOOSED)
                playerTwo
            }
            playerOne.playerType.ordinal == 1 && playerTwo.playerType.ordinal == 0 -> {
                setPlayerTwoMovement(PlayerType.ROCK, PlayerState.CHOOSED)
                playerOne
            }
            playerOne.playerType.ordinal == 0 && playerTwo.playerType.ordinal == 2 -> {
                setPlayerTwoMovement(PlayerType.SCISSORS, PlayerState.CHOOSED)
                playerOne
            }
            playerOne.playerType.ordinal == 0 && playerTwo.playerType.ordinal == 1 -> {
                setPlayerTwoMovement(PlayerType.PAPER, PlayerState.CHOOSED)
                playerTwo
            }
            else -> {
                draw
            }
        }
        setGameState(GameState.FINISHED)
        listener.onGameFinished(state, winner)
    }

    private fun getPlayerTwoType(): PlayerType {
        val randomPosition = Random.nextInt(0, until = PlayerType.values().size)
        return getPlayerTypeByOrdinal(randomPosition)
    }
}

class MultiplayerRPSGameManager(listener: RPSGameListener) : RPSComputerEnemyGameManager(listener) {

    override fun initGame() {
        super.initGame()
        setGameState(GameState.PLAYER_ONE_TURN)
    }

    override fun movePlayerOne(type: String) {
        super.movePlayerOne(type)
        setGameState(GameState.PLAYER_TWO_TURN)
    }

    override fun restartGame(isUsingMultiplayerMode: Boolean) {
        when (state) {
            GameState.PLAYER_ONE_TURN -> {
                setGameState(GameState.PLAYER_TWO_TURN)
            }
            GameState.PLAYER_TWO_TURN -> {
                startGame(isUsingMultiplayerMode)
            }
            GameState.FINISHED -> {
                resetGame()
            }
            else -> return
        }
    }

}