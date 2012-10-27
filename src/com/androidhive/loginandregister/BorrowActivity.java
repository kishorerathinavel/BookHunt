package com.androidhive.loginandregister;

import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.androidhive.loginandregister.ManualShareActivity.BooksShare;
import com.androidhive.loginandregister.ManualShareActivity.CheckStart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

public class BorrowActivity extends Activity implements TextWatcher{


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
	private BorrowBooksDbAdapter mDbHelper;
	
	EditText bookTitle;
	EditText bookAuthor;
	EditText isbnedit;
	Button done;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrowform);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		bookTitle = (EditText)findViewById(R.id.bookTitle);
		bookAuthor = (EditText)findViewById(R.id.bookAuthor);
		isbnedit = (EditText)findViewById(R.id.isbnedit);
		done = (Button)findViewById(R.id.done);
		bookTitle.addTextChangedListener(this);
		
		mDbHelper = new BorrowBooksDbAdapter(this);
		mDbHelper.open();
		
		progressDialog = new ProgressDialog(this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(false);
		
		done.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToCheck();
			}
		});
		
		
	}

	public void goToCheck(){
		String Rollno = preferences.getString("RollNo",null);
		String Server = preferences.getString("Server", null);
		String passcode = preferences.getString("Passcode", null);
		
		if(Rollno==null || Server == null || passcode == null)
		{
			
		Toast.makeText(getApplicationContext(), "Empty fields in settings", Toast.LENGTH_LONG).show();
		}
		else {			
						String roll = Rollno.toString();
						Rollno = preferences.getString("RollNo",null);
			    		roll = Rollno.toString();
						
						httpclient= new DefaultHttpClient();
						httppost = new HttpPost("http://"+"10.2.2.125"+"/login.php");
						nameValuePairs = new ArrayList<NameValuePair>(2);
						nameValuePairs.add(new BasicNameValuePair("RollNo", roll.trim()));
						nameValuePairs.add(new BasicNameValuePair("Passcode",passcode.trim()));
						new CheckStart().execute(nameValuePairs);
		}
	}
	
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void putOnWishList()
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	String RollNo = preferences.getString("RollNo", null);
    	String Email = preferences.getString("Email", null);
    	String Room = preferences.getString("Room", null);
    	httpclient= new DefaultHttpClient();
		httppost = new HttpPost("http://10.2.2.125/Market.php");
		nameValuePairs = new ArrayList<NameValuePair>(10);
		nameValuePairs.add(new BasicNameValuePair("RollNo",RollNo.toString() ));
		nameValuePairs.add(new BasicNameValuePair("Email", Email));
		nameValuePairs.add(new BasicNameValuePair("Room", Room));
		nameValuePairs.add(new BasicNameValuePair("Title", bookTitle.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("Author", bookAuthor.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("ISBN", isbnedit.getText().toString()));
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
		nameValuePairs.add(new BasicNameValuePair("shareDate", currentDateTimeString));
		nameValuePairs.add(new BasicNameValuePair("lend", "1"));
		nameValuePairs.add(new BasicNameValuePair("borrow", "0"));
		nameValuePairs.add(new BasicNameValuePair("price", ""));
		new BooksBorrow().execute(nameValuePairs);
	}
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if(bookTitle.getText().toString().trim().length()==0)
			bookTitle.setError("this field cannot be empty");
		if(bookAuthor.getText().toString().trim().length()==0)
			bookAuthor.setError("this field cannot be empty");
			
	}
	public class CheckStart extends AsyncTask<java.util.List<NameValuePair>, Void, Void>{

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
				/*if (preferences.getInt("numberOfbooks", 0)>0)
		        {
		        	Log.d("in condition", numberOfbooks+"");
		        	Log.d("preference number of books", preferences.getInt("numberOfbooks", 0)+"");
		        	for(int p=0;p<preferences.getInt("numberOfbooks",0);p++)
		        	{
		        		Log.d("CalSync.getIndex", CalSync.getIndex[p]);
		        		obj.DeleteCalendarEntry(Integer.parseInt(CalSync.getIndex[p]));
		        	}
		        }*/
				
				flag=true;
			}
			catch (Exception e)
	        {
				Log.d("catch", e.toString());
				flag=false;
				err = e.toString();
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
				if (arr[0].equalsIgnoreCase("1"))
				{
					putOnWishList();
				}
				else
				Toast.makeText(getApplicationContext(), "Credentials are incorrect\nPlease check the settings", Toast.LENGTH_LONG).show();
			}
			}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("Validating credentials...");
		}
		
	}
	
	public class BooksBorrow extends AsyncTask<java.util.List<NameValuePair>, Void, Void>{

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
	                    
	                    if (arr[0].equals("1"))
	                    {    	
	                    	String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
	                    	mDbHelper.createEntry(bookTitle.getText().toString(), bookAuthor.getText().toString(), currentDateTimeString, isbnedit.getText().toString().trim());
	                    }              	
			}
			catch (Exception e)
	        {
				Log.d("catch", e.toString());
				flag = false;
				err = e.toString();
	            //Toast.makeText(getApplicationContext(), "error"+e.toString(), Toast.LENGTH_LONG).show();
	        }
			
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(flag)
			{
			Toast.makeText(getApplicationContext(),arr[1], Toast.LENGTH_LONG).show();
			setResult(RESULT_OK);
			BorrowActivity.this.finish();
			}else
			{
				Toast.makeText(getApplicationContext(),err , Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("Contacting server for sharing...");
		}
		
	}
}
