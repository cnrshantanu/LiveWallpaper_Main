package net.shan.livewallpaper.glwallpaper;

import java.io.IOException;
import java.sql.Struct;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;

/*
	author : Shantanu Das
*/

public class SquareNote {
	
	private class QuadPosition{
		public int x;
		public int y;
	}
		
	private QuadPosition[] m_quadPos = new QuadPosition[4];
	private Resources m_resource;
	private Context	  m_context;
	private int		  m_quad,m_width,m_height;
	FontRenderer 	  m_text;
	
	public SquareNote(Resources r,Context _context){
		
		m_resource 	= r;
		m_context	= _context;
		m_width		= 800;
		m_height	= 600;
		
		for(int i = 0;i<4;i++){
			
			m_quadPos[i] = new QuadPosition();
		}
	}
	
	public void onSurfaceCreated(GL10 gl) {
		
		m_text = new FontRenderer(m_context, gl);
		
		try {
			m_text.LoadFont("TimesNewRoman.bff", gl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setDimensions(int _width,int _height){
		
		m_width		= _width;
		m_height	= _height;
		
		m_quadPos[0].x = 0;
		m_quadPos[0].y = (m_height * 93)/100;
		
		m_quadPos[1].x = m_width/2;
		m_quadPos[1].y = (m_height * 93)/100;
		
		m_quadPos[2].x = 0;
		m_quadPos[2].y = (m_height * 93)/200;
		
		m_quadPos[3].x = m_width/2;
		m_quadPos[3].y = (m_height * 93)/200;
		
		
	}
	
	public void setQuadrant(int _index){
		
		if(_index > 4 || _index < 1){
			
			return;
		
		}
		m_quad = _index-1;
	
	}
	
	public void Draw(GL10 gl){
		
		m_text.PrintAt(gl, "when the storm come" +
				"and i dont want to do it " +
				"but the winter is coming " +
				"and the stark family is dead" +
				"so now whatever we want and whatever" +
				"we desire can only be expected from " +
				"jon snow if u know what i mean " +
				"just check out coz the world is " +
				"mean and we need people to fight " +
				"with the other side of the wall " +
				"Thank you do well and definitely fare well",m_quadPos[m_quad].x,m_quadPos[m_quad].y);
		
		
	}
	
	public void release(GL10 gl){
		m_text.release(gl);
	}
}