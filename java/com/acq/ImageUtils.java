package com.acq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

public class ImageUtils 
{

	public static boolean decode64AndSave(String base64Image,String location)
	{
		BASE64Decoder decoder= new BASE64Decoder();
		FileOutputStream out=null ;
		try
		{
		byte[] image = decoder.decodeBuffer(base64Image);
		File imageFile = new File(location);	
		if(imageFile.exists())
		imageFile.delete();
		imageFile.createNewFile();
		out= new FileOutputStream(imageFile);
		out.write(image);
		out.close();
		return true;
		}
		catch(IOException ioException)
		{
			return false;			
		}
		
	}
	
	public static boolean save(MultipartFile usrImage,String location)
	{	
		//System.out.println("zzzzzz111111111111111111");
		FileOutputStream out=null ;
		try
		{
		byte[] image = usrImage.getBytes();
		File imageFile = new File(location);
		if(imageFile.exists())
		imageFile.delete();
		imageFile.createNewFile();
		out= new FileOutputStream(imageFile);
		out.write(image);
		out.close();
		//System.out.println("zzzzz22222222222222222");
		return true;
		}
		catch(IOException ioException)
		{
			return false;
			
		}
	}
	
}