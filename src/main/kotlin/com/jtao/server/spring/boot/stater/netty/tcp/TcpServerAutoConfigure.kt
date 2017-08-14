package com.jtao.server.spring.boot.stater.netty.tcp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@ConditionalOnClass(TcpServer::class)
@EnableConfigurationProperties(TcpServerProperties::class)
open class TcpServerAutoConfigure {
    @Autowired
    lateinit var properties:TcpServerProperties

    @Bean
    @ConditionalOnMissingBean
    open fun tcpServer(): TcpServer {
        return  TcpServer(
                properties.tcpPort,
                properties.channelHandlers,
                properties.parentThread,
                properties.childThread,
                properties.optionMap,
                properties.childOptionMap,
                tcpRepository())
    }

    @Bean
    @ConditionalOnMissingBean
    open fun tcpRepository() = TcpRepository()
}