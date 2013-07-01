package net.shan.livewallpaper.glwallpaper;

import net.shan.livewallpaper.glwallpaper.NotePad;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class ActivityContainer extends Activity{
	
	public static Context appContext;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);
        appContext = getApplicationContext();

       //ActionBar
        ActionBar actionbar = getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab TileTab = actionbar.newTab().setText("Tiles");
        ActionBar.Tab NoteTab = actionbar.newTab().setText("Notes");
        ActionBar.Tab SettingsTab = actionbar.newTab().setText("Settings");
        
        
        Fragment SettingFragment 	= new SettingsFragment();
        Fragment TileFragment 		= new TilesFragment();
        Fragment NoteFragment 		= new NotesFragment();

        SettingsTab.setTabListener(new MyTabsListener(SettingFragment));
        TileTab.setTabListener(new MyTabsListener(TileFragment));
        NoteTab.setTabListener(new MyTabsListener(NoteFragment));

        actionbar.addTab(TileTab);
        actionbar.addTab(NoteTab);
        actionbar.addTab(SettingsTab);
        
        Intent intent = getIntent();

        if (intent.getData() == null) {
            intent.setData(NotePad.Notes.CONTENT_URI);
        }
        
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_options_menu, menu);
        return true;
    }

    
@Override
public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {
    case R.id.menu_add:
      /*
       * Launches a new Activity using an Intent. The intent filter for the Activity
       * has to have action ACTION_INSERT. No category is set, so DEFAULT is assumed.
       * In effect, this starts the NoteEditor Activity in NotePad.
       */
    	SquareNote.reQuery();
    	Intent intent = getIntent();
    	//Uri link = Uri.parse("content://com.google.provider.NotePad/notes");
    	startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
    	return true;
    case R.id.menu_paste:
      /*
       * Launches a new Activity using an Intent. The intent filter for the Activity
       * has to have action ACTION_PASTE. No category is set, so DEFAULT is assumed.
       * In effect, this starts the NoteEditor Activity in NotePad.
       */
    	SquareNote.reQuery();
    	startActivity(new Intent(Intent.ACTION_PASTE, getIntent().getData()));
    	return true;
    default:
        return super.onOptionsItemSelected(item);
    }
	
}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
    
}



class MyTabsListener implements ActionBar.TabListener {
public Fragment fragment;

public MyTabsListener(Fragment fragment) {
this.fragment = fragment;
}

@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
//Toast.makeText(ActivityContainer.appContext, "Reselected!", Toast.LENGTH_LONG).show();
	}

@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
	ft.replace(R.id.fragment_container, fragment);
}

@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
ft.remove(fragment);
}
	
}