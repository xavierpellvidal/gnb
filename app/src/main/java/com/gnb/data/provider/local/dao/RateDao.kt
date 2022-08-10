package com.gnb.data.provider.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gnb.domain.entity.local.RateEntity

@Dao
interface RateDao {

    @Query("SELECT * FROM rates")
    suspend fun getRates(): List<RateEntity>

    @Query("SELECT * FROM rates WHERE `from` LIKE :from AND `to` LIKE :to")
    suspend fun getRate(from: String, to: String): RateEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<RateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rates: RateEntity)

    @Query("DELETE FROM rates")
    suspend fun clear()
}