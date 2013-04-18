package net.markguerra.android.glwallpapertest;

import java.util.ArrayList;

import net.markguerra.android.glwallpapertest.R;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class LiveWallpaperSettings extends PreferenceActivity
    implements SharedPreferences.OnSharedPreferenceChangeListener {
	 
	
	final ArrayList<String> image_folders = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(
                MyWallpaperService.SHARED_PREFS_NAME);
        addPreferencesFromResource(R.xml.glwallpapertest_setting);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(
                this);
        CharSequence folderName[] = { "Football" , "Comics", "Saugat"};
        ListPreference myFolderList = (ListPreference) findPreference("Folderpath");
        myFolderList.setEntries(folderName);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                this);
        super.onDestroy();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
    	Log.d("DEBUG","DEBUG Listener called");
    }
}
