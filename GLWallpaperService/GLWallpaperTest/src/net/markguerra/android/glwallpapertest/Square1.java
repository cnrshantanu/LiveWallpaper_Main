/**
 * 
 */
package net.markguerra.android.glwallpapertest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.Display;

/**
 * @author impaler
 *
 */
public class Square1 {
	
	private static final int C_REVOLUTION_VELOCITY_MAX = 2;
	private static final int C_REVOLUTION_VELOCITY_MIN = 1;
	private static final int C_TTL_MAX = 8000;
	private static final int C_TTL_MIN = 12000;
	
	private static float m_imageW 		= 40;
	private static float m_imageH 		= 30;
	private long  time_start 	= 0;
	private int   ttl 			= 0;
	private boolean m_rotate 	= false;
	private float revolution_angle;
	private float revolution_velocity;
	
			
		
	private FloatBuffer vertexBuffer;	// buffer holding the vertices
	private float vertices_frontface[] = {
			-1.5f,  0.0f,  0.0f,		// V1 - bottom left
			-1.5f,  2.5f,  0.0f,		// V2 - top left
			 0.0f,  0.0f,  0.0f,		// V3 - bottom right
			 0.0f,  2.5f,  0.0f,		// V4 - top right
	};
	
	private float vertices_rightface[] = {
			 0.0f,  0.0f,  0.0f,		// V1 - bottom left
			 0.0f,  2.5f,  0.0f,		// V2 - top left
			 0.0f,  0.0f, -0.2f,		// V3 - bottom right
			 0.0f,  2.5f, -0.2f,		// V4 - top right
	};	
	private float vertices_backface[] = {
			 0.0f, 0.0f,  -0.2f,		// V1 - bottom left
			 0.0f, 2.5f,  -0.2f,		// V2 - top left
			-1.5f, 0.0f,  -0.2f,		// V3 - bottom right
			-1.5f, 2.5f,  -0.2f,		// V4 - top right
	};
	
	private float vertices_leftface[] = {
			-1.5f, 0.0f,  -0.2f,		// V3 - bottom right
			-1.5f, 2.5f,  -0.2f,		// V4 - top right
			-1.5f, 0.0f,   0.0f,		// V1 - bottom left
			-1.5f, 2.5f,   0.0f,		// V2 - top left
	};		
			 
	private FloatBuffer textureBuffer;	// buffer holding the texture coordinates
	private float texture[] = {    		
			// Mapping coordinates for the vertices
			0.0f, 1.0f,		// top left		(V2)
			0.0f, 0.0f,		// bottom left	(V1)
			1.0f, 1.0f,		// top right	(V4)
			1.0f, 0.0f,		// bottom right	(V3)
	};		
	/** The texture pointer */
	private int[] textures = new int[2];

	public Square1() {
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices_frontface.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		
		// allocates the memory from the byte buffer
		vertexBuffer = byteBuffer.asFloatBuffer();
		
		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices_frontface);
		
		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
		
		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		revolution_angle = 0;
		time_start = System.currentTimeMillis();
		
		revolution_velocity	= 2.f;//C_REVOLUTION_VELOCITY_MIN 	+ (int)(Math.random()* C_REVOLUTION_VELOCITY_MAX);
		ttl = C_TTL_MIN + (int)(Math.random() * C_TTL_MAX);
		//revolution_velocity /= 5 ;
		
	}

	/**
	 * Load the texture for the square
	 * @param gl
	 * @param context
	 */
	public static void setDimension(int width,int height){
		m_imageW = width/2.f;
		m_imageH = height/2.f;
	}
	public void loadGLTexture(GL10 gl, Resources resource, int id) {
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeResource(resource,id);
		loadGLTexture(gl,bitmap);
		bitmap.recycle();
	}
	
	public void loadGLTexture(GL10 gl,Bitmap bitmap) {
		
		//Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
			//	R.drawable.android1);

		// generate one texture pointer
		gl.glGenTextures(1, textures, 0);
		
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		m_imageW = bitmap.getWidth();
		m_imageH = bitmap.getHeight();		
		
		// Clean up
		
		
		bitmap.recycle();
	}

	public void loadGLTexture(GL10 gl,Bitmap bitmap1,Bitmap bitmap2) {
		
		//Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
			//	R.drawable.android1);

		// generate one texture pointer
		
		gl.glGenTextures(2, textures, 0);
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap1, 0);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);		
		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap2, 0);

		//m_imageW = bitmap.getWidth();
		//m_imageH = bitmap.getHeight();		
		
		// Clean up
		
		
		bitmap1.recycle();
		bitmap2.recycle();
	}

	public void drawImage(GL10 gl){
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glPushMatrix();
		{
			
			
			gl.glTranslatef(-1.5f/2,2.5f/2,0f);
			gl.glScalef(0.97f, 0.97f, 0f);
			gl.glRotatef(revolution_angle, 0, 1, 0);
			gl.glTranslatef(1.5f/2,-2.5f/2f,0f);
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			vertexBuffer.put(vertices_frontface);
			vertexBuffer.position(0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices_frontface.length / 3);
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
			vertexBuffer.put(vertices_backface);
			vertexBuffer.position(0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices_frontface.length / 3);
			
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			vertexBuffer.put(vertices_rightface);
			vertexBuffer.position(0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices_frontface.length / 3);
	
			vertexBuffer.put(vertices_leftface);
			vertexBuffer.position(0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices_frontface.length / 3);
		}
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		
	}
	/** The draw method for the square with the GL context */
	public void draw(GL10 gl) {
		// bind the previously generated texture
		
		///x-= 0.0002;
			
		// Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		// Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		
		// Point to our vertex buffer
				
		// Draw the vertices as triangle strip
		drawImage(gl);
		//Disable the client state before leaving
	}
	
	public void startRotation(){
		m_rotate = true;
	}
	
	public void release(GL10 gl){
		gl.glDeleteTextures(2, textures, 0);
		Log.d("DEBUG","released");
	}
	
	public void update(){
		
		// for rotation
		
		if(m_rotate) {
			
			revolution_angle+=revolution_velocity;
			if(revolution_angle == 180)
			{
			//	revolution_angle=180;
				m_rotate = false;
			}
			else if(revolution_angle == 360)
			{
				revolution_angle=0;
				m_rotate = false;
			} 
			if(!m_rotate){
				time_start = System.currentTimeMillis();
				ttl = C_TTL_MIN + (int)(Math.random() * C_TTL_MAX);
			}
		
		}
		else {
			if(System.currentTimeMillis() - time_start > ttl)
			{
				m_rotate = true;
			}
		}
		
	}
	
	//-------------- draw functions -----
		
}

