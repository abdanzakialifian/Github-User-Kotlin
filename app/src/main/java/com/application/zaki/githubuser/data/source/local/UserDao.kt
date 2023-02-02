package com.application.zaki.githubuser.data.source.local

import androidx.room.*
import com.application.zaki.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM tb_user")
    fun getAllUser(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(userEntity: UserEntity)

    @Delete
    fun deleteUser(userEntity: UserEntity)

    @Query("SELECT EXISTS (SELECT * FROM tb_user WHERE id=:id)")
    fun getUserById(id: Int): Boolean
}