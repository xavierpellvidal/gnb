package com.gnb.domain.usecase.rate

import com.gnb.data.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAndSaveRatesUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {

        suspend fun getAndSaveRates() {
            return withContext(Dispatchers.IO) {
                ratesRepository.getRemoteRates()
            }
        }

}