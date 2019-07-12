/**
 * 
 */
package com.monk.util.network.tcp.netty.mmo;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author huangguanlin
 *
 * 2019年7月12日
 */
public class MMODecode extends ByteToMessageDecoder {

	private static final int HEAD_LEN = 6;
	private static final int CONTENT_SIZE_FLAG_LEN = 2;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < HEAD_LEN + CONTENT_SIZE_FLAG_LEN) {
			return;
		}
		in.markReaderIndex();
		short length = in.getShort(HEAD_LEN);
		if (in.readableBytes() < HEAD_LEN + CONTENT_SIZE_FLAG_LEN + length) {
			in.resetReaderIndex();
			return;
		}
		byte[] temp = new byte[length];
		in.readBytes(temp);
		out.add(temp);
	}

}
