package com.androidhive.loginandregister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.androidhive.loginandregister.RegisterActivity.SendMailClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    
	EditText roll;
	EditText pass;
	Button btnlogin;
	
	boolean flag = true;
	String err;
	byte[] data;
	String arr[];
	
	HttpPost httppost;
	StringBuffer buffer;
	HttpClient httpclient;
	HttpResponse response;
	InputStream input;
	
	List<NameValuePair> nameValuePairs;
	private ProgressDialog progressDialog;
	SharedPreferences preferences;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        roll = (EditText)findViewById(R.id.roll);
        pass = (EditText)findViewById(R.id.pass);
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        btnlogin = (Button)findViewById(R.id.btnLogin);
        Log.d("roll",roll.getText().toString().trim());
        Log.d("passcode",pass.getText().toString().trim());
        progressDialog = new ProgressDialog(this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(true);
        btnlogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				httpclient= new DefaultHttpClient();
				httppost = new HttpPost("http://"+"10.2.2.125"+"/login.php");
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("RollNo", roll.getText().toString().trim()));
				nameValuePairs.add(new BasicNameValuePair("Passcode",pass.getText().toString().trim()));
				new Verification().execute(nameValuePairs);
			}
		});
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivityForResult(i, 1);
			}
		});
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1)
		{
			if(resultCode == 0)
			{
				LoginActivity.this.finish();
			}
		}
	}
	public class Verification extends AsyncTask<java.util.List<NameValuePair>, Void, Void>{

		private volatile boolean running = true;
		
		public Verification(){
			 progressDialog.setCancelable(true);
		        progressDialog.setOnCancelListener(new OnCancelListener() {
		            public void onCancel(DialogInterface dialog) {
		                // actually could set running = false; right here, but I'll
		                // stick to contract.
		                cancel(true);
		            }
		        });
		}
		protected void onCancelled() {
	        running = false;
	    }
		protected Void doInBackground(java.util.List<NameValuePair>... params) {
			while(running)
			{
			try{
				httppost.setEntity(new UrlEncodedFormEntity(params[0])); 
	            response = httpclient.execute(httppost);
	            input = response.getEntity().getContent();
				
				data = new byte[256];
	          	arr = new String [50];
	            buffer = new StringBuffer();
	               
	                    int len = 0;
	                    while (-1 != (len = input.read(data)) )
	                    {
	                        buffer.append(new String(data, 0, len));
	                        
	                    }
	                    String bf = buffer.toString();
	                    arr = bf.split("\n");
	                    input.close();       
	                    for(int j=0;j<arr.length;j++)
	                    {
	                    	Log.d("array", arr[j]);
	                    }
	                    
				Log.d("main", "processed");	
				Log.d("arr[0]", arr[0]);
				running = false;
			
			}
			catch (Exception e)
	        {
				Log.d("catch", e.toString());
				flag=false;
				err = e.toString();
				running = false;
	           // Toast.makeText(getApplicationContext(), "error"+e.toString(), Toast.LENGTH_LONG).show();
	        }			
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(!flag)
			{
			Toast.makeText(getApplicationContext(),err, Toast.LENGTH_LONG).show();
			flag = true;
			}
			else 			
				if(arr[0].equals("1"))
				{
					preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					Editor edit = preferences.edit();
					edit.putString("RollNo", roll.getText().toString().trim());
					Log.d("rollnumber", roll.getText().toString().trim());
					edit.putString("Server", "10.2.2.125");
					edit.putString("Passcode", pass.getText().toString().trim());
					edit.putString("Email", arr[2]);
					Log.d("arr[2]", arr[2]);
					edit.putString("Room", arr[3]);
					Log.d("arr[3]", arr[3]);
					edit.putBoolean("registered", false);
					edit.putBoolean("FirstTime", false);
					edit.commit();
					Intent tabsintent = new Intent(LoginActivity.this,Tabs.class);
					startActivity(tabsintent);
					LoginActivity.this.finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Invalid roll/passcode", Toast.LENGTH_LONG).show();
				}
			}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("Connecting server...");
		}		
	}

}