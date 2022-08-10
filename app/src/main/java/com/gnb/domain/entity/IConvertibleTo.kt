package com.gnb.domain.entity

interface IConvertibleTo<T> {

    fun convertTo(): T?

}