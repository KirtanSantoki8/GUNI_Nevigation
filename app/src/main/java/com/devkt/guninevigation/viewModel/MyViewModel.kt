package com.devkt.guninevigation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devkt.guninevigation.common.Result
import com.devkt.guninevigation.model.CreateUserResponse
import com.devkt.guninevigation.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val repo: Repo): ViewModel() {
    private val _createUser = MutableStateFlow(CreateUserState())
    val createUser = _createUser.asStateFlow()

    private val _loginUser = MutableStateFlow(LoginUserState())
    val loginUser = _loginUser.asStateFlow()

    fun createUser(
        name: String,
        email: String,
        password: String,
        phone_no: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            repo.createUser(
                name,
                email,
                password,
                phone_no
            ).collect {
                when(it){
                    is Result.Loading -> {
                        _createUser.value = CreateUserState(isLoading = true)
                    }
                    is Result.Success -> {
                        _createUser.value = CreateUserState(data = it.data.body())
                    }
                    is Result.Error -> {
                        _createUser.value = CreateUserState(error = it.message)
                    }
                }
            }
        }
    }

    fun login(
        email: String,
        password: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            repo.login(
                email,
                password
            ).collect {
                when(it) {
                    is Result.Loading -> {
                        _loginUser.value = LoginUserState(isLoading = true)
                    }

                    is Result.Success -> {
                        _loginUser.value = LoginUserState(data = it.data.body())
                    }

                    is Result.Error -> {
                        _loginUser.value = LoginUserState(error = it.message)
                    }
                }
            }
        }
    }
}

data class CreateUserState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: CreateUserResponse? = null
)

data class LoginUserState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: CreateUserResponse? = null
)