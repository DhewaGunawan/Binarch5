package com.binar.binarch4.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.viewpager2.widget.ViewPager2
import com.binar.binarch4.databinding.ActivityOnboardingBinding
import com.binar.binarch4.form.FormFragment
import com.binar.binarch4.form.OnNameSubmittedListener
import com.binar.binarch4.model.SliderData
import com.binar.binarch4.slider.SliderFragment
import com.binar.binarch4.utils.ViewPagerAdapter
import com.binar.binarch4.utils.getNextIndex
import com.binar.binarch4.utils.getPreviousIndex

class OnBoardingActivity : AppCompatActivity(), OnNameSubmittedListener {
    private val binding: ActivityOnboardingBinding by lazy {
        ActivityOnboardingBinding.inflate(layoutInflater)
    }

    private val pagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(supportFragmentManager, lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        initFragmentViewPager()
        setOnClickListeners()
    }

    private fun initAdapter() {
        pagerAdapter.apply {
            addFragment(
                SliderFragment.newInstance(
                    SliderData(
                        title = "FUN WITH YOUR FRIEND",
                        desc = "You can play with friend",
                        imgSlider = "vs_player"
                    )
                )
            )
            addFragment(
                SliderFragment.newInstance(
                    SliderData(
                        title = "FUN WITH YOURSELF",
                        desc = "You can play with computer",
                        imgSlider = "vs_bot"
                    )
                )
            )
            addFragment(FormFragment.newInstance().apply {
                setNameSubmittedListener(this@OnBoardingActivity)
            })
        }
    }

    private fun setupViewPager() {
        binding.vpIntro.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when {
                        position == 0 -> {
                            binding.tvNext.isInvisible = false
                            binding.tvNext.isEnabled = true
                            binding.tvPrevious.isInvisible = true
                            binding.tvPrevious.isEnabled = false
                        }
                        position < pagerAdapter.getMaxIndex() -> {
                            binding.tvNext.isInvisible = false
                            binding.tvNext.isEnabled = true
                            binding.tvPrevious.isInvisible = false
                            binding.tvPrevious.isEnabled = true
                        }
                        position == pagerAdapter.getMaxIndex() -> {
                            binding.tvNext.isInvisible = true
                            binding.tvNext.isEnabled = false
                            binding.tvPrevious.isInvisible = false
                            binding.tvPrevious.isEnabled = true
                        }
                    }
                }
            })
        }
        binding.dotsIndicator.attachTo(binding.vpIntro)
    }

    private fun initFragmentViewPager() {
        initAdapter()
        setupViewPager()
    }

    private fun setOnClickListeners() {
        binding.tvNext.setOnClickListener {
            navigateToNextFragment()
        }
        binding.tvPrevious.setOnClickListener {
            navigateToPreviousFragment()
        }
    }

    private fun navigateToPreviousFragment() {
        val nextIndex = binding.vpIntro.getPreviousIndex()
        if (nextIndex != -1) {
            binding.vpIntro.setCurrentItem(nextIndex, true)
        }
    }

    private fun navigateToNextFragment() {
        val nextIndex = binding.vpIntro.getNextIndex()
        if (nextIndex != -1) {
            binding.vpIntro.setCurrentItem(nextIndex, true)
        }
    }

    override fun onNameSubmitted(name: String) {
        MenuGameActivity.startActivity(this, name)
    }
}