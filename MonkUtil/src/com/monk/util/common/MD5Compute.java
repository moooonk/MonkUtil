package com.monk.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;

public class MD5Compute {

	/**
	 * 计算单个文件md5
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()){
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return fillMD5(bigInt.toString(16));
	}
	
	public static String fillMD5(String md5){
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }
	
	/**
	 * 计算整个文件夹的MD5
	 */
	public static Map<String, String> getDirMD5(File file, boolean listChild, Map<String, String> map) {
		if(!file.isDirectory()){
			return null;
		}
		String md5;
		File files[] = file.listFiles();
		for(int i = 0; i < files.length; i++){
			File f = files[i];
			if(f.isDirectory() && listChild){
				map.putAll(getDirMD5(f, listChild, map));
			} else {
				md5 = getFileMD5(f);
				if(md5 != null){
					map.put(f.getPath(), md5);
				}
			}
		}
		return map;
	}
	
}
