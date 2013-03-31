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
	private static final int C_TTL_MAX = 2;
	private static final int C_TTL_MIN = 1;
	
	private float m_imageW = 0;
	private float m_imageH = 0;
	private float revolution_angle;
	private float revolution_velocity;
	private int   ttl = 0;
			
		
	private FloatBuffer vertexBuffer;	// buffer holding the vertices
	/*
	private float vertices[] = {
			-1.0f, -1.0f,  0.0f,		// V1 - bottom left
			-1.0f,  1.0f,  0.0f,		// V2 - top left
			 1.0f, -1.0f,  0.0f,		// V3 - bottom right
			 1.0f,  1.0f,  0.0f			// V4 - top right
	};

	private FloatBuffer textureBuffer;	// buffer holding the texture coordinates
	private float texture[] = {    		
			// Mapping coordinates for the vertices
			0.0f, 1.0f,		// top left		(V2)
			0.0f, 0.0f,		// bottom left	(V1)
			1.0f, 1.0f,		// top right	(V4)
			1.0f, 0.0f		// bottom right	(V3)
	};
	
	*/
	/*
	private float vertices[] = {
			-1.0f, -1.0f,  -1.0f,		// V1 - bottom left
			-1.0f,  1.0f,  -1.0f,		// V2 - top left
			 1.0f, -1.0f,  -1.0f,		// V3 - bottom right
			 1.0f,  1.0f,  -1.0f,		// V4 - top right
			
			 
	};
	*/
	private float vertices[] = {
			-1.5f, 0.0f,  0.0f,		// V1 - bottom left
			-1.5f,  2.5f,  0.0f,		// V2 - top left
			 0.0f, 0.0f,  0.0f,		// V3 - bottom right
			 0.0f,  2.5f,  0.0f,		// V4 - top right
			
			 
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
	private int[] textures = new int[1];

	public Square1() {
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		
		// allocates the memory from the byte buffer
		vertexBuffer = byteBuffer.asFloatBuffer();
		
		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);
		
		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
		
		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		revolution_angle = 0;
		
		revolution_velocity	= C_REVOLUTION_VELOCITY_MIN 	+ (int)(Math.random()* C_REVOLUTION_VELOCITY_MAX);
		revolution_velocity /= 5 ;
	}

	/**
	 * Load the texture for the square
	 * @param gl
	 * @param context
	 */
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

	public void drawImage(GL10 gl){
		
		gl.glPushMatrix();
		{
			
			gl.glTranslatef(-1.5f/2,2.5f/2,0f);
			gl.glScalef(0.97f, 0.97f, 0f);
			gl.glRotatef(revolution_angle, 0, 1, 0);
			gl.glTranslatef(1.5f/2,-2.5f/2f,0f);
			
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		}
		gl.glPopMatrix();
		
	}
	/** The draw method for the square with the GL context */
	public void draw(GL10 gl) {
		// bind the previously generated texture
		
		///x-= 0.0002;
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		// Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		
		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		// Draw the vertices as triangle strip
		drawImage(gl);
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		
		
		
	}
	public void release(GL10 gl){
		gl.glDeleteTextures(1, textures, 0);
	}
	
	public void update(){
		
		// for rotation
			
		revolution_angle-=revolution_velocity;
		if(revolution_angle>360)
			revolution_angle-=360;
		
	}
	
	//-------------- draw functions -----
		
}

