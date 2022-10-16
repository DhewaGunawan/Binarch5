package com.binar.binarch4.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.binar.binarch4.R
import com.binar.binarch4.databinding.ActivityMenuBinding
import com.binar.binarch4.game.GameActivity

class MenuGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    private val name: String? by lazy {
        intent.getStringExtra(EXTRAS_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setNameOnTitle()
        setMenuClickListeners()
    }

    private fun setMenuClickListeners() {
        binding.ivMenuVsComputer.setOnClickListener {
            name?.let { it1 -> GameActivity.startActivity(this, false, it1) }
        }
        binding.ivMenuVsPlayer.setOnClickListener {
            name?.let { it1 -> GameActivity.startActivity(this, true, it1) }
        }
    }

    private fun setNameOnTitle() {
        binding.tvTitleMenu.text = getString(R.string.placeholder_title_menu_game, name)
    }


    companion object {
        private const val EXTRAS_NAME = "EXTRAS_NAME"

        fun startActivity(context: Context, name: String) {
            context.startActivity(Intent(context, MenuGameActivity::class.java).apply {
                putExtra(EXTRAS_NAME, name)
            })
        }
    }
}