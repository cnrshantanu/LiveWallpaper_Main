package net.markguerra.android.glwallpapertest;

import android.util.Log;
import android.view.SurfaceHolder;
import net.rbgrn.android.glwallpaperservice.*;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class MyWallpaperService extends GLWallpaperService {
	public MyWallpaperService() {
		super();
	}
	public Engine onCreateEngine() {
		MyEngine engine = new MyEngine();
		return engine;
	}

	class MyEngine extends GLEngine {
		//MyRenderer renderer;
		NewRenderer renderer;
		public MyEngine() {
			super();
			// handle prefs, other initialization
			//renderer = new MyRenderer();
			Log.d("*#DEBUG","*#DEBUG new renderer");
			renderer = new NewRenderer(getResources());
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
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
	}
}
