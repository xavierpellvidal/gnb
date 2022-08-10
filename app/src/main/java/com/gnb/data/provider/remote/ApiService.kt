package com.gnb.data.provider.remote

import com.gnb.domain.entity.remote.RateModel
import com.gnb.domain.entity.remote.TransactionModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    @Headers("Accept:application/json")
    @GET("rates")
    suspend fun getRates(): Response<MutableList<RateModel>>

    @Headers("Accept:application/json")
    @GET("transactions")
    suspend fun getTransactions(): Response<MutableList<TransactionModel>>

}
