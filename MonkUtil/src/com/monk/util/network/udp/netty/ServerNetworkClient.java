/**
 * 
 */
package com.monk.util.network.udp.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.TimeUnit;


/**
 * @author huangguanlin
 * UDP Client Demo
 * 2016年12月23日
 */
public class ServerNetworkClient extends Thread {
	public static ServerNetworkClient instance = new ServerNetworkClient();

	private DatagramChannel channel;
	private int paksize = 1206;

	private ServerNetworkClient() {
		try {
			channel = DatagramChannel.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			channel.socket().bind(new InetSocketAddress(30002));
			System.out.println("开始网络接收线程，监听端口" + 30002);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			SocketAddress source = recpak(paksize);
			ServerScheduled.instance.getExecutor().schedule(new JobForResponse(source), 0, TimeUnit.SECONDS);
		}
	}

	private SocketAddress recpak(int size) {
		SocketAddress source = null;
		try {
			ByteBuffer buf = ByteBuffer.allocate(size);
			buf.clear();
			source = channel.receive(buf);
			System.out.println("接受到消息");
			buf.flip();
			for(byte b : buf.array()){
				System.out.print((int)b + " ");
			}
			System.out.println();
			if (checkRequest(buf)) {
				return source;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private boolean checkRequest(ByteBuffer data) throws Exception {
		if (data == null)
			return false;
		char check[] = { 0x30, 0x2e, 0x30, 0x2e, 0x30, 0x2e, 0x30, 0x3a, 0x30 };
		String che = new String(check);
		String str = new String(data.array(), data.arrayOffset() + 2, 9,
				"US-ASCII");
		if (che.equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	public void sendpak(SocketAddress ip, byte[] data) {
		ByteBuffer sdatabuf = ByteBuffer.allocate(paksize);
		sdatabuf.clear();
		sdatabuf.put(data);
		sdatabuf.flip();
		try {
			channel.send(sdatabuf, ip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class JobForResponse implements Runnable {
		private SocketAddress source;

		public JobForResponse(SocketAddress source) {
			this.source = source;
		}

		public void run() {
			// TODO Auto-generated method stub
			
		}
	}

}
