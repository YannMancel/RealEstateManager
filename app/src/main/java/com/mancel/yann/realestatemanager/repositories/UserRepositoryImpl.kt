package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.dao.UserDAO
import com.mancel.yann.realestatemanager.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [UserRepository].
 */
class UserRepositoryImpl(
    private val mUserDAO: UserDAO
) : UserRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override suspend fun insertUser(user: User): Long = withContext(Dispatchers.IO) {
        this@UserRepositoryImpl.mUserDAO.insertUser(user)
    }

    // -- Read --

    override fun getUserById(userId: Long): LiveData<User> = this.mUserDAO.getUserById(userId)

    // -- Update --

    override suspend fun updateUser(user: User): Int = withContext(Dispatchers.IO) {
        this@UserRepositoryImpl.mUserDAO.updateUser(user)
    }

    // -- Delete --

    override suspend fun deleteUser(user: User): Int = withContext(Dispatchers.IO) {
        this@UserRepositoryImpl.mUserDAO.deleteUser(user)
    }
}