package com.example.suitmediatest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suitmediatest.data.api.ApiClient
import com.example.suitmediatest.data.models.User
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentPage = 1
    private var totalPage = 1

    init {
        fetchUsers(currentPage)
    }

    fun fetchUsers(page: Int) {
        if (_isLoading.value == true) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.getInstance().getUsers(page, 9)
                if (page == 1) {
                    _users.value = response.data
                } else {
                    _users.value = _users.value?.plus(response.data)
                }
                currentPage = response.page
                totalPage = response.total_pages
                _isLoading.value = false
            } catch (e: Exception) {
                if (_users.value.isNullOrEmpty()) {
                    _error.value = "Failed to load data"
                } else {
                    _error.value = "Failed to load more data"
                }
                _isLoading.value = false
            }
        }
    }

    fun refreshUsers() {
        currentPage = 1
        fetchUsers(currentPage)
    }

    fun loadMoreUsers() {
        if (currentPage < totalPage) {
            fetchUsers(currentPage + 1)
        }
    }

    fun clearError() {
        _error.value = null
    }
}