/**
 * 
 */
package com.monk.util.rmi;

import java.rmi.Naming;

/**
 * @author huangguanlin
 *
 * 2017年10月27日
 */
public class RmiClientStarter {
	public static void main(String[] args){    
        try{    
            //调用远程对象，注意RMI路径与接口必须与服务器配置一致    
            UserService userService = (UserService)Naming.lookup("rmi://127.0.0.1:6600/userService");  
            long time = System.currentTimeMillis();
            for(int i = 0; i < 10000; i++){
            	User user = userService.getUserById(i+"");  
            }
            System.out.println(System.currentTimeMillis() - time);
        }catch(Exception ex){    
            ex.printStackTrace();    
        }    
    } 
}
