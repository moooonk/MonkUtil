/**
 * 
 */
package com.monk.util.network.tcp.netty;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import javax.net.SocketFactory;

/**
 * @author huangguanlin
 *
 * 2019年7月5日
 */
public class TcpClient {
	public static final TcpClient instance = new TcpClient();

	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	public TcpClient() {
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/conf.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int port = Integer.valueOf(properties.getProperty("port"));
		try {
			socket = SocketFactory.getDefault().createSocket("127.0.0.1", port);
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMsg(byte[] msg) {
		try {
			outputStream.write(msg);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readMsg() {
		byte[] msg = new byte[1024];
		try {
			int length = inputStream.read(msg);
			System.out.println(new String(msg, 0, length, "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			outputStream.close();
			inputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TcpClient.instance.sendMsg("hahaha".getBytes());
		TcpClient.instance.readMsg();
		TcpClient.instance.close();
	}

}
