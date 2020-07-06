package com.exemple.thecatapi.Api.Model

data class Cat (
    val breeds: List<Any>?,
    val categories: List<Categories>?,
    val id: String?,
    val url: String?,
    val width: Number?,
    val height: Number?,
    var favStatus: String
)