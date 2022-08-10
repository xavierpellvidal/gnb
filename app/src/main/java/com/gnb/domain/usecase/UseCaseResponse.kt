package com.gnb.domain.usecase

import com.gnb.domain.entity.IConvertibleTo

sealed class UseCaseResponse<out T : Any> {
    class Success<T : Any>(val data: T) : UseCaseResponse<T>()
    object NoData : UseCaseResponse<Nothing>()
    object NoNetwork : UseCaseResponse<Nothing>()
    object GenericError : UseCaseResponse<Nothing>()
}

/**
 * Let convert MutableList data of the sealed class calling convertTo() function of each element.
 */
inline fun <reified O : Any, reified I : IConvertibleTo<O>> UseCaseResponse<MutableList<I>>.mapListTo(): UseCaseResponse<MutableList<O>> {
    return when (this) {
        is UseCaseResponse.Success -> {
            UseCaseResponse.Success(this.data.mapNotNull {
                it.convertTo()
            }.toMutableList())
        }
        is UseCaseResponse.NoData -> this
        is UseCaseResponse.NoNetwork -> this
        is UseCaseResponse.GenericError -> this
    }
}
