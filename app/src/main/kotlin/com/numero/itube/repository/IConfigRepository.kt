package com.numero.itube.repository

interface IConfigRepository {
    val isLoop: Boolean

    val apiKey: String
}