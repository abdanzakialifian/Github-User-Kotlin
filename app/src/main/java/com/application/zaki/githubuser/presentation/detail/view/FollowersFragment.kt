package com.application.zaki.githubuser.presentation.detail.view

import com.application.zaki.githubuser.databinding.FragmentFollowersBinding
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : BaseVBFragment<FragmentFollowersBinding>() {
    override fun getViewBinding(): FragmentFollowersBinding =
        FragmentFollowersBinding.inflate(layoutInflater)

    override fun initView() {}
}