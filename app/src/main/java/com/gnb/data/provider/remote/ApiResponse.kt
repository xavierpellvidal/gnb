package com.gnb.data.provider.remote

import retrofit2.Response
import java.net.UnknownHostException

sealed class ApiResponse<T : Any> {
    class Success<T : Any>(val data: T) : ApiResponse<T>()
    class BodyNull<T : Any>(val data: T?) : ApiResponse<T>()
    class NoNetwork<T : Any> : ApiResponse<T>()
    class Error<T : Any>(val code: Int) : ApiResponse<T>()
    class Exception<T : Any> : ApiResponse<T>()
}

/**
 * Handles retrofit Response and convert it to a ApiResponse.
 */
suspend fun <T : Any> handleRemoteResponse(execute: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful) {
            if (body != null) ApiResponse.Success(body)
            else ApiResponse.BodyNull(body)
        } else {
            ApiResponse.Error(code = response.code())
        }
    } catch (e: UnknownHostException) {
        return ApiResponse.NoNetwork()
    } catch (e: Exception) {
        return ApiResponse.Exception()
    }
}