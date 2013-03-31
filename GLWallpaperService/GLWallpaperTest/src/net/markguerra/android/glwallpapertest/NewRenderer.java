/**
 * 
 */
package net.markguerra.android.glwallpapertest;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

import android.R.string;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

/**
 * @author Shantanu Das
 *
 */
public class NewRenderer implements GLWallpaperService.Renderer {

	private static final int 	C_IMAGES_MAX = 4;
	private static Square1[]  			mImage		 = new Square1[C_IMAGES_MAX];
	private	static Boolean		m_init       = false;
	//private Square1 		square;		// the square
	private Resources 			resource;
		
	/** Constructor to set the handed over context */
	public NewRenderer(Resources r){//Context context) {
		this.resource = r;
		
		// initialise the square
		//this.square = new Square1();
		for(int i=0;i<C_IMAGES_MAX;i++)
		{
			mImage[i] = new Square1();
		}
		
		
		
    }
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// clear Screen and Depth Buffer
		for(int i=0;i<C_IMAGES_MAX;i++)
		{
			mImage[i].update();
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

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
			mImage[index].draw(gl);
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
		

	}
	//@Override
	/*public void onSurfaceDestroyed(GL10 gl, int width, int height){
		
	}*/
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		//GLU.gluOrtho2D(gl, 0, width, 0, height);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}
    
	public void loadImage(GL10 gl,String file_path,int index){
		
		String path = Environment.getExternalStorageDirectory()+ file_path;
		File imgFile = new File(path);
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());                  
            Log.d("*#DEBUG"," *#DEBUG Got it baby");
            mImage[index].loadGLTexture(gl, myBitmap);
            myBitmap.recycle();
        }
        else                    
            Log.d("*#DEBUG","No such image exists");
        
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Load the texture for the square
		//square.loadGLTexture(gl, this.resource,R.drawable.android2);
		Log.d("*#DEBUG","*#DEBUG surface created"+m_init);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		
		if(m_init)
			return;
		
		m_init = true;
	
		int index = 0;
		loadImage(gl,"/Hiromi/a.jpg",index);
		index++;
		loadImage(gl,"/Hiromi/b.jpg",index);
		index++;
		loadImage(gl,"/Hiromi/c.jpg",index);
		index++;
		loadImage(gl,"/Hiromi/d.jpg",index);
		        
   	}
	public void release() {
		if(!m_init)
			return;
		
	}

	public void onSurfacePause(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		if(!m_init)
			return;
		
		Log.d("*#DEBUG","*#DEBUG surface DESTROYED");
		release();
		Log.d("*#DEBUG","*#DEBUG And i am released");
		for(int i=0;i<C_IMAGES_MAX;i++)
		{
			mImage[i].release(gl);
		}
		m_init = false;
	}

}
