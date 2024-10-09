package com.example.albertsontask.data.api

sealed class NetworkResponse<T>(
    val data: T? = null,
    val code: Int? = null,
    val message: String? = null,
    val throwable: Throwable?=null
) {

    class Success<T>(data: T) : NetworkResponse<T>(data)

    class Error<T>(message: String?, data: T? = null, code: Int? = null) :
        NetworkResponse<T>(data, code, message)

    class Failure<T>(message: String?, code: Int? = null, throwable: Throwable? = null) :
        NetworkResponse<T>(message = message, code = code, throwable = throwable)

    class Loading<T> : NetworkResponse<T>()

}