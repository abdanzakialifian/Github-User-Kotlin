package com.application.zaki.githubuser.presentation.home.view

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import com.application.zaki.githubuser.databinding.FragmentHomeBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.home.adapter.HomePagingAdapter
import com.application.zaki.githubuser.presentation.home.viewmodel.HomeViewModel
import com.application.zaki.githubuser.utils.Status
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
        searchUser()
    }

    private fun setListUser() {
        binding?.apply {
            rvUsers.adapter = homePagingAdapter
            rvUsers.setHasFixedSize(true)
            lifecycleScope.launchWhenStarted {
                viewModel.listUsers
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        when (it.status) {
                            Status.LOADING -> {
                                rvUsers.gone()
                                shimmerPlaceholder.visible()
                                shimmerPlaceholder.startShimmer()
                            }
                            Status.SUCCESS -> {
                                shimmerPlaceholder.gone()
                                shimmerPlaceholder.stopShimmer()
                                rvUsers.visible()
                                it.data?.let { pagingData ->
                                    homePagingAdapter.submitData(lifecycle, pagingData)
                                }
                            }
                            Status.ERROR -> {}
                        }
                    }
            }
        }
        homePagingAdapter.setOnItemClickCallback(object : HomePagingAdapter.IOnItemCliCkCallback {
            override fun onItemClicked(item: ListUsers?) {
                val actionToDetailUserFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailUserFragment(item?.login ?: "")
                findNavController().navigate(actionToDetailUserFragment)
            }
        })
    }

    private fun searchUser() {
        binding?.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText == null || newText == "") {
                        setListUser()
                    } else {
                        // set delay search user so as not be rate limit api
                        Handler(Looper.getMainLooper()).postDelayed({
                            viewModel.searchUser(newText)
                        }, 600)
                        rvUsers.gone()
                        shimmerPlaceholder.visible()
                        shimmerPlaceholder.startShimmer()
                    }
                    return true
                }
            })

            lifecycleScope.launchWhenStarted {
                viewModel.users
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        when (it.status) {
                            Status.LOADING -> {
                                rvUsers.gone()
                                shimmerPlaceholder.visible()
                                shimmerPlaceholder.startShimmer()
                            }
                            Status.SUCCESS -> {
                                it.data?.let { pagingData ->
                                    homePagingAdapter.submitData(lifecycle, pagingData)
                                }
                                shimmerPlaceholder.gone()
                                shimmerPlaceholder.stopShimmer()
                                rvUsers.visible()
                            }
                            Status.ERROR -> {}
                        }
                    }
            }
        }
    }
}