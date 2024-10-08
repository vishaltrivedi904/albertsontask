package com.example.albertsontask.data.api

sealed class NetworkResponse<T>(
    val data: T? = null,
    val code: Int? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResponse<T>(data)

    class Error<T>(message: String?, data: T? = null, code: Int? = null) :
        NetworkResponse<T>(data, code, message)

    class Loading<T> : NetworkResponse<T>()

}