package com.jtao.server.spring.boot.stater.netty.tcp

import io.netty.channel.Channel
import org.springframework.stereotype.Component

@Component
open class TcpRepository {
    private val map = HashMap<String, Channel>()

    fun add(channelKey:String, channel: Channel) = map.put(channelKey, channel)
    fun delete(channelKey: String) = map.remove(channelKey)
    fun find(channelKey: String) = map[channelKey]
    fun findAll() = map.values
    fun size() = map.size
}