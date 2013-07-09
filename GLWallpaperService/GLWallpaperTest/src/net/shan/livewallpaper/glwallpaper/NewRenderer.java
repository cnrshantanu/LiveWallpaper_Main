/**
 * 
 */
package net.shan.livewallpaper.glwallpaper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.SlidingDrawer;
import android.widget.Toast;

/**
 * @author Shantanu Das
 *
 */
public class NewRenderer implements GLWallpaperService.Renderer {

	private static final int 	C_IMAGES_MAX 		= 4;
	private FontRenderer		m_font;
	private static final int 	C_FILE_NAME_MAX 	= 100;
	private static Square1[]  	mImage		 		= new Square1[C_IMAGES_MAX];
	private SquareNote			mNote;				
	private	static Boolean		m_init       		= false;
	private int 				m_width 			= 320;
	private int 				m_height 			= 180;
	String[] 					file_name 			= new String[C_FILE_NAME_MAX];
	public String[] 			folder_name 		= new String[C_FILE_NAME_MAX];
	private	int					image_index			= 0;
	private	int					m_image_indexPrev	= 0;
	private Resources 			resource;
	private Context				context;
	private  static String 		sourceFolder		= new String();
	private LoadfromSDCard		m_loader = new LoadfromSDCard();
		
	/** Constructor to set the handed over context */
	
	public NewRenderer(Resources r,Context _context){//
		
		this.resource = r;
		this.context = _context;
		
		// initialise the square
		//this.square = new Square1();
		
		for(int i=0;i<C_IMAGES_MAX;i++)
		{
			mImage[i] = new Square1();
		}
		
		//m_loader.reScanDirectory();
		m_image_indexPrev = image_index;
		Log.d("Debug","Folder Selected "+sourceFolder);
		reading_files(sourceFolder);
		
		for(int i = 0;i<C_IMAGES_MAX;i++)
		{
			if(file_name[image_index] == null)
			{
				if(file_name[image_index-1] != null)
				{
					mImage[i].m_filePath[0] = sourceFolder+file_name[image_index-1];
					mImage[i].m_filePath[1] = sourceFolder+file_name[0];
					//mImage[i].m_filePath[0] = "/football/"+file_name[image_index-1];
					//mImage[i].m_filePath[1] = "/football/"+file_name[0];
					image_index = 1;
				}
				else
				{
					image_index = 0;
					mImage[i].m_filePath[0] = sourceFolder+file_name[image_index];
					mImage[i].m_filePath[1] = sourceFolder+file_name[image_index+1];
					//mImage[i].m_filePath[0] = "/football/"+file_name[image_index];
					//mImage[i].m_filePath[1] = "/football/"+file_name[image_index+1];
					image_index += 2;
					continue;
				}
			}
			
			mImage[i].m_filePath[0] = sourceFolder+file_name[image_index];
			mImage[i].m_filePath[1] = sourceFolder+file_name[image_index+1];
			//mImage[i].m_filePath[0] = "/football/"+file_name[image_index];
			//mImage[i].m_filePath[1] = "/football/"+file_name[image_index+1];
			image_index+=2;
			
		}
		
		resource =r;
			
		mNote = new SquareNote(resource,context);
	}
	public void setSourceFolder(String chosenFolder)
	{
		sourceFolder = chosenFolder;
		reading_files(sourceFolder);
	}
	
