package com.gnb.data.repository

import com.gnb.data.datasource.DataSourceResponse
import com.gnb.data.datasource.local.TransactionsLocalDataSource
import com.gnb.data.datasource.mapListTo
import com.gnb.data.datasource.remote.TransactionsRemoteDataSource
import com.gnb.domain.entity.local.TransactionEntity
import com.gnb.domain.repository.ITransactionsRepository
import com.gnb.domain.repository.RepositoryResponse
import javax.inject.Inject

class TransactionsRepository @Inject constructor(
        private val remoteDataSource: TransactionsRemoteDataSource,
        private val localDataSource: TransactionsLocalDataSource
): ITransactionsRepository {

        /**
         * Get transactions from remote datasource and save it to local datasource
         */
        override suspend fun getTransactions(): RepositoryResponse<MutableList<TransactionEntity>> =
                when (val result = remoteDataSource.getTransactions().mapListTo()) {
                        is DataSourceResponse.Success -> {
                                clearTransactions()
                                insertTransactions(result.data)
                                RepositoryResponse.Success(result.data)
                        }
                        is DataSourceResponse.NoData -> RepositoryResponse.NoData
                        is DataSourceResponse.NoNetwork -> RepositoryResponse.NoNetwork
                        is DataSourceResponse.GenericError -> RepositoryResponse.GenericError
                }

        override suspend fun getTransactionsBySku(sku: String): RepositoryResponse<MutableList<TransactionEntity>> =
                when (val result = localDataSource.getTransactionsBySku(sku)){
                        is DataSourceResponse.Success -> RepositoryResponse.Success(result.data)
                        is DataSourceResponse.NoData -> RepositoryResponse.NoData
                        is DataSourceResponse.NoNetwork -> RepositoryResponse.NoNetwork
                        is DataSourceResponse.GenericError -> RepositoryResponse.GenericError
                }

        override suspend fun clearTransactions() = localDataSource.clearTransactions()

        override suspend fun insertTransactions(transactions: MutableList<TransactionEntity>) = localDataSource.insertTransactions(transactions)

}
