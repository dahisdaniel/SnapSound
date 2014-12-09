package com.example.snapsound;

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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends ActionBarActivity {
	protected TextView  signUptextView;
	protected EditText  musername;
	protected  EditText mpassword;
	protected Button signUpButton;
	protected EditText memail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_sign_up);
// Deixar essa parte comentada, pois ele ta acessando o fragment.
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		
		
		
		
		
		    
		    signUpButton=(Button)findViewById(R.id.SignUpbutton);
		    musername=(EditText)findViewById(R.id.userNameSignUp);
		    mpassword=(EditText)findViewById(R.id.passwordSignup);
		    memail=(EditText)findViewById(R.id.emailSignUp);
		    
		
		    
		    
		    signUpButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					    String username=musername.getText().toString();
					    String password=mpassword.getText().toString();
					    String email=memail.getText().toString();
					    
					    username=username.trim();
					    password=password.trim();
					    email=email.trim();
					    
					    if( password.isEmpty() || username.isEmpty() || email.isEmpty()){
					    	AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					    	builder.setMessage(R.string.loginDialogMessage_error);
					    	builder.setTitle(R.string.loginDialogTitle_error);
					    	builder.setPositiveButton(android.R.string.ok, null);
					    	
					    	AlertDialog dialog= builder.create();
					    	dialog.show();
					    	
					    }
					    
					    else{
					    	//Enters the main activity
					    	//Cria um novo User
					    	ParseUser user= new ParseUser();
					    	user.setUsername(username);
					    	user.setPassword(password);
					    	user.setEmail(email);
					    	setProgressBarIndeterminateVisibility(true);
					    	user.signUpInBackground(new SignUpCallback(){

								@Override
								public void done(ParseException e) {
									// TODO Auto-generated method stub
									setProgressBarIndeterminateVisibility(false);
									if(e==null){
										//Sucess
										Intent newPage = new Intent(SignUpActivity.this,MainActivity.class);
										newPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										newPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
										startActivity(newPage);
										Toast.makeText(SignUpActivity.this, "Done!", Toast.LENGTH_SHORT).show();
									}
									else{
										//Fail to Register
									}
									
								}
					    		
					    		
					    		
					    		
					    	});
					    	
					    }
					    
					    
					
					
					
				}
			});

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
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
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_sign_up,
					container, false);
			return rootView;
		}
	}

}
