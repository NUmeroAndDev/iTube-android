package com.numero.itube.model

data class Channel(
        val id: ChannelId,
        val title: String
)

inline class ChannelId(val value: String)