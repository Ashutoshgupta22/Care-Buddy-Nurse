package com.aspark.carebuddynurse.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.carebuddynurse.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repo: Repository): ViewModel() {

    fun uploadProfilePic() {

        viewModelScope.launch(Dispatchers.IO) {


        }
    }

}