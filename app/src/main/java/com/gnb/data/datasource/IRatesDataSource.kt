package com.gnb.data.datasource

import com.gnb.domain.entity.local.RateEntity
import com.gnb.domain.entity.remote.RateModel

interface IRatesDataSource {

    interface IRatesRemoteDataSource {
        suspend fun getRates(): DataSourceResponse<MutableList<RateModel>>
    }

    interface IRatesLocalDataSource {
        suspend fun getRates(): DataSourceResponse<MutableList<RateEntity>>
        suspend fun insertRates(data: MutableList<RateEntity>)
        suspend fun insertRate(data: RateEntity)
        suspend fun clearRates()
    }

}