package com.application.zaki.githubuser.presentation.home.view

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.application.zaki.githubuser.R
import com.application.zaki.githubuser.databinding.FragmentHomeBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.User
import com.application.zaki.githubuser.presentation.adapter.LoadingStateAdapter
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.home.adapter.HomePagingAdapter
import com.application.zaki.githubuser.presentation.home.viewmodel.HomeViewModel
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        binding?.imgFavorite?.setOnClickListener {
            navigateToFavoritePage()
        }
        binding?.searchView?.let {
            lifecycleScope.launchWhenStarted {
                viewModel.searchFlow(it)
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        if (it.isEmpty()) {
                            setListUser()
                        } else {
                            searchUser(it)
                        }
                    }
            }
        }
        binding?.rvUsers?.adapter = homePagingAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                homePagingAdapter.retry()
            }
        )
        homePagingAdapter.setOnItemClickCallback(object : HomePagingAdapter.IOnItemCliCkCallback {
            override fun onItemClicked(item: ListUsers?) {
                navigateToDetailPage(item?.login ?: "")
            }

            override fun onFavoriteClicked(item: ListUsers?, imageView: ImageView) {
                handleFavoriteButton(item, imageView)
            }

            override fun onUserId(id: Int, imageView: ImageView) {
                handleFavoriteButtonState(id, imageView)
            }
        }
        )
    }

    private fun favoriteUser(imgFavorite: ImageView, isFavorite: Boolean = false) {
        if (isFavorite) {
            imgFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_favorite_filled_red_24
                )
            )
        } else {
            imgFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_favorite_border_grey_24
                )
            )
        }
    }

    private fun setListUser() {
        binding?.apply {
            lifecycleScope.launchWhenStarted {
                viewModel.getListUsers
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { pagingData ->
                        homePagingAdapter.submitData(lifecycle, pagingData)
                        homePagingAdapter.addLoadStateListener { loadState ->
                            when (loadState.refresh) {
                                is LoadState.Loading -> {
                                    showShimmerLoading()
                                }
                                is LoadState.NotLoading -> {
                                    if (loadState.append.endOfPaginationReached && homePagingAdapter.itemCount == 0) {
                                        showEmptyAnimation()
                                    } else {
                                        showListUser()
                                    }
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
                                    showShimmerLoading()
                                }
                                is LoadState.NotLoading -> {
                                    if (loadState.append.endOfPaginationReached && homePagingAdapter.itemCount == 0) {
                                        showEmptyAnimation()
                                    } else {
                                        showListUser()
                                    }
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

    private fun handleFavoriteButton(item: ListUsers?, imageView: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            val isFavorite = viewModel.getUserById(item?.id ?: 0)
            val user = User(id = item?.id, image = item?.avatarUrl, username = item?.login)
            if (isFavorite) {
                viewModel.deleteUser(user)
                favoriteUser(imageView, false)
            } else {
                viewModel.addUser(user)
                favoriteUser(imageView, true)
            }
        }
    }

    private fun handleFavoriteButtonState(id: Int, imageView: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            val isFavorite = viewModel.getUserById(id)
            if (isFavorite) {
                favoriteUser(imageView, true)
            } else {
                favoriteUser(imageView, false)
            }
        }
    }

    private fun navigateToFavoritePage() {
        val navigateToFavoriteFragment =
            HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
        findNavController().navigate(navigateToFavoriteFragment)
    }

    private fun navigateToDetailPage(username: String) {
        val actionToDetailUserFragment =
            HomeFragmentDirections.actionHomeFragmentToDetailUserFragment(username)
        findNavController().navigate(actionToDetailUserFragment)
    }

    private fun showShimmerLoading() {
        binding?.apply {
            rvUsers.gone()
            shimmerPlaceholder.visible()
            shimmerPlaceholder.startShimmer()
            emptyAnimation.gone()
            errorAnimation.gone()
        }
    }

    private fun showEmptyAnimation() {
        binding?.apply {
            shimmerPlaceholder.gone()
            shimmerPlaceholder.stopShimmer()
            rvUsers.gone()
            emptyAnimation.visible()
            errorAnimation.gone()
        }
    }

    private fun showErrorAnimation() {
        binding?.apply {
            shimmerPlaceholder.gone()
            shimmerPlaceholder.stopShimmer()
            rvUsers.gone()
            emptyAnimation.gone()
            errorAnimation.visible()
        }
    }

    private fun showListUser() {
        binding?.apply {
            shimmerPlaceholder.gone()
            shimmerPlaceholder.stopShimmer()
            rvUsers.visible()
            emptyAnimation.gone()
            errorAnimation.gone()
        }
    }
}