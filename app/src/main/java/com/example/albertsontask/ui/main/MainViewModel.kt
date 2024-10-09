package com.example.albertsontask.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albertsontask.data.api.NetworkResponse
import com.example.albertsontask.data.model.user.UserModel
import com.example.albertsontask.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _data = MutableLiveData<NetworkResponse<UserModel>>()
    val data: LiveData<NetworkResponse<UserModel>> get() = _data

    var isLoading: Boolean = false
    var page = 1
    var results = 10
    var type = 0

    fun getUsers(page: Int, results: Int, inc: String) {
        viewModelScope.launch {
            _data.postValue(NetworkResponse.Loading())
            val result = repository.getUsers(page, results, inc)
            _data.postValue(result)
        }
    }
}