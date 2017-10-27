/**
 * 
 */
package com.monk.util.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author huangguanlin
 *
 * 2017年10月27日
 */
public interface UserService extends Remote{
	public User getUserById(String id)throws RemoteException;
}
