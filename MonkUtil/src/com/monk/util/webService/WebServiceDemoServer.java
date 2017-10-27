/**
 * 
 */
package com.monk.util.webService;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author huangguanlin
 *
 * 2017年10月27日
 */
@WebService
public class WebServiceDemoServer {
	@WebMethod
	public String getString(String str){
		return str;
	}
}
