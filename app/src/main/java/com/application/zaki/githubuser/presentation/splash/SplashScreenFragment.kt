package com.application.zaki.githubuser.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.application.zaki.githubuser.R
import com.application.zaki.githubuser.databinding.FragmentSplashScreenBinding
import com.application.zaki.githubuser.presentation.base.BaseVBFragment

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseVBFragment<FragmentSplashScreenBinding>() {
    override fun getViewBinding(): FragmentSplashScreenBinding =
        FragmentSplashScreenBinding.inflate(layoutInflater)

    override fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            val navigateToHomeFragment =
                SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
            findNavController().navigate(navigateToHomeFragment)
        }, DELAY_SPLASH)
    }

    companion object {
        private const val DELAY_SPLASH = 3000L
    }
}