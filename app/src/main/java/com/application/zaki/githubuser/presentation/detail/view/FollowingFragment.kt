package com.application.zaki.githubuser.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.application.zaki.githubuser.databinding.FragmentFollowingBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.presentation.adapter.LoadingStateAdapter
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.DetailPagingAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
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
            rvUsersFollowing.adapter = detailPagingAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    detailPagingAdapter.retry()
                }
            )
            rvUsersFollowing.setHasFixedSize(true)
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getFollowingUser(username)
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect { pagingData ->
                        detailPagingAdapter.submitData(lifecycle, pagingData)
                        detailPagingAdapter.addLoadStateListener { loadState ->
                            when (loadState.refresh) {
                                is LoadState.Loading -> {
                                    showShimmerLoading()
                                }
                                is LoadState.NotLoading -> {
                                    showDataFollowing(loadState)
                                }
                                is LoadState.Error -> {
                                    showErrorAnimation()
                                }
                            }
                        }
                    }
            }
        }
        detailPagingAdapter.setOnItemClickCallback(object :
            DetailPagingAdapter.IOnItemClickCallback {
            override fun onItemClicked(item: ListUsers?) {
                navigateToDetailPage(item)
            }
        })
    }

    private fun navigateToDetailPage(item: ListUsers?) {
        val actionToDetailUserFragment =
            DetailUserFragmentDirections.actionDetailUserFragmentToDetailUserFragment(
                item?.login ?: ""
            )
        findNavController().navigate(actionToDetailUserFragment)
    }

    private fun showShimmerLoading() {
        binding?.apply {
            shimmerPlaceholder.startShimmer()
            shimmerPlaceholder.visible()
            rvUsersFollowing.gone()
            emptyAnimation.gone()
            errorAnimation.gone()
        }
    }

    private fun showDataFollowing(loadState: CombinedLoadStates) {
        binding?.apply {
            if (loadState.append.endOfPaginationReached && detailPagingAdapter.itemCount == 0) {
                shimmerPlaceholder.gone()
                shimmerPlaceholder.stopShimmer()
                rvUsersFollowing.gone()
                emptyAnimation.visible()
                errorAnimation.gone()
            } else {
                shimmerPlaceholder.gone()
                shimmerPlaceholder.stopShimmer()
                rvUsersFollowing.visible()
                emptyAnimation.gone()
                errorAnimation.gone()
            }
        }
    }

    private fun showErrorAnimation() {
        binding?.apply {
            shimmerPlaceholder.gone()
            shimmerPlaceholder.stopShimmer()
            rvUsersFollowing.gone()
            errorAnimation.visible()
        }
    }

    override fun onDestroyView() {
        binding?.rvUsersFollowing?.adapter = null
        super.onDestroyView()
    }
}