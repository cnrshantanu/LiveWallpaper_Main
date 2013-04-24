package net.markguerra.android.glwallpapertest;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
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
    
    
    
    //searches for files with .jpg extension
    ArrayList<String> dirs;
    FilenameFilter imageFilter = new FilenameFilter() {
            public boolean accept(File file, String string) {
                    string = string.toLowerCase();
                    if (string.endsWith(".jpg")) {
                            return true;
                    }
                    return false;
            }
    };
    
    
    
    
    FileFilter noMediaFilter = new FileFilter() {
        public boolean accept(final File file) {
                if (file.isDirectory()) {
                        // We can't access this directory
                        if (!file.canRead()) {
                                Log.d("Finding Folder", file + " is not accessible");
                                return false;
                        }

                        // We don't want hidden directories
                        if (file.getName().startsWith(".")) {
                                Log.d("Finding Folder", file + " is hidden");
                                return false;
                        }
                        
                        // We don't want directories containing .nomedia
                        final String[] files = file.list();
                        for (final String f : files) {
                                if (f.equalsIgnoreCase(".nomedia")) {
                                        Log.d("Finding Folder", file + " contains .nomedia");
                                        return false;
                                }
                        }
                        
                        // We don't want directories containing only AlbumArt.jpg
                        final String[] images = file.list(imageFilter);
                        if (images.length == 1 &&
                                        images[0].equalsIgnoreCase("AlbumArt.jpg")) {
                                Log.d("Finding Folder", file + " contains only AlbumArt.jpg");
                                return false;
                        }

                        // Valid directory
                        return true;
                }

                // Not a directory
                return false;
        }
};



@Override    
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(
                MyWallpaperService.SHARED_PREFS_NAME);
        addPreferencesFromResource(R.xml.glwallpapertest_setting);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(
                this);
        
        dirs = new ArrayList<String>();
        traverse(new File("/sdcard"));
        String[] filesStr = new String[dirs.size()];
        for (int i = 0; i < dirs.size(); i++) {
                filesStr[i] = dirs.get(i).toString();
                String temp = filesStr[i].substring(filesStr[i].lastIndexOf('/'));
                temp = temp +'/';
                filesStr[i]=temp;
                Log.d("Barcelona",filesStr[i]);
        }

        ListPreference path = (ListPreference) findPreference("Folderpath");
        path.setEntries(filesStr);
        path.setEntryValues(filesStr);
        path.setDefaultValue(filesStr[0]);
        
        /*CharSequence folderName[] = { "Football" , "Comics", "Saugat"};
        ListPreference myFolderList = (ListPreference) findPreference("Folderpath");
        myFolderList.setEntries(folderName);
        ListPreference myFolders = (ListPreference) findPreference("Folderpath");
        CharSequence folders[] = { "/football/" , "/Comics/" , "/Saugat/" };
        myFolders.setEntryValues(folders);*/
    }

    private void traverse(File dir) {
	// TODO Auto-generated method stub
    	if (dir.isDirectory()) {
            final File[] files = dir.listFiles(noMediaFilter);
            for (final File file : files) {
                    final String strFile = file.toString();
                    if (!dirs.contains(strFile)) {
                            if (file.list(imageFilter).length > 0) {
                                    dirs.add(strFile);
                            }
                    }
                    traverse(file);
            }
    }
	
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
