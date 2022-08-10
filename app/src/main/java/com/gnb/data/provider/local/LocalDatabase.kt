package com.gnb.data.provider.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gnb.data.provider.local.dao.RateDao
import com.gnb.data.provider.local.dao.TransactionDao
import com.gnb.domain.entity.local.RateEntity
import com.gnb.domain.entity.local.TransactionEntity

@Database(entities = [RateEntity::class, TransactionEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun rateDao(): RateDao
    abstract fun transactionDao(): TransactionDao
}