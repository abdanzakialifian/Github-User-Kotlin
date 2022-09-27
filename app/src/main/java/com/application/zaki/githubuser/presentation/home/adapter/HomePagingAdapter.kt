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
import com.application.zaki.githubuser.databinding.ItemListUserBinding
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.utils.loadImageUrl
import javax.inject.Inject

class HomePagingAdapter @Inject constructor() :
    PagingDataAdapter<ListUsers, HomePagingAdapter.HomePagingViewHolder>(DIFF_CALLBACK) {

    private var isFavorite = false
    private lateinit var onItemCliCkCallback: OnItemCliCkCallback

    fun setOnItemClickCallback(onItemCliCkCallback: OnItemCliCkCallback) {
        this.onItemCliCkCallback = onItemCliCkCallback
    }

    inner class HomePagingViewHolder(private val binding: ItemListUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListUsers?) {
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
                    favoriteUser(imgFavorite, itemView.context)
                }

                itemView.setOnClickListener {
                    onItemCliCkCallback.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePagingViewHolder =
        ItemListUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            HomePagingViewHolder(this)
        }

    override fun onBindViewHolder(holder: HomePagingViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setIsRecyclable(true)
    }

    fun favoriteUser(imgFavorite: ImageView, context: Context) {
        if (isFavorite) {
            imgFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_favorite_border_grey_24
                )
            )
            isFavorite = false
        } else {
            imgFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_favorite_filled_red_24
                )
            )
            isFavorite = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface OnItemCliCkCallback {
        fun onItemClicked(item: ListUsers?)
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