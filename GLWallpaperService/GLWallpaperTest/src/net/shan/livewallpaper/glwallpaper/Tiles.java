package net.shan.livewallpaper.glwallpaper;

import android.widget.ImageView;

public class Tiles {
	
		public class c_tileType {
			
			public static final int E_IMAGE = 0;
			public static final int E_NOTE = 1;
			public static final int E_MAX = 2;
		
		};
		
		private static int m_iconId[] = new int[c_tileType.E_MAX];	 
		public ImageView m_icon;
		public int 		 m_tileType = c_tileType.E_IMAGE;
		
		
		public Tiles() {
			
			m_iconId[c_tileType.E_IMAGE] = R.drawable.app_image;
			m_iconId[c_tileType.E_NOTE] = R.drawable.app_note;
			m_icon.setImageResource(m_iconId[c_tileType.E_IMAGE]);
		
		}
		
		public void switchTile(){
			
			m_tileType = (m_tileType++)%(c_tileType.E_MAX);
			m_icon.setImageResource(m_iconId[m_tileType]);
			
		}
}



