package com.gnb.data.datasource.local

import com.gnb.data.datasource.DataSourceResponse
import com.gnb.data.datasource.ITransactionsDataSource
import com.gnb.data.provider.local.dao.TransactionDao
import com.gnb.domain.entity.local.TransactionEntity
import javax.inject.Inject

class TransactionsLocalDataSource @Inject constructor(
    private val transactionDao: TransactionDao
    ): ITransactionsDataSource.ITransactionsLocalDataSource{

    override suspend fun getTransactions(): DataSourceResponse<MutableList<TransactionEntity>> {
        val rates = transactionDao.getTransactions()
        return if(rates.isNullOrEmpty()) DataSourceResponse.NoData
        else DataSourceResponse.Success(rates.toMutableList())
    }

    override suspend fun getTransactionsBySku(sku: String): DataSourceResponse<MutableList<TransactionEntity>> {
        val rate = transactionDao.getTransactionsBySku(sku)
        return if(rate.isEmpty()) DataSourceResponse.NoData
        else DataSourceResponse.Success(rate.toMutableList())
    }

    override suspend fun insertTransactions(data: MutableList<TransactionEntity>) {
        transactionDao.insertAll(data)
    }

    override suspend fun clearTransactions() {
        transactionDao.clear()
    }

}