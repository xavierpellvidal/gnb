package com.gnb.di

import com.gnb.data.datasource.IRatesDataSource
import com.gnb.data.datasource.ITransactionsDataSource
import com.gnb.data.datasource.local.RatesLocalDataSource
import com.gnb.data.datasource.local.TransactionsLocalDataSource
import com.gnb.data.datasource.remote.RatesRemoteDataSource
import com.gnb.data.datasource.remote.TransactionsRemoteDataSource
import com.gnb.data.provider.local.LocalDatabase
import com.gnb.data.provider.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object DataSourceModule {

    @Provides
    fun provideRatesRemoteDataSource(apiService: ApiService) : IRatesDataSource.IRatesRemoteDataSource = RatesRemoteDataSource(apiService)

    @Provides
    fun provideTransactionsRemoteDataSource(apiService: ApiService) : ITransactionsDataSource.ITransactionsRemoteDataSource = TransactionsRemoteDataSource(apiService)

    @Provides
    fun provideRatesLocalDataSource(appDatabase: LocalDatabase) : IRatesDataSource.IRatesLocalDataSource = RatesLocalDataSource(appDatabase.rateDao())

    @Provides
    fun provideTransactionsLocalDataSource(appDatabase: LocalDatabase) : ITransactionsDataSource.ITransactionsLocalDataSource = TransactionsLocalDataSource(appDatabase.transactionDao())

}