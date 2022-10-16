package com.binar.binarch4.slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.binar.binarch4.databinding.FragmentSliderBinding
import com.binar.binarch4.model.SliderData

class SliderFragment : Fragment() {

    private lateinit var binding: FragmentSliderBinding

    private var sliderData: SliderData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSliderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindSliderData()
    }

    private fun bindSliderData() {
        with(binding) {
            tvTitle.text = sliderData?.title.orEmpty()
            tvDesc.text = sliderData?.desc.orEmpty()
//            ivIntro.load(sliderData?.imgSlider) {
//                crossfade(true)
//            }
            if (sliderData?.imgSlider == "vs_player") {
                includeBoardingImage.apply {
                    ivVersusPlayer1.isVisible = true
                    ivVersusPlayer2.isVisible = true
                    tvVs.isVisible = true
                }
            } else {
                includeBoardingImage.apply {
                    ivVersusComp.isVisible = true
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sliderData = it.getParcelable(ARG_SLIDER_DATA)
        }
    }

    companion object {
        private const val ARG_SLIDER_DATA = "ARG_SLIDER_DATA"

        @JvmStatic
        fun newInstance(sliderData: SliderData) =
            SliderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SLIDER_DATA, sliderData)
                }
            }
    }
}