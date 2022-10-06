package com.application.zaki.githubuser.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.application.zaki.githubuser.databinding.FragmentFollowingBinding
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.DetailPagingAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.NetworkResult
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class FollowingFragment(private val username: String) : BaseVBFragment<FragmentFollowingBinding>() {

    @Inject
    lateinit var detailPagingAdapter: DetailPagingAdapter
    private val viewModel by viewModels<DetailUserViewModel>()

    override fun getViewBinding(): FragmentFollowingBinding =
        FragmentFollowingBinding.inflate(layoutInflater)

    override fun initView() {
        setListFollowing()
    }

    private fun setListFollowing() {
        binding?.apply {
            rvUsersFollowing.adapter = detailPagingAdapter
            rvUsersFollowing.setHasFixedSize(true)
            lifecycleScope.launchWhenStarted {
                viewModel.getFollowingUser(username)
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .distinctUntilChanged()
                    .collect {
                        when (it) {
                            is NetworkResult.Loading -> {
                                binding?.apply {
                                    shimmerPlaceholder.startShimmer()
                                    shimmerPlaceholder.visible()
                                    rvUsersFollowing.gone()
                                }
                            }
                            is NetworkResult.Success -> {
                                binding?.apply {
                                    shimmerPlaceholder.stopShimmer()
                                    shimmerPlaceholder.gone()
                                    rvUsersFollowing.visible()
                                }
                                detailPagingAdapter.submitData(lifecycle, it.data)
                            }
                            is NetworkResult.Error -> {}
                            is NetworkResult.Empty -> {}
                        }
                    }
            }
        }
    }
}