	public void setTTLMax(int value)
	{
		Square1.C_TTL_MAX = value*1000;
		Log.d("DEBUG","DEBUG c ttl is "+Square1.C_TTL_MAX);
	}
	private int m_index = 0;
	private void loadImageThroughThread(final String filePath,final int index,final int tex_index,final GL10 gl)
	{
		 new Thread(new Runnable() {
			    public void run() {
			    	Log.d("DEBUG","image loaded start");
			        String path = Environment.getExternalStorageDirectory()+ filePath;
					File imgFile = new File(path);
			        if(imgFile.exists())
			        {
			          //  Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			        	
			        	Log.d("DEBUG", "DEBUG check time before shrinking i -" + m_index );
			        	Bitmap b = ShrinkBitmap(imgFile.getAbsolutePath(), m_width/2, m_height/2);
			        	Log.d("DEBUG", "DEBUG check time after shrinking i"+ m_index);
			        	NewRenderer:mImage[index].loadGLTexture(gl,b,tex_index);
			        	Log.d("DEBUG", "DEBUG check time loading texture i -"+ m_index);
			        	Log.d("DEBUG", "DEBUG image loaded through thread" + b.toString() + "string path" + filePath);
			        	b.recycle();
			        	m_index++;
			        }
			        try{
			        	
			        	Thread.sleep(50);
			        	Log.d("DEBUG","Thread slept");
			        }
			        catch(Exception e){
			        	Log.d("DEBUG","Cant make thread sleep sing a lullaby");
			        }
			    }
			  }).start();
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		// clear Screen and Depth Buffer
		for(int i=0;i<C_IMAGES_MAX;i++)
		{
			mImage[i].update();
			int temp = mImage[i].getChangeMyTextureStatus();
			if(temp != -1){
				Log.d("DEBUG","Image changed id :"+i+"texture :"+temp);
				image_index++;
				if(image_index >= 100)
					image_index = 0;
				if(file_name[image_index] == null)
				{
					image_index = 0;
				}
				mImage[i].m_filePath[temp] = sourceFolder+file_name[image_index];
				//mImage[i].m_filePath[temp] = "/football/"+file_name[image_index];
				loadImage(gl,mImage[i].m_filePath[temp],i,temp);
				loadImageThroughThread(mImage[i].m_filePath[temp],i,temp,gl);
				m_image_indexPrev++;
				//break;
			}
			
		}
		
		mNote.setQuadrant(1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		//gl.glClear( GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

		// Drawing
		gl.glTranslatef(0.0f, 0.0f, -6.1f);		// move 5 units INTO the screen
												// is the same as moving the camera 5 units away
//		gl.glScalef(0.5f, 0.5f, 0.5f);			// scale the square to 50% 
												// otherwise it will be too large
		int index = 0;
		gl.glPushMatrix();
		
		{
			//gl.glTranslatef(x,y,0);
			//mImage[index].draw(gl);
			gl.glTranslatef(1.5f,0f,0f);
			index++;
			mImage[index].draw(gl);
			gl.glTranslatef(0f,-2.5f,0f);
			index++;
			mImage[index].draw(gl);
			gl.glTranslatef(-1.5f,0f,0f);
			index++;
			mImage[index].draw(gl);
		}
		gl.glPopMatrix();
		
		
		
		mNote.Draw(gl);
		
        
		

	}
	//@Override
	/*public void onSurfaceDestroyed(GL10 gl, int width, int height){
		
	}*/
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}
		m_width = width;
		m_height = height;
		mNote.setDimensions(m_width, m_height);
		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix
		Log.d("DEBUG","Surface changed width"+width+" height "+height);
		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		//GLU.gluOrtho2D(gl, 0, width, 0, height);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}
    public void loadImage(GL10 gl,Bitmap bitmap,int index){
    	
    	 mImage[index].loadGLTexture(gl, bitmap);
         bitmap.recycle();
    }
	public void loadImage(GL10 gl,String file_path,int index){
		
		String path = Environment.getExternalStorageDirectory()+ file_path;
		File imgFile = new File(path);
        if(imgFile.exists())
        {
          //  Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap myBitmap = ShrinkBitmap(imgFile.getAbsolutePath(), m_width/2, m_height/2);
            Log.d("*#DEBUG"," *#DEBUG Got it baby");
            mImage[index].loadGLTexture(gl, myBitmap);
            myBitmap.recycle();
        }
        else                    
            Log.d("*#DEBUG","No such image exists");
        
	}
	
