package net.shan.livewallpaper.glwallpaper;



import net.shan.livewallpaper.glwallpaper.NotePad;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ClipboardManager;
import android.content.ComponentName;
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
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//      return true;
//    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
//
//        // The paste menu item is enabled if there is data on the clipboard.
//        ClipboardManager clipboard = (ClipboardManager)
//                getSystemService(Context.CLIPBOARD_SERVICE);
//
//
//        MenuItem mPasteItem = menu.findItem(R.id.menu_paste);
//
//        // If the clipboard contains an item, enables the Paste option on the menu.
//        if (clipboard.hasPrimaryClip()) {
//            mPasteItem.setEnabled(true);
//        } else {
//            // If the clipboard is empty, disables the menu's Paste option.
//            mPasteItem.setEnabled(false);
//        }
//
//        // Gets the number of notes currently being displayed.
//        final boolean haveItems = getListAdapter().getCount() > 0;
//
//        // If there are any notes in the list (which implies that one of
//        // them is selected), then we need to generate the actions that
//        // can be performed on the current selection.  This will be a combination
//        // of our own specific actions along with any extensions that can be
//        // found.
//        if (haveItems) {
//
//            // This is the selected item.
//            Uri uri = ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId());
//
//            // Creates an array of Intents with one element. This will be used to send an Intent
//            // based on the selected menu item.
//            Intent[] specifics = new Intent[1];
//
//            // Sets the Intent in the array to be an EDIT action on the URI of the selected note.
//            specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
//
//            // Creates an array of menu items with one element. This will contain the EDIT option.
//            MenuItem[] items = new MenuItem[1];
//
//            // Creates an Intent with no specific action, using the URI of the selected note.
//            Intent intent = new Intent(null, uri);
//
//            /* Adds the category ALTERNATIVE to the Intent, with the note ID URI as its
//             * data. This prepares the Intent as a place to group alternative options in the
//             * menu.
//             */
//            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
//
//            /*
//             * Add alternatives to the menu
//             */
//            menu.addIntentOptions(
//                Menu.CATEGORY_ALTERNATIVE,  // Add the Intents as options in the alternatives group.
//                Menu.NONE,                  // A unique item ID is not required.
//                Menu.NONE,                  // The alternatives don't need to be in order.
//                null,                       // The caller's name is not excluded from the group.
//                specifics,                  // These specific options must appear first.
//                intent,                     // These Intent objects map to the options in specifics.
//                Menu.NONE,                  // No flags are required.
//                items                       // The menu items generated from the specifics-to-
//                                            // Intents mapping
//            );
//                // If the Edit menu item exists, adds shortcuts for it.
//                if (items[0] != null) {
//
//                    // Sets the Edit menu item shortcut to numeric "1", letter "e"
//                    items[0].setShortcut('1', 'e');
//                }
//            } else {
//                // If the list is empty, removes any existing alternative actions from the menu
//                menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
//            }
//
//        // Displays the menu
//        return true;
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