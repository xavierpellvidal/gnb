package com.gnb.data.datasource.remote

import com.gnb.data.datasource.DataSourceResponse
import com.gnb.data.datasource.ITransactionsDataSource
import com.gnb.data.provider.remote.ApiResponse
import com.gnb.data.provider.remote.ApiService
import com.gnb.data.provider.remote.handleRemoteResponse
import com.gnb.domain.entity.remote.TransactionModel
import javax.inject.Inject

class TransactionsRemoteDataSource @Inject constructor(
    private val apiService: ApiService
    ): ITransactionsDataSource.ITransactionsRemoteDataSource{

    override suspend fun getTransactions(): DataSourceResponse<MutableList<TransactionModel>> {
        return when (val result = handleRemoteResponse { apiService.getTransactions() }) {
                is ApiResponse.Success -> DataSourceResponse.Success(result.data)
                is ApiResponse.BodyNull -> DataSourceResponse.NoData
                is ApiResponse.NoNetwork -> DataSourceResponse.NoNetwork
                is ApiResponse.Error -> DataSourceResponse.GenericError
                is ApiResponse.Exception -> DataSourceResponse.GenericError
        }
    }
}