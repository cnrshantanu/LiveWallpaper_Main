/**
 * 
 */
package net.shan.livewallpaper.glwallpaper;

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
public class SquareBackground {
	
	protected static final int C_REVOLUTION_VELOCITY_MAX = 2;
	protected static final int C_REVOLUTION_VELOCITY_MIN = 1;
	public static int C_TTL_MAX = 48000;
	protected static int C_TTL_MIN = 21000;
	protected long  time_start 			= 0;
	protected int   ttl 					= 0;
	protected boolean m_rotate 			= false;
	protected int   m_changeTexture 		= -1;
	protected float revolution_angle;
	protected float revolution_velocity;
	
	
	
	protected FloatBuffer vertexBuffer;	// buffer holding the vertices
	protected float vertices_frontface[] = {
			-1.5f,  0.0f,  0.0f,		// V1 - bottom left
			-1.5f,  2.5f,  0.0f,		// V2 - top left
			 0.0f,  0.0f,  0.0f,		// V3 - bottom right
			 0.0f,  2.5f,  0.0f,		// V4 - top right
	};
	
	protected float vertices_rightface[] = {
			 0.0f,  0.0f,  0.0f,		// V1 - bottom left
			 0.0f,  2.5f,  0.0f,		// V2 - top left
			 0.0f,  0.0f, -0.2f,		// V3 - bottom right
			 0.0f,  2.5f, -0.2f,		// V4 - top right
	};	
	protected float vertices_backface[] = {
			 0.0f, 0.0f,  -0.2f,		// V1 - bottom left
			 0.0f, 2.5f,  -0.2f,		// V2 - top left
			-1.5f, 0.0f,  -0.2f,		// V3 - bottom right
			-1.5f, 2.5f,  -0.2f,		// V4 - top right
	};
	
	protected float vertices_leftface[] = {
			-1.5f, 0.0f,  -0.2f,		// V3 - bottom right
			-1.5f, 2.5f,  -0.2f,		// V4 - top right
			-1.5f, 0.0f,   0.0f,		// V1 - bottom left
			-1.5f, 2.5f,   0.0f,		// V2 - top left
	};		
			 
	public SquareBackground() {
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices_frontface.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		
		// allocates the memory from the byte buffer
		vertexBuffer = byteBuffer.asFloatBuffer();
		
		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices_frontface);
		
		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
		
		
		revolution_angle = 0;
		time_start = System.currentTimeMillis();
		
		revolution_velocity	= 3.f;//C_REVOLUTION_VELOCITY_MIN 	+ (int)(Math.random()* C_REVOLUTION_VELOCITY_MAX);
		ttl = C_TTL_MIN + (int)(Math.random() * C_TTL_MAX);
		//revolution_velocity /= 5 ;
		
	}

	/**
	 * Load the texture for the square
	 * @param gl
	 * @param context
	 */
	
	
	
	private void drawImage(GL10 gl){
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			
		gl.glPushMatrix();
		{
			
			gl.glTranslatef(-1.5f/2,2.5f/2,0f);
			gl.glScalef(0.97f, 0.97f, 0f);
			gl.glRotatef(revolution_angle, 0, 1, 0);
			gl.glTranslatef(1.5f/2,-2.5f/2f,0f);
			gl.glColor4f(0.945f, 0.812f, 0.447f, 1f);
			
			vertexBuffer.put(vertices_frontface);
			vertexBuffer.position(0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices_frontface.length / 3);
			
			vertexBuffer.put(vertices_backface);
			vertexBuffer.position(0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices_frontface.length / 3);
			
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
	
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			
		// Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		drawImage(gl);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
	}
	
	public void startRotation(){
		m_rotate = true;
	}
	
	public void release(GL10 gl){
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
				m_changeTexture = 0;
			}
			else if(revolution_angle == 360)
			{
				revolution_angle=0;
				m_changeTexture = 1;
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

