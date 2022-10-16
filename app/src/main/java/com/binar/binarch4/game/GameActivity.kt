package com.binar.binarch4.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.binar.binarch4.R
import com.binar.binarch4.databinding.ActivityGameBinding
import com.binar.binarch4.enum.GameState
import com.binar.binarch4.enum.PlayerSide
import com.binar.binarch4.enum.PlayerState
import com.binar.binarch4.enum.PlayerType
import com.binar.binarch4.manager.MultiplayerRPSGameManager
import com.binar.binarch4.manager.RPSComputerEnemyGameManager
import com.binar.binarch4.manager.RPSGameListener
import com.binar.binarch4.manager.RPSGameManager
import com.binar.binarch4.model.Player
import com.binar.binarch4.onboarding.CustomResultDialog
import com.binar.binarch4.onboarding.MenuGameActivity
import com.binar.binarch4.onboarding.OnMenuSelectedListener

class GameActivity : AppCompatActivity(), RPSGameListener {
    private val binding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    private val isUsingMultiplayerMode: Boolean by lazy {
        intent.getBooleanExtra(EXTRAS_MULTIPLAYER_MODE, false)
    }

    private val rpsGameManager: RPSGameManager by lazy {
        if (isUsingMultiplayerMode)
            MultiplayerRPSGameManager(this)
        else
            RPSComputerEnemyGameManager(this)
    }

    private val name: String? by lazy {
        intent.getStringExtra(EXTRAS_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        rpsGameManager.initGame()
        setOnClickListeners()
        supportActionBar?.hide()
        binding.tvPlayer.text = name
    }

    private fun setOnClickListeners() {
        binding.apply {
            ivLeftRock.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.ic_result_background)
                    setImageResource(R.drawable.ic_rock_left_pressed)
                    rpsGameManager.movePlayerOne("ROCK")
                    if (!isUsingMultiplayerMode) {
                        rpsGameManager.startGame(false)
                    }
                }
            }

