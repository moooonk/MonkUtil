/**
 * 
 */
package com.monk.util.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author huangguanlin
 *
 * 2017年10月27日
 */
public class UserServiceImpl extends UnicastRemoteObject  implements UserService{

	/**
	 * @throws RemoteException
	 */
	protected UserServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public User getUserById(String id) throws RemoteException {
		User user = new User();
		user.setId(id);
		return user;
	}

}
