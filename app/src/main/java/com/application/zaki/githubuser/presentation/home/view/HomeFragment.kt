package com.application.zaki.githubuser.presentation.home.view

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.application.zaki.githubuser.databinding.FragmentHomeBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.home.adapter.HomePagingAdapter
import com.application.zaki.githubuser.presentation.home.viewmodel.HomeViewModel
import com.application.zaki.githubuser.utils.NetworkResult
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
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
                viewModel.getListUser()
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .distinctUntilChanged()
                    .collect {
                        when (it) {
                            is NetworkResult.Loading -> {
                                rvUsers.gone()
                                shimmerPlaceholder.visible()
                                shimmerPlaceholder.startShimmer()
                            }
                            is NetworkResult.Success -> {
                                shimmerPlaceholder.gone()
                                shimmerPlaceholder.stopShimmer()
                                rvUsers.visible()
                                homePagingAdapter.submitData(lifecycle, it.data)
                            }
                            is NetworkResult.Error -> {}
                            is NetworkResult.Empty -> {}
                        }
                    }
            }
        }
        homePagingAdapter.setOnItemClickCallback(object : HomePagingAdapter.OnItemCliCkCallback {
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
                        viewModel.querySearch.value = newText
                        rvUsers.gone()
                        shimmerPlaceholder.visible()
                        shimmerPlaceholder.startShimmer()
                    }
                    return true
                }
            })

            lifecycleScope.launchWhenStarted {
                viewModel.searchResult
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        when (it) {
                            is NetworkResult.Loading -> {
                                rvUsers.gone()
                                shimmerPlaceholder.visible()
                                shimmerPlaceholder.startShimmer()
                            }
                            is NetworkResult.Success -> {
                                homePagingAdapter.submitData(lifecycle, it.data)
                                shimmerPlaceholder.gone()
                                shimmerPlaceholder.stopShimmer()
                                rvUsers.visible()
                            }
                            is NetworkResult.Error -> {}
                            is NetworkResult.Empty -> {}
                        }
                    }
            }
        }
    }
}