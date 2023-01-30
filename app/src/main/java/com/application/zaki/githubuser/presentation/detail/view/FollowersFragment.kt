package com.application.zaki.githubuser.presentation.detail.view

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.application.zaki.githubuser.databinding.FragmentFollowersBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.detail.adapter.DetailPagingAdapter
import com.application.zaki.githubuser.presentation.detail.viewmodel.DetailUserViewModel
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
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { pagingData ->
                        detailPagingAdapter.submitData(lifecycle, pagingData)
                        detailPagingAdapter.addLoadStateListener { loadState ->
                            when (loadState.refresh) {
                                is LoadState.Loading -> {
                                    shimmerPlaceholder.visible()
                                    shimmerPlaceholder.startShimmer()
                                    rvUsersFollowers.gone()
                                }
                                is LoadState.NotLoading -> {
                                    shimmerPlaceholder.gone()
                                    shimmerPlaceholder.stopShimmer()
                                    rvUsersFollowers.visible()
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

        detailPagingAdapter.setOnItemClickCallback(object :
            DetailPagingAdapter.IOnItemClickCallback {
            override fun onItemClicked(item: ListUsers?) {
                val actionToDetailUserFragment =
                    DetailUserFragmentDirections.actionDetailUserFragmentToDetailUserFragment(
                        item?.login ?: ""
                    )
                findNavController().navigate(actionToDetailUserFragment)
            }
        })
    }
}