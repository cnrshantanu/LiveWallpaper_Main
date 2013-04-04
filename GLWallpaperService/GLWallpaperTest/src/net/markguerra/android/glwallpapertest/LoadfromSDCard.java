package net.markguerra.android.glwallpapertest;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import org.xml.sax.Parser;

import android.graphics.Bitmap;
import android.os.Environment;

public class LoadfromSDCard implements  Runnable{

	private Queue<Bitmap> image_queue = new LinkedList<Bitmap>();
	private Queue<String> file_name = 	new LinkedList<String>();
	
	public void LoadfromSDCard(){
		
		
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	private void reading_files()
	{
		
		int i = 0;
		File sdCardRoot = Environment.getExternalStorageDirectory();
		File yourDir = new File(sdCardRoot, "/football/");
		for (File f : yourDir.listFiles()) 
		{
			if (f.isFile())
			{
				file_name.add(f.getName());
				
			}
		}
		

		/*for ( int j = 0; j<i; j++)
		{
		Log.d("*#DEBUG"," File "+j+" is "+ file_name[j]);
		}*/
	}
	

}
