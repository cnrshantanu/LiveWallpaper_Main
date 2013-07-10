
package net.shan.livewallpaper.glwallpaper;


import android.app.Fragment;
import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NotesFragment extends ListFragment {

	public static final String[] PROJECTION = new String[] {
        NotePad.Notes._ID, // 0
        NotePad.Notes.COLUMN_NAME_TITLE, // 1
	};

	private static final int COLUMN_INDEX_TITLE = 1;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		//shit
		
		
	
		  Cursor cursor = getActivity().managedQuery(
				  	NotePad.Notes.CONTENT_URI,            // Use the default content URI for the provider.
		            PROJECTION,                       // Return the note ID and title for each note.
		            null,                             // No where clause, return all records.
		            null,                             // No where clause, therefore no where column values.
		            NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
		        );
		  
		  String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE } ;
		  int[] viewIDs = { android.R.id.text1 };
		  SimpleCursorAdapter adapter
        = new SimpleCursorAdapter(
                  getActivity(),                             // The Context for the ListView
                  R.layout.tab_notesfragment,          // Points to the XML for a list item
                  cursor,                           // The cursor to get items from
                  dataColumns,
                  viewIDs
          );

    // Sets the ListView's adapter to be the cursor adapter that was just created.
		setListAdapter(adapter);
		// Inflate the layout for this fragment
        //return inflater.inflate(R.layout.tab_notesfragment, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	@Override
	public void onStart() {
		super.onStart();
		
		/** Setting the multiselect choice mode for the listview */
			getListView().setOnCreateContextMenuListener(this);
			setHasOptionsMenu(true);
	}
	
	 @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	
		 Uri uri = ContentUris.withAppendedId(getActivity().getIntent().getData(), id);

	        // Gets the action from the incoming Intent
	        String action = getActivity().getIntent().getAction();

	        // Handles requests for note data
	        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {

	            // Sets the result to return to the component that called this Activity. The
	            // result contains the new URI
	            //setResult(RESULT_OK, new Intent().setData(uri));
	        } else {

	            // Sends out an Intent to start an Activity that can handle ACTION_EDIT. The
	            // Intent's data is the note ID URI. The effect is to call NoteEdit.
	            startActivity(new Intent(Intent.ACTION_EDIT, uri));
	        }
		 
		 
		 
	 }
	 
	 @Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 //
       // The paste menu item is enabled if there is data on the clipboard.
       ClipboardManager clipboard = (ClipboardManager)
               getActivity().getSystemService(Context.CLIPBOARD_SERVICE);


       MenuItem mPasteItem = menu.findItem(R.id.menu_paste);

       // If the clipboard contains an item, enables the Paste option on the menu.
       if (clipboard.hasPrimaryClip()) {
           mPasteItem.setEnabled(true);
       } else {
           // If the clipboard is empty, disables the menu's Paste option.
           mPasteItem.setEnabled(false);
       }

       // Gets the number of notes currently being displayed.
       final boolean haveItems = getListAdapter().getCount() > 0;

       // If there are any notes in the list (which implies that one of
       // them is selected), then we need to generate the actions that
       // can be performed on the current selection.  This will be a combination
       // of our own specific actions along with any extensions that can be
       // found.
       if (haveItems) {

    	   // This is the selected item.
           Uri uri = ContentUris.withAppendedId(getActivity().getIntent().getData(), getSelectedItemId());

           // Creates an array of Intents with one element. This will be used to send an Intent
           // based on the selected menu item.
           Intent[] specifics = new Intent[1];

           // Sets the Intent in the array to be an EDIT action on the URI of the selected note.
           specifics[0] = new Intent(Intent.ACTION_EDIT, uri);

           // Creates an array of menu items with one element. This will contain the EDIT option.
           MenuItem[] items = new MenuItem[1];

           // Creates an Intent with no specific action, using the URI of the selected note.
           Intent intent = new Intent(null, uri);

           /* Adds the category ALTERNATIVE to the Intent, with the note ID URI as its
            * data. This prepares the Intent as a place to group alternative options in the
            * menu.
            */
           intent.addCategory(Intent.CATEGORY_ALTERNATIVE);

           /*
            * Add alternatives to the menu
            */
           menu.addIntentOptions(
               Menu.CATEGORY_ALTERNATIVE,  // Add the Intents as options in the alternatives group.
               Menu.NONE,                  // A unique item ID is not required.
               Menu.NONE,                  // The alternatives don't need to be in order.
               null,                       // The caller's name is not excluded from the group.
               specifics,                  // These specific options must appear first.
               intent,                     // These Intent objects map to the options in specifics.
               Menu.NONE,                  // No flags are required.
               items                       // The menu items generated from the specifics-to-
                                           // Intents mapping
           );
               // If the Edit menu item exists, adds shortcuts for it.
               if (items[0] != null) {

                   // Sets the Edit menu item shortcut to numeric "1", letter "e"
                   items[0].setShortcut('1', 'e');
               }
           } else {
               // If the list is empty, removes any existing alternative actions from the menu
               menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
             }

       // Displays the menu
         super.onPrepareOptionsMenu(menu);
         
         //setHasOptionsMenu(true);
	}
	 
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		    inflater.inflate(R.menu.list_options_menu, menu);
	        Intent intent = new Intent(null, getActivity().getIntent().getData());
	        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
	                new ComponentName(getActivity(), NotesFragment.class), null, intent, 0, null);
	        super.onCreateOptionsMenu(menu, inflater);
	}
	 
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		// The data from the menu item.
        AdapterView.AdapterContextMenuInfo info;

        // Tries to get the position of the item in the ListView that was long-pressed.
        try {
            // Casts the incoming data object into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            // If the menu object can't be cast, logs an error.
            Log.e("DEBUG", "bad menuInfo", e);
            return;
        }

        /*
         * Gets the data associated with the item at the selected position. getItem() returns
         * whatever the backing adapter of the ListView has associated with the item. In NotesList,
         * the adapter associated all of the data for a note with its list item. As a result,
         * getItem() returns that data as a Cursor.
         */
        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);

        // If the cursor is empty, then for some reason the adapter can't get the data from the
        // provider, so returns null to the caller.
        if (cursor == null) {
            // For some reason the requested item isn't available, do nothing
            return;
        }

        // Inflate menu from XML resource
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);

        // Sets the menu header to be the title of the selected note.
        menu.setHeaderTitle(cursor.getString(COLUMN_INDEX_TITLE));

        // Append to the
        // menu items for any other activities that can do stuff with it
        // as well.  This does a query on the system for any activities that
        // implement the ALTERNATIVE_ACTION for our data, adding a menu item
        // for each one that is found.
        Intent intent = new Intent(null, Uri.withAppendedPath(getActivity().getIntent().getData(), 
                                        Integer.toString((int) info.id) ));
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(getActivity(), NotesFragment.class), null, intent, 0, null);

		
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// The data from the menu item.
        AdapterView.AdapterContextMenuInfo info;

        /*
         * Gets the extra info from the menu item. When an note in the Notes list is long-pressed, a
         * context menu appears. The menu items for the menu automatically get the data
         * associated with the note that was long-pressed. The data comes from the provider that
         * backs the list.
         *
         * The note's data is passed to the context menu creation routine in a ContextMenuInfo
         * object.
         *
         * When one of the context menu items is clicked, the same data is passed, along with the
         * note ID, to onContextItemSelected() via the item parameter.
         */
        try {
            // Casts the data object in the item into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {

            // If the object can't be cast, logs an error
           // Triggers default processing of the menu item.
            return false;
        }
        // Appends the selected note's ID to the URI sent with the incoming Intent.
        Uri noteUri = ContentUris.withAppendedId(getActivity().getIntent().getData(), info.id);

        /*
         * Gets the menu item's ID and compares it to known actions.
         */
        switch (item.getItemId()) {
        case R.id.context_open:
            // Launch activity to view/edit the currently selected item
            startActivity(new Intent(Intent.ACTION_EDIT, noteUri));
            return true;

        case R.id.context_copy:
            // Gets a handle to the clipboard service.
            ClipboardManager clipboard = (ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
  
            // Copies the notes URI to the clipboard. In effect, this copies the note itself
            clipboard.setPrimaryClip(ClipData.newUri(   // new clipboard item holding a URI
                    getActivity().getContentResolver(),               // resolver to retrieve URI info
                    "Note",                             // label for the clip
                    noteUri)                            // the URI
            );
  
            // Returns to the caller and skips further processing.
            return true;

        case R.id.context_delete:
  
            // Deletes the note from the provider by passing in a URI in note ID format.
            // Please see the introductory note about performing provider operations on the
            // UI thread.
            getActivity().getContentResolver().delete(
                noteUri,  // The URI of the provider
                null,     // No where clause is needed, since only a single note ID is being
                          // passed in.
                null      // No where clause is used, so no where arguments are needed.
            );
  
            // Returns to the caller and skips further processing.
            return true;
        default:
            return super.onContextItemSelected(item);
        }
	}
	
    
}