	public void loadImage(GL10 gl,String file_path,int index,int tex_index){
		
		String path = Environment.getExternalStorageDirectory()+ file_path;
		File imgFile = new File(path);
        if(imgFile.exists())
        {
          //  Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap myBitmap = ShrinkBitmap(imgFile.getAbsolutePath(), m_width/2, m_height/2);
            Log.d("*#DEBUG"," *#DEBUG Got it baby");
            mImage[index].loadGLTexture(gl, myBitmap,tex_index);
            myBitmap.recycle();
        }
        else                    
            Log.d("*#DEBUG","No such image exists");
        
	}
	

	
	public void loadImage(GL10 gl,String file_path1,String file_path2,int index){
		
		String path = Environment.getExternalStorageDirectory()+ file_path1;
		File imgFile = new File(path);
		Bitmap myBitmap1,myBitmap2;
        myBitmap1 = null;
        myBitmap2 = null;    		
		if(imgFile.exists())
        {
            myBitmap1 = ShrinkBitmap(imgFile.getAbsolutePath(), m_width/2, m_height/2);
            Log.d("*#DEBUG"," *#DEBUG Got image"+file_path1);
            //mImage[index].loadGLTexture(gl, myBitmap);
            
        }
        else                    
            Log.d("*#DEBUG",file_path1+" does not exists");
        
        
        path = Environment.getExternalStorageDirectory()+ file_path2;
		imgFile = new File(path);
        
        if(imgFile.exists())
        {
            myBitmap2 = ShrinkBitmap(imgFile.getAbsolutePath(), m_width/2, m_height/2);
            Log.d("*#DEBUG"," *#DEBUG Got image"+file_path1);
            //mImage[index].loadGLTexture(gl, myBitmap);
        }
        else                    
            Log.d("*#DEBUG",file_path1+" does not exists");
        
        if(myBitmap1 != null && myBitmap2 != null)
        	mImage[index].loadGLTexture(gl, myBitmap1,myBitmap2);
      
        if(myBitmap1 != null)
        	myBitmap1.recycle();
        if(myBitmap2 != null)
        	myBitmap2.recycle();
        
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
	
	
	private void listingFolders()
	{
		int i = 0;
		File sdCardRoot = Environment.getExternalStorageDirectory();
		File yourDir = new File(sdCardRoot, "/sdcard-ext/");
		for (File f : yourDir.listFiles()) 
		{
			if (f.isDirectory())
			{
				folder_name[i] = f.getName();
				i++;
			}
			//if(i == C_FILE_NAME_MAX)
				//break;
		}
		
		for ( int j = 0; j<i; j++)
		{
		Log.d("*#Saugat"," File "+j+" is "+ folder_name[j]);
		}	
	}
	private void reading_files(String folder)
	{
		
		int i = 0;
		File sdCardRoot = Environment.getExternalStorageDirectory();
		File yourDir = new File(sdCardRoot, folder);
		Log.d("Debug"," Folder being read is "+folder);
		for (File f : yourDir.listFiles()) 
		{
			if (f.isFile())
			{
				file_name[i] = f.getName();
				i++;
			}
			if(i == C_FILE_NAME_MAX)
				break;
		}
		for ( int j = 0; j<i; j++)
		{
		Log.d("*#DEBUG"," File "+j+" is "+ file_name[j]);
		}
	}
	
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Load the texture for the square
		//square.loadGLTexture(gl, this.resource,R.drawable.android2);
		Log.d("*#DEBUG","*#DEBUG surface created"+m_init);
		
		m_font = new FontRenderer(context, gl);
		
		try {
			m_font.LoadFont("TimesNewRoman.bff", gl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		//gl.glClearColor(0f, 0f, 0f, 0f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_ALPHA_TEST);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		m_init = true;
	
		for(int i = 0;i<C_IMAGES_MAX;i++)
		{
			loadImage(gl,mImage[i].m_filePath[0],mImage[i].m_filePath[1],i);
		}
		
		mNote.onSurfaceCreated(gl);
				        
   	}
	public void release(GL10 gl) {
		if(!m_init)
			return;
		
		for(int i=0;i<C_IMAGES_MAX;i++)
		{
			mImage[i].release(gl);
		}
		m_font.release(gl);
		image_index = m_image_indexPrev;
		m_init = false;
	}

	public void onSurfacePause(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		release(gl);
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float xValue = event.getRawX();
		float yValue = event.getRawY();
		if (event.getPointerCount()>1){
			
			mImage[0].startRotation();
			mImage[1].startRotation();
			mImage[2].startRotation();
			mImage[3].startRotation();
			
			return;
		}
		
		if (xValue <= m_width/2 && yValue <= m_height/2)
		{
			//Log.d("Change","1st quadrant");
			mImage[0].startRotation();
			mNote.onTouchEvent(event);
		}
		
		else if (xValue <= m_width/2 && yValue >= m_height/2)
		{
			//Log.d("Change","3rd quadrant");
			mImage[3].startRotation();
		}
		
		else if (xValue >= m_width/2 && yValue <= m_height/2)
		{
			//Log.d("Change","2nd quadrant");
			mImage[1].startRotation();
		}
		
		else
		{
			//Log.d("Change","4th quadrant");
			mImage[2].startRotation();
			
		}
		
		//mNote.onTouchEvent(event);
			
		
	}

}
