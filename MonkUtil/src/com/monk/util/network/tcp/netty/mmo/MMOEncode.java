/**
 * 
 */
package com.monk.util.network.tcp.netty.mmo;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author huangguanlin
 *
 * 2019年7月12日
 */
public class MMOEncode extends MessageToByteEncoder<ByteBuffer> {

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		super.flush(ctx);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuffer msg, ByteBuf out) throws Exception {
		out.writeBytes(msg);
	}

}
