package com.application.zaki.githubuser.presentation.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.githubuser.R
import com.application.zaki.githubuser.databinding.ItemListUsersBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.utils.loadImageUrl
import javax.inject.Inject

class HomePagingAdapter @Inject constructor() :
    PagingDataAdapter<ListUsers, HomePagingAdapter.HomePagingViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemCliCkCallback: IOnItemCliCkCallback

    fun setOnItemClickCallback(onItemCliCkCallback: IOnItemCliCkCallback) {
        this.onItemCliCkCallback = onItemCliCkCallback
    }

    inner class HomePagingViewHolder(private val binding: ItemListUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListUsers?) {
            onItemCliCkCallback.onUserId(item?.id ?: 0, binding.imgFavorite)

            binding.apply {
                imgUser.loadImageUrl(item?.avatarUrl)
                tvUsername.text = item?.login

                // add link github
                tvGithubUrl.text = item?.htmlUrl
                tvGithubUrl.setOnClickListener {
                    Intent.parseUri(item?.htmlUrl, Intent.URI_INTENT_SCHEME).run {
                        itemView.context.startActivity(this)
                    }
                }

                imgFavorite.setOnClickListener {
                    onItemCliCkCallback.onFavoriteClicked(item, binding.imgFavorite)
                }

                itemView.setOnClickListener {
                    onItemCliCkCallback.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePagingViewHolder =
        ItemListUsersBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            HomePagingViewHolder(this)
        }

    override fun onBindViewHolder(holder: HomePagingViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setIsRecyclable(true)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface IOnItemCliCkCallback {
        fun onItemClicked(item: ListUsers?)
        fun onFavoriteClicked(item: ListUsers?, imageView: ImageView)
        fun onUserId(id: Int, imageView: ImageView)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListUsers>() {
            override fun areItemsTheSame(
                oldItem: ListUsers,
                newItem: ListUsers
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListUsers,
                newItem: ListUsers
            ): Boolean = oldItem == newItem
        }
    }
}