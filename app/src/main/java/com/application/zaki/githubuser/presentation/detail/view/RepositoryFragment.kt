package com.application.zaki.githubuser.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.application.zaki.githubuser.databinding.FragmentRepositoryBinding
import com.application.zaki.githubuser.presentation.adapter.LoadingStateAdapter
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.RepositoryPagingAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RepositoryFragment(private val username: String) :
    BaseVBFragment<FragmentRepositoryBinding>() {

    @Inject
    lateinit var repositoryPagingAdapter: RepositoryPagingAdapter
    private val viewModel by viewModels<DetailUserViewModel>()

    override fun getViewBinding(): FragmentRepositoryBinding =
        FragmentRepositoryBinding.inflate(layoutInflater)

    override fun initView() {
        setListRepositoriesUser()
        listener()
    }

    private fun listener() {
        binding?.btnTryAgain?.setOnClickListener {
            repositoryPagingAdapter.retry()
        }
    }

    private fun setListRepositoriesUser() {
        binding?.apply {
            rvRepository.adapter = repositoryPagingAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    repositoryPagingAdapter.retry()
                }
            )
            rvRepository.setHasFixedSize(true)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getRepositoriesUser(username)
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect { pagingData ->
                        repositoryPagingAdapter.submitData(lifecycle, pagingData)
                        repositoryPagingAdapter.addLoadStateListener { loadState ->
                            when (loadState.refresh) {
                                is LoadState.Loading -> {
                                    showShimmerLoading()
                                }
                                is LoadState.NotLoading -> {
                                    showDataRepository(loadState)
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

    private fun showShimmerLoading() {
        binding?.apply {
            shimmerPlaceholder.startShimmer()
            shimmerPlaceholder.visible()
            rvRepository.gone()
            emptyAnimation.gone()
            errorAnimation.gone()
            btnTryAgain.gone()
        }
    }

    private fun showDataRepository(loadState: CombinedLoadStates) {
        binding?.apply {
            if (loadState.append.endOfPaginationReached && repositoryPagingAdapter.itemCount == 0) {
                shimmerPlaceholder.gone()
                shimmerPlaceholder.stopShimmer()
                rvRepository.gone()
                emptyAnimation.visible()
                errorAnimation.gone()
                btnTryAgain.gone()
            } else {
                shimmerPlaceholder.gone()
                shimmerPlaceholder.stopShimmer()
                rvRepository.visible()
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
            rvRepository.gone()
            errorAnimation.visible()
            btnTryAgain.visible()
        }
    }

    override fun onDestroyView() {
        binding?.rvRepository?.adapter = null
        super.onDestroyView()
    }
}