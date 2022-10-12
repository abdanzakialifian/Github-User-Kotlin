package com.application.zaki.githubuser.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.application.zaki.githubuser.databinding.FragmentFollowersBinding
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.DetailPagingAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.Status
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FollowersFragment(private val username: String) : BaseVBFragment<FragmentFollowersBinding>() {

    @Inject
    lateinit var detailPagingAdapter: DetailPagingAdapter
    private val viewModel by viewModels<DetailUserViewModel>()

    override fun getViewBinding(): FragmentFollowersBinding =
        FragmentFollowersBinding.inflate(layoutInflater)

    override fun initView() {
        setListFollowers()
    }

    private fun setListFollowers() {
        binding?.apply {
            rvUsersFollowers.adapter = detailPagingAdapter
            rvUsersFollowers.setHasFixedSize(true)
            lifecycleScope.launchWhenStarted {
                viewModel.getFollowersUser(username)
                viewModel.listFollowers
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        when (it.status) {
                            Status.LOADING -> {
                                binding?.apply {
                                    shimmerPlaceholder.visible()
                                    shimmerPlaceholder.startShimmer()
                                    rvUsersFollowers.gone()
                                }
                            }
                            Status.SUCCESS -> {
                                binding?.apply {
                                    shimmerPlaceholder.gone()
                                    shimmerPlaceholder.stopShimmer()
                                    rvUsersFollowers.visible()
                                }
                                it.data?.let { pagingData ->
                                    detailPagingAdapter.submitData(lifecycle, pagingData)
                                }
                            }
                            Status.ERROR -> {}
                        }
                    }
            }
        }
    }
}