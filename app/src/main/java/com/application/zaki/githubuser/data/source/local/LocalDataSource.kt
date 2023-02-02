package com.application.zaki.githubuser.data.source.local

import com.application.zaki.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val userDao: UserDao) {
    fun getAllUser(): Flow<List<UserEntity>> = flow { emitAll(userDao.getAllUser()) }

    fun addUser(userEntity: UserEntity) = userDao.addUser(userEntity)

    fun deleteUser(userEntity: UserEntity) = userDao.deleteUser(userEntity)

    fun getUserById(id: Int): Boolean = userDao.getUserById(id)
}