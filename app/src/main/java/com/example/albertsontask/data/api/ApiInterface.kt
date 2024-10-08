package com.example.albertsontask.data.api

import com.example.albertsontask.data.model.user.UserModel
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @GET("api")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("results") result: Int,
        @Query("inc") inc: String
    ): Response<UserModel>
}