package com.gnb.domain.usecase.transaction

import com.gnb.data.repository.TransactionsRepository
import com.gnb.domain.entity.ui.ProductUI
import com.gnb.domain.repository.RepositoryResponse
import com.gnb.domain.usecase.UseCaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) {

    /**
     * Returns all products. These products are transactions
     * grouped by sku and converted to a ProductUI list.
     */
    suspend fun getProducts(): UseCaseResponse<MutableList<ProductUI>> {
        return withContext(Dispatchers.IO) {
            when (val result = transactionsRepository.getTransactions()) {
                is RepositoryResponse.Success -> {
                    if (result.data.size == 0) UseCaseResponse.NoData
                    else {
                        // Group transaction by sku
                        var grouped = result.data.groupBy { it.sku }
                            .map { (k, values) -> ProductUI(k, values.count().toString(), "") }
                            .sortedBy { it.sku }
                        UseCaseResponse.Success(grouped.toMutableList())
                    }
                }
                is RepositoryResponse.NoData -> UseCaseResponse.NoData
                is RepositoryResponse.NoNetwork -> UseCaseResponse.NoNetwork
                is RepositoryResponse.GenericError -> UseCaseResponse.GenericError
            }
        }
    }

}