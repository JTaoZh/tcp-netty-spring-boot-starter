package com.jtao.server.spring.boot.stater.netty.tcp

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory

class TcpServer(val tcpPort:Int,
                val channelHandlers: List<ChannelHandler>,
                val parentThread:Int,
                val childThread:Int,
                val optionMap: Map<ChannelOption<Any>, Any>,
                val childOptionMap:Map<ChannelOption<Any>, Any>,
                val tcpRepository: TcpRepository) {

    val logger = LoggerFactory.getLogger(TcpServer::class.java)!!

    init{
        val parentGroup = NioEventLoopGroup(parentThread)
        val childGroup = NioEventLoopGroup(childThread)
        val b = ServerBootstrap().group(parentGroup, childGroup)
                .channel(NioServerSocketChannel::class.java)
                .childHandler(object :ChannelInitializer<SocketChannel>(){
                    override fun initChannel(ch: SocketChannel?) {
                        val pipeline = ch!!.pipeline()
                        channelHandlers.forEach {
                            pipeline.addLast(it)
                        }
                    }
                })
        channelHandlers.forEach { logger.info("handlers: ${it::class.java.typeName }}") }
        optionMap.forEach { channelOption, value -> b.option(channelOption, value) }
        childOptionMap.forEach { channelOption, value -> b.childOption(channelOption, value) }
        val cf = b.bind(tcpPort).sync()
        logger.info("Tcp Server running on $tcpPort.")
        cf.channel().closeFuture().sync()



        childGroup.shutdownGracefully()
        parentGroup.shutdownGracefully()
    }

    fun pushMsg(channelKey:String, msg:ByteArray){
        tcpRepository.find(channelKey)?.writeAndFlush(Unpooled.wrappedBuffer(msg))
    }

    fun pushAllMsg(msg:ByteArray){
        tcpRepository.findAll().forEach { it.writeAndFlush(Unpooled.wrappedBuffer(msg)) }
    }
}