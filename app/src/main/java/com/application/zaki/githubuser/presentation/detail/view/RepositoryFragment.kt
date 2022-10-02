package com.application.zaki.githubuser.presentation.detail.view

import com.application.zaki.githubuser.databinding.FragmentRepositoryBinding
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoryFragment : BaseVBFragment<FragmentRepositoryBinding>() {
    override fun getViewBinding(): FragmentRepositoryBinding =
        FragmentRepositoryBinding.inflate(layoutInflater)

    override fun initView() {}
}