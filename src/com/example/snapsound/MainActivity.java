package com.example.snapsound;


//TODO Implementar a parte de contagem com o TaskTimer,
//TODO Iniciar o Microfone a partir do momento do TaskTimer
//TODO Verificar a questao do botao estar pressionado ou nao.

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
	
	
	public static final String TAG = MainActivity.class.getSimpleName();


    SectionsPagerAdapter mSectionsPagerAdapter;
    double elapsedTime;
    
   
    ViewPager mViewPager;
    long tStart;
    private MediaRecorder myRecorder;
    private String outputFile = null;
    private MediaPlayer myPlayer;
	protected byte[] byteArrayPath;
    private File file;
    Uri introURI; 

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        
         	
       
       
        //setting up the audio part (recording)
        outputFile = Environment.getExternalStorageDirectory().
	              getAbsolutePath() + "/snapSound.3gpp";
       
        //test
        
        byteArrayPath= outputFile.getBytes();
        //done
        
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);
        
        //end of audio part
        
       
		
		
         
         
        
        
       
        
        
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser==null){
        navigateToLogin();
        
        }
        
        else{
        	
        	Log.i(TAG,currentUser.getUsername());
        	
        }
        
        
        
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this,getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


	private void navigateToLogin() {
		Intent newIntent = new Intent(this,LogInActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(newIntent);
	}

	
	
	//audio functions
	
	
	public void start(){
		try {
			myRecorder.prepare();
			myRecorder.start();
		} catch (IllegalStateException e) {
			// start:it is called before prepare()
			// prepare: it is called after start() or before setOutputFormat()
			e.printStackTrace();
		} catch (IOException e) {
			// prepare() fails
			e.printStackTrace();
		}

		

	}


	public void stop(){

		try {

			myRecorder.stop();
			myRecorder.release();
			myRecorder  = null;
			

		} catch (IllegalStateException e) {
			//  it is called before start()
			e.printStackTrace();
		} catch (RuntimeException e) {
			// no valid audio/video data has been received
			e.printStackTrace();
		}
	}

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

	public void stopPlay() {
		try {
			if (myPlayer != null) {
				myPlayer.stop();
				myPlayer.release();
				myPlayer = null;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	
	//end of audio functions
	
	
	
	
	
	
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        switch(id){
        case R.id.action_logout:
            ParseUser.logOut();
            navigateToLogin();
            
        
        
        case R.id.action_edits_friends:
        	Intent newPage = new Intent(MainActivity.this,EditsFriends.class);
        	startActivity(newPage);
        	
        case R.id.action_Mic:
        	
        	
        	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        	
        	LayoutInflater inflater = (MainActivity.this).getLayoutInflater();
        	
        	builder.setView(inflater.inflate(R.layout.dialog_time, null));
        	builder.setTitle(R.string.Message_Counter);
        	builder.setMessage(R.string.Message_stop_Recording);
        	
        	builder.setNegativeButton("Record", new DialogInterface.OnClickListener() {
        		
        		@Override
        		public void onClick(DialogInterface dialog, int which) {
        			tStart = System.currentTimeMillis();
        		
        			start();

        			//TENTATIVA DE CRIAR UM ALERTDIALOG DENTRO DO OUTRO
        			AlertDialog.Builder builderr = new AlertDialog.Builder(MainActivity.this);
        			LayoutInflater inflater = (MainActivity.this).getLayoutInflater();
                	
                	builderr.setView(inflater.inflate(R.layout.dialog_time, null));
        			builderr.setTitle(R.string.Message_Counter);
        			builderr.setMessage("Stop Recording?");
        			builderr.setNeutralButton("Stop", new DialogInterface.OnClickListener() {
        				@Override
        				public void onClick(DialogInterface dialog, int which) {
        					long tDelta=(System.currentTimeMillis()-tStart);
                			elapsedTime=tDelta/1000.0;
                			
                			
                			Toast.makeText(getApplicationContext(), elapsedTime+" Segundos", Toast.LENGTH_SHORT).show();


        					stop();


        					Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
        					dialog.dismiss();

        					

        					//int messageDuration = myPlayer.getDuration();
        					
        					
        					//String MessageDuration= messageDuration+"";
        					file = new File(outputFile);
        					
        					introURI = Uri.fromFile(file);
        					
        					byte[] fileArray = FileHelper.getByteArrayFromFile(MainActivity.this, introURI);
        					
        					
        					
        					Intent nois = new Intent(MainActivity.this, ListToSend.class);
        					//sending the path of stored file through the intent 
        					//nois.putExtra(ParseConstants.KEY_FILE, byteArrayPath);
        					
        					nois.putExtra(ParseConstants.KEY_FILE,fileArray);
        					//nois.putExtra(ParseConstants.KEY_MESSAGE_DURATION, MessageDuration);
        					startActivity(nois);
        					
        					
        					
        					//In case you want to see your recordings
//        					Intent intent = new Intent();
//        					intent.setAction(android.content.Intent.ACTION_VIEW);
//        					intent.setDataAndType(Uri.fromFile(file), "audio/*");
//        					startActivity(intent); 
        					
        				
        					
        					
        					
        					
        					
        					

        				}
        				
        			
        			});	
        			
        			


        			AlertDialog stopRecording= builderr.create();
        			stopRecording.show();

        			//FIM




        		}
        	});

        	AlertDialog dialog= builder.create();
        	dialog.show();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    
   

   
    
    
    
   
}
