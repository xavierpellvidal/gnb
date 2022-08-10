package com.gnb.domain.repository

import com.gnb.domain.entity.local.RateEntity

interface IRatesRepository {

    suspend fun getRemoteRates(): RepositoryResponse<MutableList<RateEntity>>

    suspend fun getLocalRates(): RepositoryResponse<MutableList<RateEntity>>

    suspend fun insertRates(rates: MutableList<RateEntity>)

    suspend fun insertRate(rate: RateEntity)

    suspend fun clearRates()

}