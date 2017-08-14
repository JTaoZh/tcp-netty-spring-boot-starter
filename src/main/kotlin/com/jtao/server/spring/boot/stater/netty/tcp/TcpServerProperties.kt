package com.jtao.server.spring.boot.stater.netty.tcp

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelOption
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("com.jtao.server.tcp")
open class TcpServerProperties(
        var tcpPort:Int,
        var channelHandlers:List<ChannelHandler>,
        var parentThread:Int,
        var childThread:Int,
        var optionMap: Map<ChannelOption<Any>, Any>,
        var childOptionMap:Map<ChannelOption<Any>, Any>) {
}