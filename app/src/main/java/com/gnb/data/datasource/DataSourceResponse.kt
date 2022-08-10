package com.gnb.data.datasource

import com.gnb.domain.entity.IConvertibleTo

sealed class DataSourceResponse<out T : Any> {

    class Success<T : Any>(val data: T) : DataSourceResponse<T>()
    object NoData : DataSourceResponse<Nothing>()
    object NoNetwork : DataSourceResponse<Nothing>()
    object GenericError : DataSourceResponse<Nothing>()
}

/**
 * Let convert MutableList data of the sealed class calling convertTo() function of each element.
 */
inline fun <reified O : Any, reified I : IConvertibleTo<O>> DataSourceResponse<MutableList<I>>.mapListTo(): DataSourceResponse<MutableList<O>> {
    return when (this) {
        is DataSourceResponse.Success -> {
            DataSourceResponse.Success(this.data.mapNotNull {
                it.convertTo()
            }.toMutableList())
        }
        is DataSourceResponse.NoData -> this
        is DataSourceResponse.NoNetwork -> this
        is DataSourceResponse.GenericError -> this
    }
}
