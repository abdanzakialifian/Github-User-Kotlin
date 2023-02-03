package com.application.zaki.githubuser.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.githubuser.databinding.ItemLoadingBinding
import com.application.zaki.githubuser.utils.gone
import com.application.zaki.githubuser.utils.visible

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    inner class LoadingStateViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener {
                retry()
                binding.progressBar.visible()
                binding.btnRetry.gone()
            }
        }

        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.btnRetry.isVisible = loadState is LoadState.Error
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder =
        ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            LoadingStateViewHolder(this)
        }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}