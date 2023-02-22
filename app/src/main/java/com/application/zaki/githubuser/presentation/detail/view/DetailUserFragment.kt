package com.application.zaki.githubuser.presentation.detail.view

import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.zaki.githubuser.R
import com.application.zaki.githubuser.databinding.FragmentDetailUserBinding
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.ViewPagerAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.Status
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.loadImageUrl
import com.application.zaki.githubuser.utils.visible
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailUserFragment : BaseVBFragment<FragmentDetailUserBinding>() {
    private val args: DetailUserFragmentArgs by navArgs()
    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    override fun getViewBinding(): FragmentDetailUserBinding =
        FragmentDetailUserBinding.inflate(layoutInflater)

    override fun initView() {
        setDataUser()
        setViewPager()
        listener()
    }

    private fun listener() {
        binding?.imgArrowBack?.setOnClickListener {
            findNavController().navigate(R.id.action_detailUserFragment_to_homeFragment)
        }
    }

    private fun setDataUser() {
        val username = args.username
        viewLifecycleOwner.lifecycleScope.launch {
            detailUserViewModel.getDetailUser(username)
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it.status) {
                        Status.LOADING -> {
                            showShimmerLoading()
                        }
                        Status.SUCCESS -> {
                            showUserData(it.data)
                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }
    }

    private fun showShimmerLoading() {
        binding?.apply {
            layoutDetailInformationNotShimmerPlaceholder.gone()
            layoutDetailInformationShimmerPlaceholder.visible()
            layoutDetailInformationShimmerPlaceholder.startShimmer()
        }
    }

    private fun showUserData(data: DetailUser?) {
        binding?.apply {
            layoutDetailInformationNotShimmerPlaceholder.visible()
            layoutDetailInformationShimmerPlaceholder.gone()
            layoutDetailInformationShimmerPlaceholder.stopShimmer()
            tvUsernameUser.text = data?.login
            imgGithubLogo.setOnClickListener {
                startActivity(
                    Intent.parseUri(
                        data?.htmlUrl,
                        Intent.URI_INTENT_SCHEME
                    )
                )
            }
            imgProfileUser.loadImageUrl(data?.avatarUrl)
            tvBodyFollowers.text = data?.followers.toString()
            tvBodyFollowing.text = data?.following.toString()
            tvBodyRepository.text = data?.publicRepos.toString()
            tvNameUser.text = data?.name
            tvBodyBio.text = data?.bio ?: resources.getString(R.string.dash)
            tvBodyCompany.text =
                data?.company ?: resources.getString(R.string.dash)
        }
    }

    private fun setViewPager() {
        val viewPager = binding?.viewPager
        val tabLayout = binding?.tabLayout
        val adapter = ViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        adapter.addFragment(RepositoryFragment(args.username))
        adapter.addFragment(FollowersFragment(args.username))
        adapter.addFragment(FollowingFragment(args.username))
        viewPager?.adapter = adapter
        if (tabLayout != null && viewPager != null) {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onDestroyView() {
        binding?.viewPager?.adapter = null
        super.onDestroyView()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.repository,
            R.string.followers,
            R.string.following
        )
    }
}