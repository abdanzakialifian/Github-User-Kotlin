package com.application.zaki.githubuser.presentation.home.view

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.application.zaki.githubuser.databinding.FragmentHomeBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.home.adapter.HomePagingAdapter
import com.application.zaki.githubuser.presentation.home.viewmodel.HomeViewModel
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseVBFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var homePagingAdapter: HomePagingAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun initView() {
        setListUser()
        listener()
    }

    private fun listener() {
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null || newText == "") {
                    setListUser()
                } else {
                    // set delay search user so as not be rate limit api
                    Handler(Looper.getMainLooper()).postDelayed({
                        searchUser(newText)
                    }, 600)
                }
                return true
            }
        })
        binding?.rvUsers?.adapter = homePagingAdapter
        homePagingAdapter.setOnItemClickCallback(object : HomePagingAdapter.IOnItemCliCkCallback {
            override fun onItemClicked(item: ListUsers?) {
                val actionToDetailUserFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailUserFragment(item?.login ?: "")
                findNavController().navigate(actionToDetailUserFragment)
            }
        })
    }

    private fun setListUser() {
        binding?.apply {
            lifecycleScope.launchWhenStarted {
                viewModel.getListUsers()
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { pagingData ->
                        homePagingAdapter.submitData(lifecycle, pagingData)
                        homePagingAdapter.addLoadStateListener { loadState ->
                            when (loadState.refresh) {
                                is LoadState.Loading -> {
                                    rvUsers.gone()
                                    shimmerPlaceholder.visible()
                                    shimmerPlaceholder.startShimmer()
                                }
                                is LoadState.NotLoading -> {
                                    shimmerPlaceholder.gone()
                                    shimmerPlaceholder.stopShimmer()
                                    rvUsers.visible()
                                }
                                is LoadState.Error -> {
                                    rvUsers.gone()
                                    shimmerPlaceholder.gone()
                                    shimmerPlaceholder.stopShimmer()
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

    private fun searchUser(value: String) {
        binding?.apply {
            lifecycleScope.launchWhenStarted {
                viewModel.searchUser(value)
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { pagingData ->
                        homePagingAdapter.submitData(lifecycle, pagingData)
                        homePagingAdapter.addLoadStateListener { loadState ->
                            when (loadState.refresh) {
                                is LoadState.Loading -> {
                                    rvUsers.gone()
                                    shimmerPlaceholder.visible()
                                    shimmerPlaceholder.startShimmer()
                                }
                                is LoadState.NotLoading -> {
                                    shimmerPlaceholder.gone()
                                    shimmerPlaceholder.stopShimmer()
                                    rvUsers.visible()
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