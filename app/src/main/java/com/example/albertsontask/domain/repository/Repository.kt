package com.example.albertsontask.domain.repository

import android.net.Network
import com.example.albertsontask.data.api.NetworkResponse
import com.example.albertsontask.data.model.user.UserModel
import retrofit2.Response

interface Repository {
    suspend fun getUsers(page: Int, results: Int, inc: String): NetworkResponse<UserModel>
}