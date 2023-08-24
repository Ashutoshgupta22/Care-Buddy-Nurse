package com.aspark.carebuddynurse.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.carebuddynurse.repository.Repository
import com.aspark.carebuddynurse.retrofit.HttpStatusCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(private val repo: Repository): ViewModel() {

    private var _showError = MutableLiveData<String>()
    val showError: LiveData<String> = _showError

    fun getNurseById(nurseId: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            repo.getNurseById(nurseId) {

                when(it) {
                    HttpStatusCode.FAILED -> {
                        _showError.postValue("Something went wrong!")
                    }
                    else -> {}
                }
            }

        }
    }

}