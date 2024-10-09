package com.example.albertsontask.data.repository

import com.example.albertsontask.data.api.ApiInterface
import com.example.albertsontask.data.api.NetworkResponse
import com.example.albertsontask.data.model.user.UserModel
import com.example.albertsontask.domain.repository.Repository
import com.example.albertsontask.utils.NetworkErrorUtil
import retrofit2.Response
import javax.inject.Inject

class RepositoryImp @Inject constructor(private val apiInterface: ApiInterface) : Repository {

    override suspend fun getUsers(page: Int, results: Int, inc: String): NetworkResponse<UserModel> {
        return try {
            val response=apiInterface.getUsers(page, results, inc)
            if (response.isSuccessful) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Error(
                    NetworkErrorUtil.getErrorMessage(response.errorBody()?.string() ?: ""),
                    null,
                    response.code()
                )
            }
        } catch (e:Throwable) {
            NetworkResponse.Failure("Exception",null,e)
        }

    }
}