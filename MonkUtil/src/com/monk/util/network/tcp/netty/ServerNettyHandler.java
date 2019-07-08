/**
 * 
 */
package com.monk.util.network.tcp.netty;

import java.nio.ByteBuffer;

import com.monk.util.network.ServerScheduled;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author huangguanlin
 *
 * 2019年7月5日
 */
public class ServerNettyHandler extends SimpleChannelInboundHandler<byte[]> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		ServerScheduled.instance.getExecutor().execute(new JobForResponse(ctx.channel(), msg));
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		// 建立连接的时候的处理
		System.out.println("新连接加入");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		// 移除连接的时候的处理
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		// 异常处理
		cause.printStackTrace();
	}

	class JobForResponse implements Runnable {
		private byte[] msg;
		private Channel channel;

		public JobForResponse(Channel channel, byte[] msg) {
			this.channel = channel;
			this.msg = msg;
		}

		public void run() {
			try {
				run0();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		private void run0() throws Exception {
			System.out.println(new String(msg, "utf-8"));
			// 回复函数
			response(ByteBuffer.wrap("ok".getBytes()));
		}

		private void response(ByteBuffer buffer) {
			channel.writeAndFlush(buffer);
		}
	}

}
