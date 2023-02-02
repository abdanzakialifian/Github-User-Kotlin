package com.application.zaki.githubuser.presentation.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.githubuser.R
import com.application.zaki.githubuser.databinding.ItemListFavoriteBinding
import com.application.zaki.githubuser.domain.model.User
import com.application.zaki.githubuser.utils.loadImageUrl
import javax.inject.Inject

class FavoriteAdapter @Inject constructor() :
    ListAdapter<User, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class FavoriteViewHolder(private val binding: ItemListFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User) {
            binding.apply {
                imgProfileUser.loadImageUrl(item.image)
                tvUsernameUser.text = item.username
                imgFavorite.setOnClickListener {
                    onItemClickCallback.onFavoriteClicked(item)
                    binding.imgFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_favorite_border_grey_24
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder =
        ItemListFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            FavoriteViewHolder(this)
        }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickCallback {
        fun onFavoriteClicked(user: User)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }
}