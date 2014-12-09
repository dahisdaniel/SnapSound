package com.example.snapsound;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditsFriends extends ListActivity{
	
	protected final static String TAG = EditsFriends.class.getSimpleName();
	
	protected List<ParseUser> users;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_edits_friends);

		

		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setProgressBarIndeterminateVisibility(true);
		
		mCurrentUser=ParseUser.getCurrentUser();
		mFriendsRelation= mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		
		
		ParseQuery<ParseUser> query =ParseUser.getQuery();
		query.orderByAscending(ParseConstants.KEY_USERNAME);
		query.setLimit(1000);
		
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> user, com.parse.ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				
				// TODO Auto-generated method stub
				if(e==null){
					//Sucess
					users=user;
					String[] usernames = new String[users.size()];
					int i =0;
					for (ParseUser pUser : users) {
						usernames[i]=pUser.getUsername();
						i++;
						
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditsFriends.this,android.R.layout.simple_list_item_checked,usernames);
					setListAdapter(adapter);
					
					
					checkFriendsCheckmarks();
				}
				
				else{
					Log.e(TAG,e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(EditsFriends.this);
			    	builder.setMessage(R.string.Message_error);
			    	builder.setTitle(R.string.loginDialogTitle_error);
			    	builder.setPositiveButton(android.R.string.ok, null);
			    	
			    	AlertDialog dialog= builder.create();
			    	dialog.show();
				}
				
				
				
				
			}
		});
		

		
	}
	



//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_edits_friends,
					container, false);
			return rootView;
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		if (getListView().isItemChecked(position)){
			//add friend
			//adding friends
			mFriendsRelation.add(users.get(position));
			
		}
		else{
			//dont add
			mFriendsRelation.remove(users.get(position));
			
		}
		mCurrentUser.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e!=null){
					Log.e(TAG,e.getMessage());
				}
					
					
			}
		});
		
	}
	

	public void checkFriendsCheckmarks(){
		mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				// TODO Auto-generated method stub
				if(e==null){
					int i=0;
					for(i =0;i<users.size();i++){
						ParseUser xuser=users.get(i);
						for(ParseUser friend : friends){
							if(xuser.getObjectId().equals(friend.getObjectId())){
								getListView().setItemChecked(i,true);
							}
								
						}
						
						
					}
				}
				
				else{
					Log.e(TAG,e.getMessage());
				}
				
			}
		});
	}

}
