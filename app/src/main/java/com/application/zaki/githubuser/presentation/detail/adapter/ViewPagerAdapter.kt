package com.application.zaki.githubuser.presentation.detail.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.zaki.githubuser.presentation.detail.view.FollowersFragment
import com.application.zaki.githubuser.presentation.detail.view.RepositoryFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = NUMBER_OF_TABS

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> FollowersFragment()
        1 -> FollowersFragment()
        else -> RepositoryFragment()
    }

    companion object {
        private const val NUMBER_OF_TABS = 3
    }
}