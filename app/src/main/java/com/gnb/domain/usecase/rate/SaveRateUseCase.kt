package com.gnb.domain.usecase.rate

import com.gnb.data.repository.RatesRepository
import com.gnb.domain.entity.ui.RateUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveRateUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {

    /**
     * Save the given [rate].
     */
    suspend fun saveRate(rate: RateUI) {
        return withContext(Dispatchers.IO) {
            ratesRepository.insertRate(rate.convertTo())
        }
    }

}
