package com.application.zaki.githubuser.presentation.home.view

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
            val navigateToFavoriteFragment =
                HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
            findNavController().navigate(navigateToFavoriteFragment)
        }
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

            override fun onFavoriteClicked(item: ListUsers?, imageView: ImageView) {
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

            override fun onUserId(id: Int, imageView: ImageView) {
                CoroutineScope(Dispatchers.IO).launch {
                    val isFavorite = viewModel.getUserById(id)
                    if (isFavorite) {
                        favoriteUser(imageView, true)
                    } else {
                        favoriteUser(imageView, false)
                    }
                }
            }
        })
    }

    fun favoriteUser(imgFavorite: ImageView, isFavorite: Boolean = false) {
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
                                    emptyAnimation.gone()
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