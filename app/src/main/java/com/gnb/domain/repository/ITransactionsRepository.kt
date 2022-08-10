package com.gnb.domain.repository

import com.gnb.domain.entity.local.TransactionEntity

interface ITransactionsRepository {

    suspend fun getTransactions(): RepositoryResponse<MutableList<TransactionEntity>>

    suspend fun getTransactionsBySku(sku: String): RepositoryResponse<MutableList<TransactionEntity>>

    suspend fun insertTransactions(transactions: MutableList<TransactionEntity>)

    suspend fun clearTransactions()

}