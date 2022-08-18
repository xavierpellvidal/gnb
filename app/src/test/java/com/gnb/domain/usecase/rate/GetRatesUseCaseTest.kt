package com.gnb.domain.usecase.rate

import com.gnb.data.repository.RatesRepository
import com.gnb.domain.entity.local.RateEntity
import com.gnb.domain.repository.RepositoryResponse
import com.gnb.domain.usecase.UseCaseResponse
import com.gnb.domain.usecase.mapListTo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetRatesUseCaseTest{

    @MockK
    private lateinit var ratesRepository: RatesRepository

    lateinit var getRatesUseCase: GetRatesUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getRatesUseCase = GetRatesUseCase(ratesRepository)
    }

    @Test
    fun `when local DataSource response is success with empty data return response NoData`() = runBlocking {
        //Given
        coEvery {
            ratesRepository.getLocalRates()
        } returns RepositoryResponse.Success(emptyList<RateEntity>().toMutableList())

        //When
        val response = getRatesUseCase.getRates()

        //Then
        assertTrue(response is UseCaseResponse.NoData)
    }

    @Test
    fun `when local DataSource response is success with data return response Success with mapped list`() = runBlocking {
        //Given
        val rateList = mutableListOf(RateEntity("EUR", "USD", "1.23"))
        coEvery {
            ratesRepository.getLocalRates()
        } returns RepositoryResponse.Success(rateList)

        //When
        val response = getRatesUseCase.getRates()

        //Then
        assertTrue(response is UseCaseResponse.Success)
        assertEquals((response as UseCaseResponse.Success).data[0],
            (UseCaseResponse.Success(rateList).mapListTo() as UseCaseResponse.Success).data[0])
    }

    @Test
    fun `when local DataSource response is NoData return response NoData`() = runBlocking {
        //Given
        coEvery {
            ratesRepository.getLocalRates()
        } returns RepositoryResponse.NoData

        //When
        val response = getRatesUseCase.getRates()

        //Then
        assertTrue(response is UseCaseResponse.NoData)
    }

    @Test
    fun `when local DataSource response is NoNetwork return response NoNetwork`() = runBlocking {
        //Given
        coEvery {
            ratesRepository.getLocalRates()
        } returns RepositoryResponse.NoNetwork

        //When
        val response = getRatesUseCase.getRates()

        //Then
        assertTrue(response is UseCaseResponse.NoNetwork)
    }

    @Test
    fun `when local DataSource response is GenericError return response GenericError`() = runBlocking {
        //Given
        coEvery {
            ratesRepository.getLocalRates()
        } returns RepositoryResponse.GenericError

        //When
        val response = getRatesUseCase.getRates()

        //Then
        assertTrue(response is UseCaseResponse.GenericError)
    }

}