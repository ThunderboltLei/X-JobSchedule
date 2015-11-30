package com.bfd.job.utils.files;

/**
 * @author: BFD474
 *
 * @description: 
 */
public class HdfsUtilsImpl implements FileUtils {
	
	public static void readHDFS(){
		String path = HdfsUtilsImpl.class.getClassLoader().getResource(CORE_SITE_XML).getPath();
		System.out.println(path);
	}
	
	public static void main(String[] args){
		HdfsUtilsImpl.readHDFS();
	}

}
