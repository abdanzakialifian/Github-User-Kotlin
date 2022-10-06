package com.application.zaki.githubuser.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.application.zaki.githubuser.databinding.FragmentRepositoryBinding
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.RepositoryPagingAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
import com.application.zaki.githubuser.utils.NetworkResult
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
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
            rvRepository.adapter = repositoryPagingAdapter
            rvRepository.setHasFixedSize(true)
            lifecycleScope.launchWhenStarted {
                viewModel.getRepositoriesUser(username)
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .distinctUntilChanged()
                    .collect {
                        when (it) {
                            is NetworkResult.Loading -> {
                                binding?.apply {
                                    shimmerPlaceholder.startShimmer()
                                    shimmerPlaceholder.visible()
                                    rvRepository.gone()
                                }
                            }
                            is NetworkResult.Success -> {
                                binding?.apply {
                                    shimmerPlaceholder.stopShimmer()
                                    shimmerPlaceholder.gone()
                                    rvRepository.visible()
                                }
                                repositoryPagingAdapter.submitData(lifecycle, it.data)
                            }
                            is NetworkResult.Error -> {}
                            is NetworkResult.Empty -> {}
                        }
                    }
            }
        }
    }
}