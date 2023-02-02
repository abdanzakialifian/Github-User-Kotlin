package com.application.zaki.githubuser.presentation.favorite.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.application.zaki.githubuser.R
import com.application.zaki.githubuser.databinding.FragmentFavoriteBinding
import com.application.zaki.githubuser.domain.model.User
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.favorite.adapter.FavoriteAdapter
import com.application.zaki.githubuser.presentation.favorite.viewmodel.FavoriteViewModel
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
            override fun onFavoriteClicked(user: User) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteUser(user)
                }
            }
        })
        lifecycleScope.launchWhenStarted {
            viewModel.getAllUser()
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { users ->
                    favoriteAdapter.submitList(users)
                }
        }
    }

    private fun listener() {
        binding?.imgArrowBack?.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_homeFragment)
        }
    }
}