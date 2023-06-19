package com.aspark.carebuddynurse.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.carebuddynurse.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel(
    @Inject val repo: Repository
    ) : ViewModel() {

    private val mCallActivity = MutableLiveData<Boolean>()
    val callActivity: LiveData<Boolean> = mCallActivity
    private val mLoginErrorMessage = MutableLiveData<String>()
    val loginErrorMessage: LiveData<String> = mLoginErrorMessage
    var showNetworkError = MutableLiveData<Boolean>()


    fun loginClickListener(email: String, password: String, firebaseToken: String) {

        viewModelScope.launch(Dispatchers.IO) {

            repo.login(email, password, firebaseToken)
        }

    }

}