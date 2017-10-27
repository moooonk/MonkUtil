/**
 * 
 */
package com.monk.util.webService.client;

import java.net.URL;


/**
 * @author huangguanlin
 *
 * 2017年10月27日
 */
public class WebServiceDemoClient {

	public static void main(String[] args) throws Exception {
		String url = "http://%s:%s/WebServiceDemo";
        url = String.format(url, "0.0.0.0", 9734);
        WebServiceDemoServerService service = new WebServiceDemoServerService(new URL(url));
        WebServiceDemoServer server = service.getWebServiceDemoServerPort();
        long time = System.currentTimeMillis();
        for(int i = 0; i < 10000; i++){
        	server.getString(i + "");
        }
        System.out.println(System.currentTimeMillis() - time);
	}
}
