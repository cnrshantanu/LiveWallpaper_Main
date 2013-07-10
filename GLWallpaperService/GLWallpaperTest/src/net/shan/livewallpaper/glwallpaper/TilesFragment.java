
package net.shan.livewallpaper.glwallpaper;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


public class TilesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    
//    	iv.setOnClickListener(new OnClickListener(){
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			Log.d("COW"," we have an image");
//			
//		}});
    	
    	
    	return inflater.inflate(R.layout.tab_tilefragment, container, false);
    }
    
    @Override
	public void onStart() {
		super.onStart();
		
		/** Setting the multiselect choice mode for the listview */
		ImageView iv = (ImageView)getActivity().findViewById(R.id.quad1);
    	iv.setOnClickListener(clickListener);	
    	iv = (ImageView)getActivity().findViewById(R.id.quad2);
    	iv.setOnClickListener(clickListener);	
    	iv = (ImageView)getActivity().findViewById(R.id.quad3);
    	iv.setOnClickListener(clickListener);	
    	iv = (ImageView)getActivity().findViewById(R.id.quad4);
    	iv.setOnClickListener(clickListener);	
	}
    
    private OnClickListener clickListener = new OnClickListener() {

        public void onClick(View v) {
        	
			Log.d("COW"," we have an image");
        }
};
}