            ivLeftPaper.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.ic_result_background)
                    setImageResource(R.drawable.ic_paper_left_pressed)
                    rpsGameManager.movePlayerOne("PAPER")
                    if (!isUsingMultiplayerMode) {
                        rpsGameManager.startGame(false)
                    }
                }
            }

            ivLeftScissors.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.ic_result_background)
                    setImageResource(R.drawable.ic_scissor_left_pressed)
                    rpsGameManager.movePlayerOne("SCISSORS")
                    if (!isUsingMultiplayerMode) {
                        rpsGameManager.startGame(false)
                    }
                }
            }

            ivRightRock.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.ic_result_background)
                    setImageResource(R.drawable.ic_rock_right_pressed)
                    rpsGameManager.movePlayerTwo("ROCK")
                    rpsGameManager.restartGame(isUsingMultiplayerMode)
                }
            }

            ivRightPaper.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.ic_result_background)
                    setImageResource(R.drawable.ic_paper_right_pressed)
                    rpsGameManager.movePlayerTwo("PAPER")
                    rpsGameManager.restartGame(isUsingMultiplayerMode)
                }
            }

            ivRightScissors.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.ic_result_background)
                    setImageResource(R.drawable.ic_scissor_right_pressed)
                    rpsGameManager.movePlayerTwo("SCISSORS")
                    rpsGameManager.restartGame(isUsingMultiplayerMode)
                }
            }

            ivRefresh.setOnClickListener {
                rpsGameManager.resetGame()
            }
        }
    }

    override fun onPlayerStatusChanged(player: Player) {
        setCharacterType(player)
    }

    override fun onResetImage() {
        binding.apply {
            ivLeftRock.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.ic_rock_left_idle)
            }
            ivLeftPaper.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.ic_paper_left_idle)
            }
            ivLeftScissors.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.ic_scissor_left_idle)
            }
            ivRightRock.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.ic_rock_right_idle)
            }
            ivRightPaper.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.ic_paper_right_idle)
            }
            ivRightScissors.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.ic_scissor_right_idle)
            }
        }
    }

    override fun onGameStateChanged(gameState: GameState) {
        binding.tvStatus.text = ""
        when (gameState) {
            GameState.IDLE -> {
                binding.ivRefresh.visibility = View.INVISIBLE
                binding.apply {
                    ivLeftRock.isEnabled = true
                    ivLeftPaper.isEnabled = true
                    ivLeftScissors.isEnabled = true
                    ivRightRock.isEnabled = true
                    ivRightPaper.isEnabled = true
                    ivRightScissors.isEnabled = true
                }
            }
            GameState.STARTED -> binding.ivRefresh.visibility = View.INVISIBLE
            GameState.FINISHED -> {
                binding.ivRefresh.visibility = View.VISIBLE
                setCharacterVisibility(isPlayerOneVisible = true, isPlayerTwoVisible = true)
                binding.apply {
                    ivLeftRock.isEnabled = false
                    ivLeftPaper.isEnabled = false
                    ivLeftScissors.isEnabled = false
                    ivRightRock.isEnabled = false
                    ivRightPaper.isEnabled = false
                    ivRightScissors.isEnabled = false
                }
            }
            GameState.PLAYER_ONE_TURN -> {
                binding.tvStatus.text = getString(R.string.text_lock_player_one)
                setCharacterVisibility(isPlayerOneVisible = true, isPlayerTwoVisible = false)
            }
            GameState.PLAYER_TWO_TURN -> {
                binding.tvStatus.text = getString(R.string.text_lock_player_two)
                setCharacterVisibility(isPlayerOneVisible = false, isPlayerTwoVisible = true)
            }
        }
    }

    private fun setCharacterVisibility(isPlayerOneVisible: Boolean, isPlayerTwoVisible: Boolean) {
        if (isPlayerOneVisible) {
            binding.llPlayerLeft.visibility = View.VISIBLE
        } else {
            binding.llPlayerLeft.visibility = View.INVISIBLE
        }
        if (isPlayerTwoVisible) {
            binding.llPlayerRight.visibility = View.VISIBLE
        } else {
            binding.llPlayerRight.visibility = View.INVISIBLE
        }
    }

    override fun onGameFinished(gameState: GameState, winner: Player) {
        when (winner.playerSide) {
            PlayerSide.DRAW -> {
                binding.tvStatus.text = getString(R.string.draw)
                CustomResultDialog("DRAW").apply {
                    setOnMenuSelectedListener(object : OnMenuSelectedListener {
                        override fun onTryAgain(dialog: DialogFragment) {
                            dialog.dismiss()
                            rpsGameManager.resetGame()
                        }

                        override fun onBackToMenu(dialog: DialogFragment) {
                            dialog.dismiss()
                            val intent = Intent(this@GameActivity, MenuGameActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra(EXTRAS_NAME, name)
                            startActivity(intent)
                            finish()
                        }
                    })
                }.show(supportFragmentManager, null)
            }
            PlayerSide.PLAYER_ONE -> {
                binding.tvStatus.text = getString(R.string.text_win, name)
                CustomResultDialog(name).apply {
                    setOnMenuSelectedListener(object : OnMenuSelectedListener {
                        override fun onTryAgain(dialog: DialogFragment) {
                            dialog.dismiss()
                            rpsGameManager.resetGame()
                        }

                        override fun onBackToMenu(dialog: DialogFragment) {
                            dialog.dismiss()
                            val intent = Intent(this@GameActivity, MenuGameActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra(EXTRAS_NAME, name)
                            startActivity(intent)
                            finish()
                        }
                    })
                }.show(supportFragmentManager, null)
            }
            else -> {
                CustomResultDialog(getString(R.string.your_friend_win)).apply {
                    setOnMenuSelectedListener(object : OnMenuSelectedListener {
                        override fun onTryAgain(dialog: DialogFragment) {
                            dialog.dismiss()
                            rpsGameManager.resetGame()
                        }

                        override fun onBackToMenu(dialog: DialogFragment) {
                            dialog.dismiss()
                            val intent = Intent(this@GameActivity, MenuGameActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra(EXTRAS_NAME, name)
                            startActivity(intent)
                            finish()
                        }
                    })
                }.show(supportFragmentManager, null)
                if (isUsingMultiplayerMode) {
                    binding.tvStatus.text = getString(R.string.your_friend_win)
                } else {
                    binding.tvStatus.text = getString(R.string.you_lose)
                }
            }
        }
    }

    private fun setCharacterType(player: Player) {
        val ivCharTop: ImageView?
        val ivCharMiddle: ImageView?
        val ivCharBottom: ImageView?

        ivCharTop = binding.ivRightRock
        ivCharMiddle = binding.ivRightPaper
        ivCharBottom = binding.ivRightScissors

        if (player.playerState == PlayerState.CHOOSED) {
            when (player.playerType) {
                PlayerType.ROCK -> {
                    ivCharTop.apply {
                        setBackgroundResource(R.drawable.ic_result_background)
                        setImageResource(R.drawable.ic_rock_right_pressed)
                    }
                }
                PlayerType.PAPER -> {
                    ivCharMiddle.apply {
                        setBackgroundResource(R.drawable.ic_result_background)
                        setImageResource(R.drawable.ic_paper_right_pressed)
                    }
                }
                PlayerType.SCISSORS -> {
                    ivCharBottom.apply {
                        setBackgroundResource(R.drawable.ic_result_background)
                        setImageResource(R.drawable.ic_scissor_right_pressed)
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRAS_MULTIPLAYER_MODE = "EXTRAS_MULTIPLAYER_MODE"
        private const val EXTRAS_NAME = "EXTRAS_NAME"

        fun startActivity(context: Context, isUsingMultiplayerMode: Boolean, name: String) {
            context.startActivity(Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRAS_MULTIPLAYER_MODE, isUsingMultiplayerMode)
                putExtra(EXTRAS_NAME, name)
            })
        }
    }
}