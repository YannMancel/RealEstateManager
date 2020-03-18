package com.mancel.yann.realestatemanager.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mancel.yann.realestatemanager.models.IdTypeAddressPriceTupleOfRealEstate
import com.mancel.yann.realestatemanager.models.User
import com.mancel.yann.realestatemanager.repositories.RealEstateRepository
import com.mancel.yann.realestatemanager.repositories.UserRepository
import kotlinx.coroutines.launch

/**
 * Created by Yann MANCEL on 09/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.viewModels
 *
 * A [ViewModel] subclass.
 */
class RealEstateViewModel(
    private val mUserRepository: UserRepository,
    private val mRealEstateRepository: RealEstateRepository
    ) : ViewModel() {


    // FIELDS --------------------------------------------------------------------------------------

    private var mUsers: LiveData<List<User>>? = null

    private var mRealEstatesSimpleFormat: LiveData<List<IdTypeAddressPriceTupleOfRealEstate>>? = null

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
     * Gets all [User] thanks to [LiveData]
     */
    fun getAllUsers(): LiveData<List<User>> {
        if (this.mUsers == null) {
            this.mUsers = this.mUserRepository.getAllUsers()
        }
        return this.mUsers!!
    }

    // -- Real Estate --

    /**
     * Gets all [IdTypeAddressPriceTupleOfRealEstate] thanks to [LiveData]
     */
    fun getRealEstatesSimpleFormat(): LiveData<List<IdTypeAddressPriceTupleOfRealEstate>> {
        if (this.mRealEstatesSimpleFormat == null) {
            this.mRealEstatesSimpleFormat = this.mRealEstateRepository.getIdTypeAddressPriceTupleOfRealEstate()
        }
        return this.mRealEstatesSimpleFormat!!
    }
}