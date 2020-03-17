package com.mancel.yann.realestatemanager.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mancel.yann.realestatemanager.models.User
import com.mancel.yann.realestatemanager.repositories.UserRepository
import kotlinx.coroutines.launch

/**
 * Created by Yann MANCEL on 09/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.viewModels
 *
 * A [ViewModel] subclass.
 */
class RealEstateViewModel(private val mUserRepository: UserRepository) : ViewModel() {

    // FIELDS --------------------------------------------------------------------------------------

        // LiveData

    // CONSTRUCTORS --------------------------------------------------------------------------------

    init {
        Log.d(this::class.java.simpleName, "RealEstateViewModel: INIT")
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    override fun onCleared() {
        super.onCleared()
        Log.d(this::class.java.simpleName, "RealEstateViewModel: onCleared")
    }

    // -- User --

    /**
     * Inserts the new [User] in argument
     */
    fun insertUser(user: User) = viewModelScope.launch { mUserRepository.insertUser(user) }

    /**
     * Inserts several new [User]s in argument
     */
    fun insertUsers(vararg users: User) = viewModelScope.launch { mUserRepository.insertUsers(*users) }
}