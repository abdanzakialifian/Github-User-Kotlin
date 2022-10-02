package com.application.zaki.githubuser.presentation.detail.view

import com.application.zaki.githubuser.databinding.FragmentFollowingBinding
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : BaseVBFragment<FragmentFollowingBinding>() {
    override fun getViewBinding(): FragmentFollowingBinding =
        FragmentFollowingBinding.inflate(layoutInflater)

    override fun initView() {}
}