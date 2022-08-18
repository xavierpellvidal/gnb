package com.gnb.domain.usecase.transaction

import com.gnb.data.repository.RatesRepository
import com.gnb.data.repository.TransactionsRepository
import com.gnb.domain.entity.local.RateEntity
import com.gnb.domain.entity.local.TransactionEntity
import com.gnb.domain.repository.RepositoryResponse
import com.gnb.domain.usecase.UseCaseResponse
import com.gnb.domain.usecase.mapListTo
import com.gnb.domain.usecase.rate.GetRatesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

internal class GetTransactionsUseCaseTest{

    @MockK
    private lateinit var transactionsRepository: TransactionsRepository

    lateinit var getTransactionsUseCase: GetTransactionsUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getTransactionsUseCase = GetTransactionsUseCase(transactionsRepository)
    }

    @Test
    fun `when local DataSource response is success with empty data return response NoData`() = runBlocking {
        //Given
        coEvery {
            transactionsRepository.getTransactionsBySku("")
        } returns RepositoryResponse.Success(emptyList<TransactionEntity>().toMutableList())

        //When
        val response = getTransactionsUseCase.getTransactions("")

        //Then
        assertTrue(response is UseCaseResponse.NoData)
    }

    @Test
    fun `when local DataSource response is success with data return response Success with mapped list`() = runBlocking {
        //Given
        val rateList = mutableListOf(TransactionEntity("E12564", "1.36", "EUR"))
        coEvery {
            transactionsRepository.getTransactionsBySku("")
        } returns RepositoryResponse.Success(rateList)

        //When
        val response = getTransactionsUseCase.getTransactions("")

        //Then
        assertTrue(response is UseCaseResponse.Success)
        assertEquals((response as UseCaseResponse.Success).data[0],
            (UseCaseResponse.Success(rateList).mapListTo() as UseCaseResponse.Success).data[0])
    }

    @Test
    fun `when local DataSource response is NoData return response NoData`() = runBlocking {
        //Given
        coEvery {
            transactionsRepository.getTransactionsBySku("")
        } returns RepositoryResponse.NoData

        //When
        val response = getTransactionsUseCase.getTransactions("")

        //Then
        Assert.assertTrue(response is UseCaseResponse.NoData)
    }

    @Test
    fun `when local DataSource response is NoNetwork return response NoNetwork`() = runBlocking {
        //Given
        coEvery {
            transactionsRepository.getTransactionsBySku("")
        } returns RepositoryResponse.NoNetwork

        //When
        val response = getTransactionsUseCase.getTransactions("")

        //Then
        Assert.assertTrue(response is UseCaseResponse.NoNetwork)
    }

    @Test
    fun `when local DataSource response is GenericError return response GenericError`() = runBlocking {
        //Given
        coEvery {
            transactionsRepository.getTransactionsBySku("")
        } returns RepositoryResponse.GenericError

        //When
        val response = getTransactionsUseCase.getTransactions("")

        //Then
        Assert.assertTrue(response is UseCaseResponse.GenericError)
    }

}