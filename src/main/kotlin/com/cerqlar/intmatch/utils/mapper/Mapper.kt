package com.cerqlar.intmatch.utils.mapper

/**
 * Created by chinnku on Aug, 2021
 * Mapper Interface
 */
interface Mapper<D, M> {
    fun fromModel(model: M): D
    fun toModel(domain: D): M
}