package com.application.zaki.githubuser.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.application.zaki.githubuser.databinding.FragmentFollowersBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.presentation.adapter.LoadingStateAdapter
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.DetailPagingAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        listener()
    }

    private fun listener() {
        detailPagingAdapter.setOnItemClickCallback(object :
            DetailPagingAdapter.IOnItemClickCallback {
            override fun onItemClicked(item: ListUsers?) {
                navigateToDetailPage(item)
            }
        })
        binding?.btnTryAgain?.setOnClickListener {
            detailPagingAdapter.retry()
        }
    }

    private fun setListFollowers() {
        binding?.apply {
            rvUsersFollowers.adapter = detailPagingAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    detailPagingAdapter.retry()
                }
            )
            rvUsersFollowers.setHasFixedSize(true)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getFollowersUser(username)
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect { pagingData ->
                        detailPagingAdapter.submitData(lifecycle, pagingData)
                        detailPagingAdapter.addLoadStateListener { loadState ->
                            when (loadState.refresh) {
                                is LoadState.Loading -> {
                                    showShimmerLoading()
                                }
                                is LoadState.NotLoading -> {
                                    showDataFollowers(loadState)
                                }
                                is LoadState.Error -> {
                                    showErrorAnimation()
                                }
                            }
                        }
                    }
            }
        }
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
            shimmerPlaceholder.visible()
            shimmerPlaceholder.startShimmer()
            rvUsersFollowers.gone()
            emptyAnimation.gone()
            errorAnimation.gone()
            btnTryAgain.gone()
        }
    }

    private fun showDataFollowers(loadState: CombinedLoadStates) {
        binding?.apply {
            if (loadState.append.endOfPaginationReached && detailPagingAdapter.itemCount == 0) {
                shimmerPlaceholder.gone()
                shimmerPlaceholder.stopShimmer()
                rvUsersFollowers.gone()
                emptyAnimation.visible()
                errorAnimation.gone()
                btnTryAgain.gone()
            } else {
                shimmerPlaceholder.gone()
                shimmerPlaceholder.stopShimmer()
                rvUsersFollowers.visible()
                emptyAnimation.gone()
                errorAnimation.gone()
                btnTryAgain.gone()
            }
        }
    }

    private fun showErrorAnimation() {
        binding?.apply {
            shimmerPlaceholder.gone()
            shimmerPlaceholder.stopShimmer()
            rvUsersFollowers.gone()
            errorAnimation.visible()
            btnTryAgain.visible()
        }
    }

    override fun onDestroyView() {
        binding?.rvUsersFollowers?.adapter = null
        super.onDestroyView()
    }
}