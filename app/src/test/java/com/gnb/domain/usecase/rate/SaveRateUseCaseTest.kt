package com.gnb.domain.usecase.rate

import com.gnb.data.repository.RatesRepository
import com.gnb.domain.entity.ui.RateUI
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SaveRateUseCaseTest{

    @RelaxedMockK
    private lateinit var ratesRepository: RatesRepository

    lateinit var saveRatesUseCase: SaveRateUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        saveRatesUseCase = SaveRateUseCase(ratesRepository)
    }

    @Test
    fun `when use case called get remote rates is called`() = runBlocking {
        val rate = RateUI("EUR", "USD","1.2")

        //When
        saveRatesUseCase.saveRate(rate)

        //Then
        coVerify { ratesRepository.insertRate(rate.convertTo()) }
    }

}