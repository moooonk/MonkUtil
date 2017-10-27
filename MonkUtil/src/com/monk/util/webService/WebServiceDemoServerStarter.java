/**
 * 
 */
package com.monk.util.webService;

import javax.xml.ws.Endpoint;


/**
 * @author huangguanlin
 *
 * 2017年10月27日
 */
public class WebServiceDemoServerStarter {
	public static void main(String[] args) {
        String address = String.format("http://0.0.0.0:%d/WebServiceDemo", 9734);
        Endpoint.publish(address, new WebServiceDemoServer());
		System.out.println("启动完毕");
	}
}
