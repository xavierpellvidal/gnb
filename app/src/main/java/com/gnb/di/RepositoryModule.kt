package com.gnb.di

import com.gnb.data.datasource.local.RatesLocalDataSource
import com.gnb.data.datasource.local.TransactionsLocalDataSource
import com.gnb.data.datasource.remote.RatesRemoteDataSource
import com.gnb.data.datasource.remote.TransactionsRemoteDataSource
import com.gnb.data.repository.RatesRepository
import com.gnb.data.repository.TransactionsRepository
import com.gnb.domain.repository.IRatesRepository
import com.gnb.domain.repository.ITransactionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object RepositoryModule {

    @Provides
    fun provideTransactionRepository(remoteDataSource: TransactionsRemoteDataSource, localDataSource: TransactionsLocalDataSource):
            ITransactionsRepository = TransactionsRepository(remoteDataSource, localDataSource)

    @Provides
    fun provideRatesRepository(remoteDataSource: RatesRemoteDataSource, localDataSource: RatesLocalDataSource):
            IRatesRepository = RatesRepository(remoteDataSource, localDataSource)

}