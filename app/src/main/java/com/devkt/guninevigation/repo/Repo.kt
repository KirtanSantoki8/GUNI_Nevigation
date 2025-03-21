package com.devkt.guninevigation.repo

import com.devkt.guninevigation.api.ApiBuilder
import com.devkt.guninevigation.model.CreateUserResponse
import com.devkt.guninevigation.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class Repo @Inject constructor(private val api: ApiBuilder) {
    suspend fun createUser(
        name: String,
        email: String,
        password: String,
        phone_no: String
    ): Flow<Result<Response<CreateUserResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.createUser(name, email, password, phone_no)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Flow<Result<Response<CreateUserResponse>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.api.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}