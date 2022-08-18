package com.gnb.domain.usecase.rate

import com.gnb.data.repository.RatesRepository
import com.gnb.domain.entity.local.RateEntity
import com.gnb.domain.repository.RepositoryResponse
import com.gnb.domain.usecase.UseCaseResponse
import com.gnb.domain.usecase.mapListTo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetAndSaveRatesUseCaseTest{

    @RelaxedMockK
    private lateinit var ratesRepository: RatesRepository

    lateinit var getAndSaveRatesUseCase: GetAndSaveRatesUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getAndSaveRatesUseCase = GetAndSaveRatesUseCase(ratesRepository)
    }

    @Test
    fun `when use case called get remote rates is called`() = runBlocking {
        //When
        getAndSaveRatesUseCase.getAndSaveRates()

        //Then
        coVerify { ratesRepository.getRemoteRates() }
    }
}