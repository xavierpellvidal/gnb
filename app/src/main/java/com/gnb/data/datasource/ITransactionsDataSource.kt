package com.gnb.data.datasource

import com.gnb.domain.entity.local.TransactionEntity
import com.gnb.domain.entity.remote.TransactionModel

interface ITransactionsDataSource {

    interface ITransactionsRemoteDataSource {
        suspend fun getTransactions(): DataSourceResponse<MutableList<TransactionModel>>
    }

    interface ITransactionsLocalDataSource {
        suspend fun getTransactions(): DataSourceResponse<MutableList<TransactionEntity>>
        suspend fun getTransactionsBySku(sku: String): DataSourceResponse<MutableList<TransactionEntity>>
        suspend fun insertTransactions(data: MutableList<TransactionEntity>)
        suspend fun clearTransactions()
    }

}