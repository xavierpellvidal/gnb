package com.gnb.di

import android.content.Context
import androidx.room.Room
import com.gnb.data.provider.local.LocalDatabase
import com.gnb.data.provider.local.dao.RateDao
import com.gnb.data.provider.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): LocalDatabase {
        return Room.databaseBuilder(appContext, LocalDatabase::class.java, "gnb.db").build()
    }

    @Provides
    fun provideRateDao(appDatabase: LocalDatabase): RateDao {
        return appDatabase.rateDao()
    }

    @Provides
    fun provideTransactionDao(appDatabase: LocalDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

}