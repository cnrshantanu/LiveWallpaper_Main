package net.shan.livewallpaper.glwallpaper;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class MyWallpaperService extends GLWallpaperService {
	
	public static final String SHARED_PREFS_NAME="cube2settings";
	
	public MyWallpaperService() {
		super();
	}
	public Engine onCreateEngine() {
		MyEngine engine = new MyEngine();
		return engine;
	}

	
	
	class MyEngine extends GLEngine implements SharedPreferences.OnSharedPreferenceChangeListener{
		//MyRenderer renderer;
		
		private final Handler mHandler = new Handler();
		private SharedPreferences mPrefs;
		private int C_TTL_MAX = 2;
		private String imagePath = "/football/";
		
		
		private NewRenderer renderer;
		public MyEngine() {
			super();
			// handle prefs, other initialization
			//renderer = new MyRenderer();
			
			Log.d("*#DEBUG","*#DEBUG new renderer");
			renderer = new NewRenderer(getResources(),getApplicationContext());
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
			
			mPrefs = MyWallpaperService.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
			mPrefs.registerOnSharedPreferenceChangeListener(this);
			onSharedPreferenceChanged(mPrefs,null);
		}
				
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			//Log.d("*#DEBUG","*#DEBUG surface destroyed");
			super.onSurfaceDestroyed(holder);
			//renderer.release();
		}
		
		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			Log.d("*#DEBUG","*#DEBUG surface created in service");
			renderer.setTTLMax(C_TTL_MAX);
			renderer.setSourceFolder(imagePath);
			super.onSurfaceCreated(holder);
			//renderer.release();
		}
		@Override
		public void onVisibilityChanged(boolean visible) {
			Log.d("*#DEBUG","*#DEBUG surface resumed");
			//renderer.release();
			renderer.setTTLMax(C_TTL_MAX);
			renderer.setSourceFolder(imagePath);
			super.onVisibilityChanged(visible);
		} 
		
		public void onDestroy() {
			super.onDestroy();
			if (renderer != null) {
				renderer.release(); // assuming yours has this method - it should!
			}
			renderer = null;
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			// TODO Auto-generated method stub
				Log.d("DEBUG","TTL key "+key);
				
						if("LiveWallpaper_TTL".equals(key))
						{
							
							String ttl_chosen = mPrefs.getString("LiveWallpaper_TTL", "null");
							Log.d("DEBUG","INSIDE TTL"+ttl_chosen);
							if(ttl_chosen!="null")
								C_TTL_MAX = Integer.parseInt(ttl_chosen);
							
							Log.d("DEBUG","TTL GOT "+ttl_chosen+" C_TTL max "+C_TTL_MAX);
								//renderer.setTTLMax(Integer.parseInt(ttl_chosen));
						}
						//path = mPrefs.getString("path", "/sdcard/DCIM/");
						//public void listFolder(final )
						else if("Folderpath".equals(key))
						{
							imagePath = mPrefs.getString("Folderpath","/football/");
							Log.d("Debug","Folder Chosen is "+imagePath);
						}
			
		}
	}
}
