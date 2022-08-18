package com.gnb.domain.usecase.transaction

import com.gnb.data.repository.TransactionsRepository
import com.gnb.domain.entity.local.TransactionEntity
import com.gnb.domain.repository.RepositoryResponse
import com.gnb.domain.usecase.UseCaseResponse
import com.gnb.domain.usecase.mapListTo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class GetProductsUseCaseTest{
    @MockK
    private lateinit var transactionsRepository: TransactionsRepository

    lateinit var getProductsUseCase: GetProductsUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getProductsUseCase = GetProductsUseCase(transactionsRepository)
    }

    @Test
    fun `when local DataSource response is success with empty data return response NoData`() = runBlocking {
        //Given
        coEvery {
            transactionsRepository.getTransactions()
        } returns RepositoryResponse.Success(emptyList<TransactionEntity>().toMutableList())

        //When
        val response = getProductsUseCase.getProducts()

        //Then
        assertTrue(response is UseCaseResponse.NoData)
    }

    @Test
    fun `when local DataSource response is success with data return response Success with mapped list`() = runBlocking {
        //Given
        val rateList = mutableListOf(TransactionEntity("E12564", "1.36", "EUR"))
        coEvery {
            transactionsRepository.getTransactions()
        } returns RepositoryResponse.Success(rateList)

        //When
        val response = getProductsUseCase.getProducts()

        //Then
        assertTrue(response is UseCaseResponse.Success)
        assertEquals((response as UseCaseResponse.Success).data[0].sku, (UseCaseResponse.Success(rateList).mapListTo() as UseCaseResponse.Success).data[0].sku)
        assertEquals(response.data[0].amount.toInt(), rateList.size)
    }

    @Test
    fun `when local DataSource response is NoData return response NoData`() = runBlocking {
        //Given
        coEvery {
            transactionsRepository.getTransactions()
        } returns RepositoryResponse.NoData

        //When
        val response = getProductsUseCase.getProducts()

        //Then
        assertTrue(response is UseCaseResponse.NoData)
    }

    @Test
    fun `when local DataSource response is NoNetwork return response NoNetwork`() = runBlocking {
        //Given
        coEvery {
            transactionsRepository.getTransactions()
        } returns RepositoryResponse.NoNetwork

        //When
        val response = getProductsUseCase.getProducts()

        //Then
        assertTrue(response is UseCaseResponse.NoNetwork)
    }

    @Test
    fun `when local DataSource response is GenericError return response GenericError`() = runBlocking {
        //Given
        coEvery {
            transactionsRepository.getTransactions()
        } returns RepositoryResponse.GenericError

        //When
        val response = getProductsUseCase.getProducts()

        //Then
        assertTrue(response is UseCaseResponse.GenericError)
    }
}