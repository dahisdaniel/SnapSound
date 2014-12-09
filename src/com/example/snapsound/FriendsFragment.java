package com.example.snapsound;

import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class FriendsFragment extends ListFragment {
	
	
	public static final String TAG = FriendsFragment.class.getSimpleName();
	
	
	protected List<ParseUser> mFriends;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	

	
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
         return rootView;
     }
	 
	 
	 public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			getActivity().setProgressBarIndeterminateVisibility(true);
			mCurrentUser=ParseUser.getCurrentUser();
			mFriendsRelation= mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
			ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
			query.setLimit(1000);
			query.addAscendingOrder(ParseConstants.KEY_USERNAME);

			query.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> friends, ParseException e) {
					getActivity().setProgressBarIndeterminateVisibility(false);
					if(e==null){
					mFriends=friends;
					

					String[] usernames = new String[mFriends.size()];
					int i =0;
					for (ParseUser pUser : mFriends) {
						usernames[i]=pUser.getUsername();
						i++;
						
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(),android.R.layout.simple_list_item_1,usernames);
					setListAdapter(adapter);
					}
					else{
						
						Log.e(TAG,e.getMessage());
						AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
				    	builder.setMessage(R.string.Message_error);
				    	builder.setTitle(R.string.loginDialogTitle_error);
				    	builder.setPositiveButton(android.R.string.ok, null);
				    	
				    	AlertDialog dialog= builder.create();
				    	dialog.show();
					}
					
				}
			});
		
	 }
	 
}
