package com.application.zaki.githubuser.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.githubuser.databinding.ItemListUsersDetailBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.utils.loadImageUrl
import javax.inject.Inject

class DetailPagingAdapter @Inject constructor() :
    PagingDataAdapter<ListUsers, DetailPagingAdapter.DetailPagingViewHolder>(DIFF_CALLBACK) {

    class DetailPagingViewHolder(private val binding: ItemListUsersDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListUsers?) {
            binding.apply {
                imgProfileUser.loadImageUrl(item?.avatarUrl)
                tvUsernameUser.text = item?.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPagingViewHolder =
        ItemListUsersDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            DetailPagingViewHolder(this)
        }

    override fun onBindViewHolder(holder: DetailPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListUsers>() {
            override fun areItemsTheSame(oldItem: ListUsers, newItem: ListUsers): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListUsers, newItem: ListUsers): Boolean =
                oldItem == newItem
        }
    }
}