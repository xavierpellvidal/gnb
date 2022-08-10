package com.gnb.domain.usecase.transaction

import com.gnb.data.repository.TransactionsRepository
import com.gnb.domain.entity.ui.ProductUI
import com.gnb.domain.repository.RepositoryResponse
import com.gnb.domain.usecase.UseCaseResponse
import com.gnb.domain.usecase.mapListTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionsRepository
) {

    /**
     * Returns all transactions of the given [sku]
     */
    suspend fun getTransactions(sku: String): UseCaseResponse<MutableList<ProductUI>> {
        return withContext(Dispatchers.IO) {
            when (val result = transactionRepository.getTransactionsBySku(sku)) {
                is RepositoryResponse.Success -> {
                    if (result.data.size > 0) UseCaseResponse.Success(result.data).mapListTo()
                    else UseCaseResponse.NoData
                }
                is RepositoryResponse.NoData -> UseCaseResponse.NoData
                is RepositoryResponse.NoNetwork -> UseCaseResponse.NoNetwork
                is RepositoryResponse.GenericError -> UseCaseResponse.GenericError
            }
        }
    }

}