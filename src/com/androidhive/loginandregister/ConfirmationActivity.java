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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmationActivity extends Activity implements TextWatcher{

	byte[] data;
	String arr[];
	
	HttpPost httppost;
	StringBuffer buffer;
	HttpClient httpclient;
	HttpResponse response;
	InputStream input;
	
	private ProgressDialog progressDialog;
	boolean flag = true;
	String err;
	
	List<NameValuePair> nameValuePairs;
	
	SharedPreferences preferences;
	EditText codeenter;
	Button enter;
	String code;
	String roll,email,room,name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);
		
		codeenter = (EditText)findViewById(R.id.codeenter);
		enter = (Button)findViewById(R.id.enter);
		
		progressDialog = new ProgressDialog(this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(false);
	    
		Bundle extras = getIntent().getExtras();
		code = extras.getString("code");
		roll = extras.getString("roll");
		email = extras.getString("email");
		room = extras.getString("room");
		name = extras.getString("name");
		Log.d("code", code);
		codeenter.addTextChangedListener(this);
		enter.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(codeenter.getText().toString().trim().compareToIgnoreCase(code)==0)
				{
					preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					Editor edit = preferences.edit();
					edit.putString("RollNo", roll);
					edit.putString("Email", email);
					edit.putString("Name", name);
					edit.putString("RoomNo", room);
					edit.putBoolean("registered", false);
					edit.putString("Passcode", code);
					edit.commit();
					httpclient= new DefaultHttpClient();
					httppost = new HttpPost("http://"+"10.2.2.125"+"/makeentry.php");
					nameValuePairs = new ArrayList<NameValuePair>(5);
					nameValuePairs.add(new BasicNameValuePair("RollNo", roll.trim()));
					nameValuePairs.add(new BasicNameValuePair("Email", email.trim()));
					nameValuePairs.add(new BasicNameValuePair("Name", name.trim()));
					nameValuePairs.add(new BasicNameValuePair("room", room.trim()));
					Log.d("name to php is", roll.trim());
					Log.d("name to php is", email.trim());
					Log.d("name to php is", name.trim());
					Log.d("name to php is", code.trim());
					nameValuePairs.add(new BasicNameValuePair("Passcode", code.trim()));
					setResult(0);
					new Check().execute(nameValuePairs);
				}
				else
					Toast.makeText(getApplicationContext(), "The passcode is incorrect", Toast.LENGTH_LONG).show();
			}
		});
		
	}

	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if(codeenter.getText().toString().length()==0)
			codeenter.setError("this field cannot be empty");
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	public class Check extends AsyncTask<java.util.List<NameValuePair>, Void, Void>{

		protected Void doInBackground(java.util.List<NameValuePair>... params) {
			// TODO Auto-generated method stub
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
			
			}
			catch (Exception e)
	        {
				Log.d("catch", e.toString());
				flag=false;
				err = e.toString();
	           // Toast.makeText(getApplicationContext(), "error"+e.toString(), Toast.LENGTH_LONG).show();
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
			}
			else
			{
				Intent main = new Intent(ConfirmationActivity.this,Tabs.class);
				startActivity(main);
				setResult(0);
				ConfirmationActivity.this.finish();
			//Toast.makeText(getApplicationContext(), arr[1], Toast.LENGTH_LONG).show();
			}
			
			}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("searching...");
		}		
	}

}
