package com.bfd.job.utils.files;

import org.apache.log4j.Logger;

/**
 * @author: BFD474
 *
 * @description: 
 */
public class HdfsUtilsImpl implements FileUtils {
	
	private static final Logger logger = Logger.getLogger(HdfsUtilsImpl.class);
	
	public static void readHDFS(){
		String path = HdfsUtilsImpl.class.getClassLoader().getResource(CORE_SITE_XML).getPath();
		System.out.println(path);
	}
	
	public static void main(String[] args){
		HdfsUtilsImpl.readHDFS();
	}

}
