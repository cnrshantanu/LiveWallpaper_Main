
package net.shan.livewallpaper.glwallpaper;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class NotesFragment extends ListFragment {

	private static final String[] PROJECTION = new String[] {
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
		 Toast.makeText(ActivityContainer.appContext, "Bleh" + position + "id" + id, Toast.LENGTH_LONG).show();
		 
		 
	 }
	 
	 
	
	
    
}