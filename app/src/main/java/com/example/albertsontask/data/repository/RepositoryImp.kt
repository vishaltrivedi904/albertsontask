package com.example.albertsontask.data.repository

import com.example.albertsontask.data.api.ApiInterface
import com.example.albertsontask.data.model.user.UserModel
import com.example.albertsontask.domain.repository.Repository
import retrofit2.Response
import javax.inject.Inject

class RepositoryImp @Inject constructor(private val apiInterface: ApiInterface) : Repository {

    override suspend fun getUsers(page: Int, results: Int, inc: String): Response<UserModel> {
        return apiInterface.getUsers(page, results, inc)
    }


}