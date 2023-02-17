package com.application.zaki.githubuser.presentation.favorite.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.application.zaki.githubuser.databinding.FragmentFavoriteBinding
import com.application.zaki.githubuser.domain.model.User
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.favorite.adapter.FavoriteAdapter
import com.application.zaki.githubuser.presentation.favorite.viewmodel.FavoriteViewModel
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseVBFragment<FragmentFavoriteBinding>() {

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter
    private val viewModel by viewModels<FavoriteViewModel>()

    override fun getViewBinding(): FragmentFavoriteBinding =
        FragmentFavoriteBinding.inflate(layoutInflater)

    override fun initView() {
        listener()
        binding?.rvFavorite?.adapter = favoriteAdapter
        favoriteAdapter.setOnItemClickCallback(object :
            FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                navigateToDetailPage(user.username ?: "")
            }

            override fun onFavoriteClicked(user: User) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteUser(user)
                }
            }
        })
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getAllUser
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { users ->
                    if (users.isNotEmpty()) {
                        showUserFavorite(users)
                    } else {
                        showEmptyAnimation()
                    }
                }
        }
    }

    private fun listener() {
        binding?.imgArrowBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showUserFavorite(users: List<User>) {
        binding?.apply {
            favoriteAdapter.submitList(users)
            rvFavorite.visible()
            emptyAnimation.gone()
        }
    }

    private fun showEmptyAnimation() {
        binding?.apply {
            rvFavorite.gone()
            emptyAnimation.visible()
        }
    }

    private fun navigateToDetailPage(username: String) {
        val navigateToDetailFragment =
            FavoriteFragmentDirections.actionFavoriteFragmentToDetailUserFragment(username)
        findNavController().navigate(navigateToDetailFragment)
    }

    override fun onDestroyView() {
        binding?.rvFavorite?.adapter = null
        super.onDestroyView()
    }
}