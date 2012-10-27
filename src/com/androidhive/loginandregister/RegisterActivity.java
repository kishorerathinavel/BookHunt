package com.androidhive.loginandregister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements TextWatcher{
	
	
	private static final int REQUEST_CODE = 1;
	EditText reg_rollnum;
	EditText reg_name;
	EditText reg_email;
	EditText room;
	Button register;
	
	int random;
	String code;
	int max = 9999;
	int min = 1000;
	
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
        
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        reg_rollnum = (EditText)findViewById(R.id.reg_rollnum);
        reg_name= (EditText)findViewById(R.id.reg_name);
        reg_email = (EditText)findViewById(R.id.reg_email);
        register = (Button)findViewById(R.id.btnRegister);
        room = (EditText)findViewById(R.id.room);
        
        reg_rollnum.addTextChangedListener(this);
        reg_name.addTextChangedListener(this);
        reg_email.addTextChangedListener(this);
        room.addTextChangedListener(this);
        room.addTextChangedListener(this);
        progressDialog = new ProgressDialog(this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(true);
        
        register.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(reg_rollnum.getText().toString().length()!=0 &&
						reg_name.getText().toString().length()!=0 &&
						reg_email.getText().toString().length()!=0 &&
						room.getText().toString().length()!=0)
				{
					validate();
				}
			}
		});
        
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// Switching to Login Screen/closing register screen
				finish();
			}
		});
    }

    public void onPause()
    {
    	super.onPause();
    	//setResult(0);
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE)
		{
			if(resultCode == 0)
				RegisterActivity.this.finish();
		}
	}

	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if(reg_rollnum.getText().toString().length()==0)
			reg_rollnum.setError("this field cannot be empty");
		if(reg_email.getText().toString().length()==0)
			reg_email.setError("this field cannot be empty");
		if(reg_name.getText().toString().length()==0)
			reg_name.setError("this field cannot be empty");
		if(room.getText().toString().length() == 0)
			room.setError("this field cannot be empty");
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	public void sendmail()
	{
		Random r = new Random();
		random = r.nextInt(max-min+1)+min;
		code = Integer.toString(random);
		httpclient= new DefaultHttpClient();
		
		String message = "Dear "+ reg_name.getText().toString().trim()+"," + "\n" + 
		"The confirmation code is "+ random ;
		httppost = new HttpPost("http://"+"10.2.2.125"+"/send_mail.php");
		nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", reg_email.getText().toString().trim()));
		nameValuePairs.add(new BasicNameValuePair("message",message.trim()));
		new SendMailClass().execute(nameValuePairs);
	}
	
	public class SendMailClass extends AsyncTask<java.util.List<NameValuePair>, Void, Void>{

		private volatile boolean running = true;
		
		public SendMailClass(){
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
				Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
			Intent newIntent = new Intent(RegisterActivity.this,ConfirmationActivity.class);
			newIntent.putExtra("code", code);
			newIntent.putExtra("roll", reg_rollnum.getText().toString().trim());
			newIntent.putExtra("email",reg_email.getText().toString().trim());
			newIntent.putExtra("name",reg_name.getText().toString().trim());
			newIntent.putExtra("room",room.getText().toString().trim());
			setResult(0);
			startActivityForResult(newIntent,REQUEST_CODE);
			}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("Connecting server...");
		}		
	}
	public void validate()
	{
		httpclient= new DefaultHttpClient();
		httppost = new HttpPost("http://"+"10.2.2.125"+"/user_credentials.php");
		nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("RollNo", reg_rollnum.getText().toString().trim()));
		new Check().execute(nameValuePairs);
	}
	
	public class Check extends AsyncTask<java.util.List<NameValuePair>, Void, Void>{

		private volatile boolean running = true;
		
		public Check(){
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
			else if(arr[0].equals("0"))
			{
				sendmail();
			}
			}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("searching...");
		}		
	}
	
	
}