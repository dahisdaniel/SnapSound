package com.example.snapsound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ListToSend extends ListActivity {

	
	public static final String TAG = ListToSend.class.getSimpleName();
	protected List<ParseUser> users;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected Button sendButton;
	protected MenuItem mSendItem;
	protected byte[] byteArrayPath;
	protected byte[] fileBytes;
	protected Uri mMedia;
	private MediaPlayer myPlayer;
	private String outputFile = null;
	private String messageDuration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_to_send);
		
		//reseting the output path of the audio archive
		  outputFile = Environment.getExternalStorageDirectory().
	              getAbsolutePath() + "/snapSound.3gpp";
		//done
		 
		play();
		  
		  
		  
		//retrieving information from the other activity
		
		byteArrayPath= getIntent().getByteArrayExtra(ParseConstants.KEY_FILE);
		//done
		//messageDuration=getIntent().getExtras().getString(ParseConstants.KEY_MESSAGE_DURATION);
		
		
		
		
		
		
		
		sendButton= (Button) findViewById(R.id.sendButton);
		final ListView listView = (ListView) findViewById(android.R.id.list);
		
		
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		
		
		
	
		
		
		
		mCurrentUser=ParseUser.getCurrentUser();
		mFriendsRelation= mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		ParseQuery <ParseUser> query = mFriendsRelation.getQuery();
		query.setLimit(1000);
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);

		query.findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> friends, com.parse.ParseException e) {
				// TODO Auto-generated method stub
				if(e==null){
					//Searched
					Toast.makeText(ListToSend.this,"DONE",Toast.LENGTH_LONG).show();
					//new
					users=friends;
					
	
					String[] usernames = new String[users.size()];
					int i =0;
					for (ParseUser pUser : users) {
						usernames[i]=pUser.getUsername();
						i++;
						
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListToSend.this,android.R.layout.simple_list_item_checked,usernames);
					listView.setAdapter(adapter);
					//end new
				}
				
				else{
					Log.e(TAG,e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(ListToSend.this);
			    	builder.setMessage(R.string.Message_error);
			    	builder.setTitle(R.string.loginDialogTitle_error);
			    	builder.setPositiveButton(android.R.string.ok, null);
			    	
			    	AlertDialog dialog= builder.create();
			    	dialog.show();
				}
				
			}
		});
		
		
		
	
		
		
		
		
	}
	
	
	//play recording
	public void play() {
		try{
			myPlayer = new MediaPlayer();
			myPlayer.setDataSource(outputFile);
			myPlayer.prepare();
			myPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//done
	
	
	
	

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (l.getCheckedItemCount()>0){

			mSendItem.setVisible(true);
		}
		else{
			mSendItem.setVisible(false);
		}

		
		
		
		if (getListView().isItemChecked(position)){
			// Add to a list of ParseUsers and then, by the time send is pressed, we send to all parse users there.
			Toast.makeText(ListToSend.this, "Clicado na posicao:"+users.get(position),Toast.LENGTH_LONG).show();

		}
		
		
		//	mCurrentUser.saveInBackground(new SaveCallback() {
		//		@Override
		//		public void done(com.parse.ParseException arg0) {
		//			// TODO Auto-generated method stub
		//			
		//		}
		//	});
	}
	
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_to_send, menu);
		mSendItem=menu.getItem(0);//theres only one item on this menu
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		int id = item.getItemId();
		
		if(id==R.id.action_sendMessage){
			ParseObject message = createMessage();
			if (message==null){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	builder.setMessage(R.string.error_sending_a_file);
		    	builder.setTitle(R.string.loginDialogTitle_error);
		    	builder.setPositiveButton(android.R.string.ok, null);
		    	
		    	AlertDialog dialog= builder.create();
		    	dialog.show();
				
			}
			else{
				
			send(message);
			finish();
			
			}
			
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}


	
	
	
	protected ParseObject createMessage(){
		ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGE);
		message.put(ParseConstants.KEY_SENDER_NAME,ParseUser.getCurrentUser().getUsername());
		message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		message.put(ParseConstants.KEY_RECIPIENTS_IDS, getRecipientsId());
		//message.put(ParseConstants.KEY_MESSAGE_DURATION, messageDuration);
		
		
		
		
		if(byteArrayPath.equals(null)){
			return null;
		}
		else{
			ParseFile file = new ParseFile(byteArrayPath);
			message.put(ParseConstants.KEY_FILE, file);
			return message;
		}
	
	}
	
	
	protected ArrayList<String> getRecipientsId(){
		ArrayList<String> recipientsIds = new ArrayList<String>();
		for(int i=0; i<getListView().getCount(); i++){
			if(getListView().isItemChecked(i)){
				recipientsIds.add(users.get(i).getObjectId());
			}
		}
		
		return recipientsIds;
		
		
	}
	
	protected void send(ParseObject message){
		message.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e==null){
					//sucess
				}
				
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(ListToSend.this);
			    	builder.setMessage("Error Saving on the Back End");
			    	builder.setTitle(R.string.loginDialogTitle_error);
			    	builder.setPositiveButton(android.R.string.ok, null);
			    	
			    	AlertDialog dialog= builder.create();
			    	dialog.show();
				}
				
			}
		});
	}

}
