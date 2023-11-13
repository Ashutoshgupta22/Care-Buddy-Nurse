package com.aspark.carebuddynurse.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.repository.Repository
import com.aspark.carebuddynurse.retrofit.HttpStatusCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val mLoginErrorMessage = MutableLiveData<String>()
    val loginErrorMessage: LiveData<String> = mLoginErrorMessage

    private var mShowNetworkError = MutableLiveData<Boolean>()
    val showNetworkError: LiveData<Boolean> = mShowNetworkError

    private val mStartActivity = MutableLiveData<Boolean>()
    val startActivity: LiveData<Boolean> = mStartActivity

    private val mSignUpFailedError = MutableLiveData<String>()
    val signUpFailedError: LiveData<String> = mSignUpFailedError

    private val _showError = MutableLiveData<String>()
    val showError: LiveData<String> = _showError


    fun login(email: String, password: String, firebaseToken: String) {

        viewModelScope.launch(Dispatchers.IO) {

            repo.login(email, password, firebaseToken) {

                when (it) {

                    HttpStatusCode.OK -> _loginSuccess.postValue(true)

                    HttpStatusCode.UNAUTHORIZED ->
                        mLoginErrorMessage.postValue("Email not registered")

                    HttpStatusCode.FORBIDDEN ->
                        mLoginErrorMessage.postValue("Invalid email or password")

                    HttpStatusCode.FAILED -> mShowNetworkError.postValue(true)
                }
            }
        }
    }

    fun signup(nurse: Nurse) {

        viewModelScope.launch(Dispatchers.IO) {

            repo.signUp(nurse) {

                when (it) {

                    HttpStatusCode.OK -> mStartActivity.postValue(true)

                    HttpStatusCode.FAILED ->
                        mSignUpFailedError.postValue("Signup failed please try again later")

                    else -> {}
                }
            }
        }
    }

    fun getNurseById(nurseId: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            repo.getNurseById(nurseId) {

                when (it) {
                    HttpStatusCode.FAILED -> {
                        _showError.postValue("Something went wrong!")
                    }

                    else -> {}
                }
            }
        }
    }

    fun setLoginSuccessFalse() {

        _loginSuccess.postValue(false)
    }
}