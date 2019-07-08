/**
 * 
 */
package com.monk.util.network.tcp.netty;

import java.io.IOException;
import java.util.Properties;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author huangguanlin
 *
 * 2019年7月5日
 */
public class ServerNetworkNetty {
	public static final ServerNetworkNetty instance = new ServerNetworkNetty();

	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;

	private final ServerBootstrap boot;

	public ServerNetworkNetty() {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(10);
		boot = new ServerBootstrap();
		boot.group(bossGroup, workerGroup);
		boot.channel(NioServerSocketChannel.class);
		ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decoder", new Decode());
				pipeline.addLast("encoder", new Encode());
				pipeline.addLast(new ServerNettyHandler());
			}

			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
				super.exceptionCaught(ctx, cause);
			}
		};
		boot.childHandler(initializer);
		boot.option(ChannelOption.SO_KEEPALIVE, true);
		boot.option(ChannelOption.TCP_NODELAY, true);
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/conf.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int port = Integer.valueOf(properties.getProperty("port"));
		try {
			boot.bind(port).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("tcp服务器已启动");
	}

	public static void main(String[] args) {

	}
}
