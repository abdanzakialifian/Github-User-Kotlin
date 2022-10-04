package com.application.zaki.githubuser.presentation.detail.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.githubuser.R
import com.application.zaki.githubuser.databinding.ItemListRepositoryBinding
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import java.util.*
import javax.inject.Inject

class RepositoryPagingAdapter @Inject constructor() :
    PagingDataAdapter<RepositoriesUser, RepositoryPagingAdapter.RepositoryPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    class RepositoryPagingViewHolder(private val binding: ItemListRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RepositoriesUser?) {
            binding.apply {
                val random = Random()
                val randomColor =
                    Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
                imgCircle.setColorFilter(randomColor)
                tvRepositoryName.text = item?.name
                tvStatusRepository.text = item?.visibility
                tvLanguage.text = item?.language ?: itemView.resources.getString(R.string.dash)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryPagingViewHolder =
        ItemListRepositoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            RepositoryPagingViewHolder((this))
        }

    override fun onBindViewHolder(holder: RepositoryPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RepositoriesUser>() {
            override fun areItemsTheSame(
                oldItem: RepositoriesUser,
                newItem: RepositoriesUser
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: RepositoriesUser,
                newItem: RepositoriesUser
            ): Boolean = oldItem == newItem
        }
    }
}