package com.application.zaki.githubuser.presentation.home.view

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.application.zaki.githubuser.databinding.FragmentHomeBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.presentation.base.BaseVBFragment
import com.application.zaki.githubuser.presentation.home.adapter.HomePagingAdapter
import com.application.zaki.githubuser.presentation.home.viewmodel.HomeViewModel
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible
import dagger.hilt.android.AndroidEntryPoint
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
    }

    private fun setListUser() {
        binding?.rvUsers?.adapter = homePagingAdapter
        binding?.rvUsers?.setHasFixedSize(true)
        binding?.progressBar?.visible()
        lifecycleScope.launch {
            viewModel.getListUser()
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding?.progressBar?.gone()
                    homePagingAdapter.submitData(lifecycle, it)
                }
        }
        homePagingAdapter.setOnItemClickCallback(object : HomePagingAdapter.OnItemCliCkCallback {
            override fun onItemClicked(item: ListUsers?) {
                Toast.makeText(requireContext(), item?.login, Toast.LENGTH_SHORT).show()
            }
        })
    }
}