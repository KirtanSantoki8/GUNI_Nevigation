package com.devkt.guninevigation.api

import com.devkt.guninevigation.model.CreateUserResponse
import com.devkt.guninevigation.model.GetMoreLocationsResponse
import com.devkt.guninevigation.model.GetSubLocationsResponse
import com.devkt.guninevigation.model.SpecificLocationResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("createUser")
    suspend fun createUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone_no") phone_no: String
    ): Response<CreateUserResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<CreateUserResponse>

    @GET("getAllLocation")
    suspend fun getMoreLocations(): Response<GetMoreLocationsResponse>

    @FormUrlEncoded
    @POST("getSubLocations")
    suspend fun getSubLocations(
        @Field("mainLocation") mainLocation: String
    ): Response<GetSubLocationsResponse>

    @FormUrlEncoded
    @POST("getSpecificSubLocation")
    suspend fun getSpecificSubLocation(
        @Field("name") subLocation: String
    ): Response<SpecificLocationResponse>
}