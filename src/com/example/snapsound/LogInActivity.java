package com.example.snapsound;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends ActionBarActivity {
	
	protected TextView  signUptextView;
	protected EditText  musername;
	protected  EditText mpassword;
	protected Button loginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_log_in);

	    signUptextView= (TextView)findViewById(R.id.signUpTextView);	
	   
	    loginButton=(Button)findViewById(R.id.LoginButton);
	    musername=(EditText)findViewById(R.id.userNameLogin);
	    mpassword=(EditText)findViewById(R.id.passwordLogin);
	    
	    
	    signUptextView.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent newPage = new Intent(LogInActivity.this, SignUpActivity.class);
				startActivity(newPage);
				
			}
		});
		
	    
	    
	    loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				    String username=musername.getText().toString();
				    String password=mpassword.getText().toString();
				    
				    username=username.trim();
				    password=password.trim();
				    
				    if( password.isEmpty() || username.isEmpty()){
				    	AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
				    	builder.setMessage(R.string.loginDialogMessage_error);
				    	builder.setTitle(R.string.loginDialogTitle_error);
				    	builder.setPositiveButton(android.R.string.ok, null);
				    	
				    	AlertDialog dialog= builder.create();
				    	dialog.show();
				    	
				    }
				    
				    else{
				    	//Enters the mainActivity
				    	//Entra a pesquisa no parse, para ver se esse usuario existe ou nao.
				    	
				    	setProgressBarIndeterminateVisibility(true);
				    	ParseUser.logInInBackground(username, password,new LogInCallback() {
							
							@Override
							public void done(ParseUser user, ParseException e) {
								// TODO Auto-generated method stub
						    	setProgressBarIndeterminateVisibility(false);
								if(e==null){
									//Sucess
									Intent newPage = new Intent(LogInActivity.this,MainActivity.class);
									newPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									newPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(newPage);
									
								}
								
								else{
									//Fail
									AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
							    	builder.setMessage(R.string.failDialogMessage_error);
							    	builder.setTitle(R.string.loginDialogTitle_error);
							    	builder.setPositiveButton(android.R.string.ok, null);
							    	
							    	AlertDialog dialog= builder.create();
							    	dialog.show();

									
								}
								
								
							}
						});
				    	
				    }
				    
				    
				
				
				
			}
		});
	    
	    
	  
	   
	   
	
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_log_in,
//					container, false);
//			return rootView;
//		}
//	}

}
