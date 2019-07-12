/**
 * 
 */
package com.monk.util.network.tcp.netty.mmo.request;

import java.nio.ByteBuffer;

/**
 * @author huangguanlin
 *
 * 2019年7月12日
 */
public class ServerRequest {

	private short headLength;
	private short sequence;
	private byte flags;
	private byte checksum;

	private short contentLength;
	private short code;
	private byte[] content;

	public ServerRequest(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		headLength = buffer.getShort(0);
		sequence = buffer.getShort(2);
		flags = buffer.get(4);
		checksum = buffer.get(5);
		contentLength = buffer.getShort(6);
		code = buffer.getShort(8);
		content = new byte[contentLength - 2];
		buffer.get(content, 10, content.length);
	}

	public short getHeadLength() {
		return headLength;
	}

	public short getSequence() {
		return sequence;
	}

	public byte getFlags() {
		return flags;
	}

	public byte getChecksum() {
		return checksum;
	}

	public short getContentLength() {
		return contentLength;
	}

	public byte[] getContent() {
		return content;
	}

	public short getCode() {
		return code;
	}

}
