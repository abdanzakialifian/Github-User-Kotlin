package com.application.zaki.githubuser.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.application.zaki.githubuser.data.source.local.UserDao
import com.application.zaki.githubuser.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}