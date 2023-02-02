package com.application.zaki.githubuser.presentation.detail.view

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.application.zaki.githubuser.databinding.FragmentRepositoryBinding
import com.application.zaki.githubuser.presentation.adapter.LoadingStateAdapter
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.RepositoryPagingAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
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
    }

    private fun setListRepositoriesUser() {
        binding?.apply {
            rvRepository.adapter = repositoryPagingAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    repositoryPagingAdapter.retry()
                }
            )
            rvRepository.setHasFixedSize(true)
            lifecycleScope.launchWhenStarted {
                viewModel.getRepositoriesUser(username)
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { pagingData ->
                        repositoryPagingAdapter.submitData(lifecycle, pagingData)
                        repositoryPagingAdapter.addLoadStateListener { loadState ->
                            when (loadState.refresh) {
                                is LoadState.Loading -> {
                                    shimmerPlaceholder.startShimmer()
                                    shimmerPlaceholder.visible()
                                    rvRepository.gone()
                                }
                                is LoadState.NotLoading -> {
                                    shimmerPlaceholder.stopShimmer()
                                    shimmerPlaceholder.gone()
                                    rvRepository.visible()
                                }
                                is LoadState.Error -> {
                                    val castError = loadState.refresh as LoadState.Error
                                    Toast.makeText(
                                        requireContext(),
                                        "Error ${castError.error.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
            }
        }
    }
}