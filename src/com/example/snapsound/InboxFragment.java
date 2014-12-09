package com.example.snapsound;

import java.util.List;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class InboxFragment extends ListFragment {
	
	
	protected List<ParseObject> mMessage;
	protected Uri mMedia;
	private MediaPlayer myPlayer;
	private String outputFile = null;

	
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
         return rootView;
     }
	 
		//play recording
		public void play() {
			try{
				myPlayer = new MediaPlayer();
				myPlayer.setDataSource(getListView().getContext(),mMedia);
				myPlayer.prepare();
				myPlayer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//done
	 
	 
	 @Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			getActivity().setProgressBarIndeterminateVisibility(true);
			
			
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGE);
			query.whereEqualTo(ParseConstants.KEY_RECIPIENTS_IDS, ParseUser.getCurrentUser().getObjectId());
			query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
			query.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> messages, ParseException e) {
					getActivity().setProgressBarIndeterminateVisibility(false);
					
					if(e==null){
						mMessage=messages;
						

						String[] usernames = new String[mMessage.size()];
						int i =0;
						for (ParseObject message : mMessage) {
							usernames[i]=message.getString(ParseConstants.KEY_SENDER_NAME);
							i++;
							
						}
						MessageAdapter adapter = new MessageAdapter(
								getListView().getContext(), 
								mMessage);
						setListAdapter(adapter);
						}
					
				}
			});
			 
			
		}
	 
	 @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ParseObject object = mMessage.get(position);
		ParseFile file = object.getParseFile(ParseConstants.KEY_FILE);
		
		Uri fileUri = Uri.parse(file.getUrl());
		
		mMedia=fileUri;
		
		
		
		
		play();
		
		
		
		
		
		
		

 //           Dialog dialog = new Dialog(getListView().getContext());
            //View vLoad = LayoutInflater.from(getListView().getContext()).inflate(R.layout.dialog_listen_message, null);      
            ProgressDialog progBar = new ProgressDialog(getListView().getContext());
            progBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progBar.setIndeterminate(false);
            progBar.show();
//            dialog.setContentView(vLoad);
//            dialog.setTitle("Teste");
//            dialog.show();            
//              
//            tv=(TextView)dialog.findViewById(R.id.tv);
//            start=(Button)dialog.findViewById(R.id.start);
//            stop=(Button)dialog.findViewById(R.id.stop);
//              
//            dialog.setTitle("Dialog with CountDownTimer");
//            dialog.setCancelable(false);
//              
//            tv.setText("Countdown begins from : 10 minutes");
//            tv.setTextColor(Color.RED);
//            start.setOnClickListener(new OnClickListener()
//                  {
//                    public void onClick(View v) 
//                          {
//                    timer.start();  //will start the timer                                                      
//                           }
//                  });
//              
//             stop.setOnClickListener(new OnClickListener() 
//                      {
//                        public void onClick(View v)
//                              {
//                                 timer.cancel();
//                                 dialog.dismiss();
//                              }
//                      });
//              
//            dialog.show();      

	 
	 }
}
