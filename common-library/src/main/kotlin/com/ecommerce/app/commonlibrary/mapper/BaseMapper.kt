package com.ecommerce.app.commonlibrary.mapper

import org.mapstruct.MappingTarget

interface BaseMapper<M, V> {

    fun toModel(vm: V): M

    fun toVm(m: M): V

    fun partialUpdate(@MappingTarget m: M, v: V)
}
