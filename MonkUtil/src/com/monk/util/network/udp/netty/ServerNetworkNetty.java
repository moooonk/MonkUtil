/**
 * 
 */
package com.monk.util.network.udp.netty;


import java.net.InetSocketAddress;

import com.monk.util.configreader.CoreConfig;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author huangguanlin
 * UDP Netty Server 本体 Demo
 * 2017年1月11日
 */
public class ServerNetworkNetty {
	public static final ServerNetworkNetty instance = new ServerNetworkNetty();
	private EventLoopGroup group;
	private Bootstrap boot;
	private int paksize = 1206;
	private Channel channel;
	private ServerNetworkNetty(){
		try {
			boot = initBootStrap();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 提供一个简易的Netty UDP服务器构造
	 */
	private Bootstrap initBootStrap() throws InterruptedException {
		Bootstrap b = new Bootstrap();
		group = new NioEventLoopGroup(1);
		b.group(group);
		b.channel(NioDatagramChannel.class);
		b.handler(new ServerNettyHandler());
		channel = b.bind(CoreConfig.instance.getConfig().port()).sync().channel();
		return b;
	}
	
	public void init(){
		
	}

	public void sendpak(InetSocketAddress ip, byte[] data) {
		ByteBuf buf = Unpooled.buffer(paksize);
		buf.writeBytes(data);
		channel.writeAndFlush(new DatagramPacket(buf, ip));
	}
}
