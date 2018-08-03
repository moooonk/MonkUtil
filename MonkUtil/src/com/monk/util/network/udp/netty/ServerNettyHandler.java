/**
 * 
 */
package com.monk.util.network.udp.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * @author huangguanlin
 * UDP Netty Server 接收处理类 Demo
 * 2017年1月11日
 */
public class ServerNettyHandler extends SimpleChannelInboundHandler<DatagramPacket>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf = (ByteBuf) msg.copy().content();
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		buf.release();
		System.out.println(new String(req, "UTF-8"));
		if(checkRequest(req)){
			ServerScheduled.instance.getExecutor().schedule(new JobForResponse(msg.sender()), 0, TimeUnit.SECONDS);
		}
		
	}
	
	private boolean checkRequest(byte[] data) throws Exception {
		if (data == null)
			return false;
		char check[] = { 0x30, 0x2e, 0x30, 0x2e, 0x30, 0x2e, 0x30, 0x3a, 0x30 };
		String che = new String(check);
		String str = new String(data,2, 9, "US-ASCII");
		if (che.equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	
	class JobForResponse implements Runnable {
		private InetSocketAddress source;

		public JobForResponse(InetSocketAddress inetSocketAddress) {
			this.source = inetSocketAddress;
		}

		public void run() {
			// TODO 自定义异步处理类
		}
	}
}
