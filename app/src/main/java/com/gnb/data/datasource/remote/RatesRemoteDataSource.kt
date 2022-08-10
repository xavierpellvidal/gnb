package com.gnb.data.datasource.remote

import com.gnb.data.datasource.DataSourceResponse
import com.gnb.data.datasource.IRatesDataSource
import com.gnb.data.provider.remote.ApiResponse
import com.gnb.data.provider.remote.ApiService
import com.gnb.data.provider.remote.handleRemoteResponse
import com.gnb.domain.entity.remote.RateModel
import javax.inject.Inject

class RatesRemoteDataSource @Inject constructor(
    private val apiService: ApiService
    ): IRatesDataSource.IRatesRemoteDataSource{

    override suspend fun getRates(): DataSourceResponse<MutableList<RateModel>> {
        return when (val result = handleRemoteResponse { apiService.getRates() }) {
                is ApiResponse.Success -> DataSourceResponse.Success(result.data)
                is ApiResponse.BodyNull -> DataSourceResponse.NoData
                is ApiResponse.NoNetwork -> DataSourceResponse.NoNetwork
                is ApiResponse.Error -> DataSourceResponse.GenericError
                is ApiResponse.Exception -> DataSourceResponse.GenericError
        }
    }
}