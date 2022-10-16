package com.binar.binarch4.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.binar.binarch4.R
import com.binar.binarch4.databinding.DialogCustomMenuBinding

class CustomResultDialog(name: String?) : DialogFragment() {

    private lateinit var binding: DialogCustomMenuBinding

    private var listener: OnMenuSelectedListener? = null

    fun setOnMenuSelectedListener(listener: OnMenuSelectedListener) {
        this.listener = listener
    }

    private val winName = name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DialogCustomMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.apply {
            Log.d("Testing4", "onViewCreated: $winName")
            when (winName) {
                "DRAW" -> {
                    tvWin.text = getString(R.string.draw_win)
                }
                getString(R.string.your_friend_win) -> {
                    tvWin.text = winName
                }
                else -> {
                    tvWin.text = getString(R.string.text_win, winName)
                }
            }
            btnTryAgain.setOnClickListener {
                listener?.onTryAgain(this@CustomResultDialog)
            }
            btnBackMenu.setOnClickListener {
                listener?.onBackToMenu(this@CustomResultDialog)
            }
        }
    }
}

interface OnMenuSelectedListener {
    fun onTryAgain(dialog: DialogFragment)
    fun onBackToMenu(dialog: DialogFragment)
}