package net.markguerra.android.glwallpapertest;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import net.rbgrn.android.glwallpaperservice.*;

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
		
		private NewRenderer renderer;
		public MyEngine() {
			super();
			// handle prefs, other initialization
			//renderer = new MyRenderer();
			
			
			
			Log.d("*#DEBUG","*#DEBUG new renderer");
			renderer = new NewRenderer(getResources());
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
		public void onVisibilityChanged(boolean visible) {
			//Log.d("*#DEBUG","*#DEBUG surface destroyed");
			//renderer.release();
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
			
			String ttl_chosen = mPrefs.getString("LiveWallpaper_TTL", "null");
			Log.d("Debug","TTL Chosen is "+ttl_chosen);
			if(ttl_chosen!="null")
			renderer.setTTLMax(Integer.parseInt(ttl_chosen));
			
		}
	}
}
