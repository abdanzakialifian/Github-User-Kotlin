package com.application.zaki.githubuser.presentation.detail.view

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.application.zaki.githubuser.R
import com.application.zaki.githubuser.databinding.FragmentDetailUserBinding
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.ViewPagerAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.NetworkResult
import com.application.zaki.githubuser.utils.loadImageUrl
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailUserFragment : BaseVBFragment<FragmentDetailUserBinding>() {
    private val args: DetailUserFragmentArgs by navArgs()
    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    override fun getViewBinding(): FragmentDetailUserBinding =
        FragmentDetailUserBinding.inflate(layoutInflater)

    override fun initView() {
        setDataUser()
        setViewPager()
    }

    private fun setDataUser() {
        val username = args.username
        lifecycleScope.launchWhenStarted {
            detailUserViewModel.getDetailUser(username)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is NetworkResult.Loading -> {
                            binding?.apply {
                            }
                        }
                        is NetworkResult.Success -> {
                            binding?.apply {
                                val data = it.data
                                tvUsernameUser.text = data.login
                                imgGithubLogo.setOnClickListener {
                                    Intent.parseUri(data.htmlUrl, Intent.URI_INTENT_SCHEME).run {
                                        startActivity(this)
                                    }
                                }
                                imgProfileUser.loadImageUrl(data.avatarUrl)
                                tvBodyFollowers.text = data.followers.toString()
                                tvBodyFollowing.text = data.following.toString()
                                tvBodyRepository.text = data.publicRepos.toString()
                                tvNameUser.text = data.name
                                tvBodyBio.text = data.bio ?: resources.getString(R.string.dash)
                                tvBodyCompany.text =
                                    data.company ?: resources.getString(R.string.dash)
                            }
                        }
                        is NetworkResult.Error -> {
                            binding?.apply {
                            }
                        }
                        is NetworkResult.Empty -> {
                            binding?.apply {
                            }
                        }
                    }
                }
        }
    }

    private fun setViewPager() {
        val tabLayoutTitle = arrayOf(
            resources.getString(R.string.followers),
            resources.getString(R.string.following),
            resources.getString(R.string.repository)
        )

        val viewPager = binding?.viewPager
        val tabLayout = binding?.tabLayout
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager?.adapter = adapter
        if (tabLayout != null && viewPager != null) {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabLayoutTitle[position]
            }.attach()
        }
    }
}