/**
 * 
 */
package com.monk.util.network.tcp.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author huangguanlin
 *
 * 2019年7月5日
 */
public class Decode extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int length = in.readableBytes();
		if (length <= 0) {
			return;
		}
		byte[] temp = new byte[length];
		in.readBytes(temp);
		out.add(temp);
	}

}
