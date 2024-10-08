package com.example.albertsontask.data.api

import android.accounts.NetworkErrorException
import android.content.Context
import com.example.albertsontask.R
import com.example.albertsontask.application.AlbertsonApplication
import com.example.albertsontask.data.model.common.ErrorBody
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException

object NetworkManager {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): NetworkResponse<T> {
        return try {
            val result = apiCall()
            if (result.isSuccessful) {
                NetworkResponse.Success(result.body()!!)
            } else {
                NetworkResponse.Error(
                    getErrorMessage(result.errorBody()?.string() ?: ""),
                    null,
                    result.code()
                )
            }
        } catch (throwable: Throwable) {
            NetworkResponse.Error(
                getExceptionMessage(
                    AlbertsonApplication.getInstance()!!,
                    throwable
                ), null, 500
            )
        }
    }

    private fun getErrorMessage(errorBody: String?): String {
        val gson = Gson()
        return try {
            val errorResponse = gson.fromJson(errorBody, ErrorBody::class.java)
            errorResponse.message + ""
        } catch (e: Exception) {
            e.printStackTrace()
            e.localizedMessage as String
        }
    }

    private fun getExceptionMessage(context: Context, error: Throwable): String? {
        val message: String?
        when (error) {
            is NetworkErrorException -> {
                message = context.getString(R.string.internet_warning)
            }

            is ParseException -> {
                message = context.getString(R.string.parsing_error)
            }

            is JsonSyntaxException -> {
                message = context.getString(R.string.parsing_error)
            }

            is TimeoutException -> {
                message = context.getString(R.string.socket_time_out)
            }

            is SocketTimeoutException -> {
                message = context.getString(R.string.socket_time_out)
            }

            is UnknownHostException -> {
                message = context.getString(R.string.internet_warning)
            }

            is ConnectException -> {
                message = context.getString(R.string.internet_warning)
            }

            is SocketException -> {
                message = context.getString(R.string.internet_warning)
            }

            else -> {
                message = error.message

            }
        }
        return message
    }


}