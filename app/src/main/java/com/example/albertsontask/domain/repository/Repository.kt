package com.example.albertsontask.domain.repository

import com.example.albertsontask.data.model.user.UserModel
import retrofit2.Response

interface Repository {
    suspend fun getUsers(page: Int, results: Int, inc: String): Response<UserModel>
}