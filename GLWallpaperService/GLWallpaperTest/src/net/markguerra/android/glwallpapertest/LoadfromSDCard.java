package net.markguerra.android.glwallpapertest;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.locks.Lock;

import org.xml.sax.Parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class LoadfromSDCard implements  Runnable{

	private final int C_QUEUE_SIZE = 5;
	private int m_width =320;
	private int m_height =240;
	private String C_DIR_PATH = "/football/";
	private static Queue<Bitmap> image_queue = new LinkedList<Bitmap>();
	private static Vector<String> file_name = 	new Vector<String>();
	private int image_index = 0;
	
	public void LoadfromSDCard(){
		
		reading_files();
	
	}
	public void reScanDirectory()
	{
		
		new Thread(new Runnable() {
		    public void run() {
		    	reading_files();
		    }
		  }).start();
	}
	public void startThread(int width,int height){
		m_width = width;
		m_height =  height;
		run();
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(image_queue.size()<C_QUEUE_SIZE){
			
			if(image_index > file_name.size())
				image_index = 0;
			
			String path = Environment.getExternalStorageDirectory()+ file_name.elementAt(image_index);
			File imgFile = new File(path);
	        if(imgFile.exists())
	        {
	        	
	        	Bitmap b = ShrinkBitmap(imgFile.getAbsolutePath(), m_width/2, m_height/2);
	        	image_queue.add(b);
	        	Log.d("DEBUG", "DEBUG image loaded through thread at buffer" + b.toString());
	        	b.recycle();
	        }
	        image_index++;
			
			
			
		}
	}
	
	private Bitmap ShrinkBitmap(String file, int width, int height){
		  
	     BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	        bmpFactoryOptions.inJustDecodeBounds = true;
	        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	        
	        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
	        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
	        
	        if (heightRatio > 1 || widthRatio > 1)
	        {
	         if (heightRatio > widthRatio)
	         {
	          bmpFactoryOptions.inSampleSize = heightRatio;
	         } else {
	          bmpFactoryOptions.inSampleSize = widthRatio; 
	         }
	        }
	        
	        bmpFactoryOptions.inJustDecodeBounds = false;
	        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	     return bitmap;
	    }
	
	private void reading_files()
	{
		
		
		Vector<String> file_nameTemp = 	new Vector<String>();
		int i = 0;
		File sdCardRoot = Environment.getExternalStorageDirectory();
		File yourDir = new File(sdCardRoot,C_DIR_PATH );
		for (File f : yourDir.listFiles()) 
		{
			if (f.isFile())
			{
				file_nameTemp.add(f.getName());
				Log.d("DEBUG", "DEBUG image found through thread "+f.getName());
			}
		}
		LoadfromSDCard.file_name.clear();
		LoadfromSDCard.file_name = file_nameTemp;
		/*for ( int j = 0; j<i; j++)
		{
		Log.d("*#DEBUG"," File "+j+" is "+ file_name[j]);
		}*/
	}
	

}
