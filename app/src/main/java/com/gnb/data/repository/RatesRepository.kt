package com.gnb.data.repository;

import com.gnb.data.datasource.DataSourceResponse
import com.gnb.data.datasource.local.RatesLocalDataSource
import com.gnb.data.datasource.mapListTo
import com.gnb.data.datasource.remote.RatesRemoteDataSource
import com.gnb.domain.entity.local.RateEntity
import com.gnb.domain.repository.IRatesRepository
import com.gnb.domain.repository.RepositoryResponse
import javax.inject.Inject

class RatesRepository @Inject constructor(
        private val remoteDataSource: RatesRemoteDataSource,
        private val localDataSource: RatesLocalDataSource
        ): IRatesRepository {

        /**
         * Get rates from remote datasource and save it to local datasource
         */
        override suspend fun getRemoteRates(): RepositoryResponse<MutableList<RateEntity>> =
                when (val result = remoteDataSource.getRates().mapListTo()) {
                        is DataSourceResponse.Success -> {
                                clearRates()
                                insertRates(result.data)
                                RepositoryResponse.Success(result.data)
                        }
                        is DataSourceResponse.NoData -> RepositoryResponse.NoData
                        is DataSourceResponse.NoNetwork -> RepositoryResponse.NoNetwork
                        is DataSourceResponse.GenericError -> RepositoryResponse.GenericError
                }

        override suspend fun getLocalRates(): RepositoryResponse<MutableList<RateEntity>> =
                when (val result = localDataSource.getRates()){
                        is DataSourceResponse.Success -> RepositoryResponse.Success(result.data)
                        is DataSourceResponse.NoData -> RepositoryResponse.NoData
                        is DataSourceResponse.NoNetwork -> RepositoryResponse.NoNetwork
                        is DataSourceResponse.GenericError -> RepositoryResponse.GenericError
                }

        override suspend fun clearRates() = localDataSource.clearRates()

        override suspend fun insertRates(rates: MutableList<RateEntity>) = localDataSource.insertRates(rates)

        override suspend fun insertRate(rate: RateEntity) = localDataSource.insertRate(rate)

}
