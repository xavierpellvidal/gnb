package com.gnb.data.datasource.local

import com.gnb.data.datasource.DataSourceResponse
import com.gnb.data.datasource.IRatesDataSource
import com.gnb.data.provider.local.dao.RateDao
import com.gnb.domain.entity.local.RateEntity
import javax.inject.Inject

class RatesLocalDataSource @Inject constructor(
    private val ratesDao: RateDao
    ): IRatesDataSource.IRatesLocalDataSource{

    override suspend fun getRates(): DataSourceResponse<MutableList<RateEntity>> {
        val rates = ratesDao.getRates()
        return if(rates.isEmpty()) DataSourceResponse.NoData
        else DataSourceResponse.Success(rates.toMutableList())
    }

    override suspend fun insertRates(data: MutableList<RateEntity>) {
        ratesDao.insertAll(data)
    }

    override suspend fun clearRates() {
        ratesDao.clear()
    }

    override suspend fun insertRate(data: RateEntity){
        ratesDao.insert(data)
    }

}