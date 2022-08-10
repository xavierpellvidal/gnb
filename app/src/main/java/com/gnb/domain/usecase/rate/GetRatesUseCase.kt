package com.gnb.domain.usecase.rate

import com.gnb.data.repository.RatesRepository
import com.gnb.domain.entity.ui.RateUI
import com.gnb.domain.repository.RepositoryResponse
import com.gnb.domain.usecase.UseCaseResponse
import com.gnb.domain.usecase.mapListTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {

    suspend fun getRates(): UseCaseResponse<MutableList<RateUI>> {
        return withContext(Dispatchers.IO) {
            when (val result = ratesRepository.getLocalRates()) {
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
