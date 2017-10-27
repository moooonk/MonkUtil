/**
 * 
 */
package com.monk.util.rmi;

import java.io.Serializable;

/**
 * @author huangguanlin
 *
 * 2017年10月27日
 */
public class User implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4597732916995814958L;
	
	private String id;  
    private String name;  
    private int age;  
  
      
  
    public String getId() {  
        return id;  
    }  
  
    public void setId(String id) {  
        this.id = id;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
  
    }  
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }  
}
