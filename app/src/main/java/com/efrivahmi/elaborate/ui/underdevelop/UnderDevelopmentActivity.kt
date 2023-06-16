package com.efrivahmi.elaborate.ui.underdevelop

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.efrivahmi.elaborate.databinding.ActivityUnderDevelopmentBinding

class UnderDevelopmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnderDevelopmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnderDevelopmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.tvLogoElaborate, View.TRANSLATION_Y, -45f, 45f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val under1 = ObjectAnimator.ofFloat(binding.tvUnderDevelopment, View.ALPHA, 1f).setDuration(2000)
        val under2 = ObjectAnimator.ofFloat(binding.tvUnderDevelopment2, View.ALPHA, 1f).setDuration(2000)

        val together = AnimatorSet().apply {
            playTogether(under1, under2)
        }

        AnimatorSet().apply {
            playSequentially(together)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}