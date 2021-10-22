package com.acadia.punk.domain.model.mapper

interface Mapper<in T, out R> {
    fun mapToModel(from: T): R
}
