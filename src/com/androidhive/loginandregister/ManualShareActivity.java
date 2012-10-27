package com.androidhive.loginandregister;

import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ManualShareActivity extends Activity
{

	SharedPreferences preferences;
	TextView title;
    TextView author;
	ImageView bookcover;
	TextView publisher;
	TextView pages;
	TextView description;
	TextView ratings;
	TextView category;
	TextView isbncode;
	Button onShare;
	EditText price;
	String err;
	boolean flag = true;
	String bookTitle = "";
	String bookAuthor = "";
	String bookPublisher = "";
	String bookPages = "";
	String bookDescription = "";
	String bookCategory = "";
	String bookISBN = "";
	Bitmap bmp;
	String bookRatings= "";
	String infoLinks = "";
	String bf="";
	private SharedBooksDbAdapter mDbHelper;
	byte[] data;
	private ProgressDialog progressDialog;
	HttpPost httppost;
	StringBuffer buffer;
	HttpClient httpclient;
	HttpResponse response;
	InputStream input;
	public static String arr[] = new String [50];
	
	java.util.List<NameValuePair> nameValuePairs;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookdisplay);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		price = (EditText)findViewById(R.id.price);
		Bundle extras = getIntent().getExtras();
		bookTitle = extras.getString("bookTitle");
		bookAuthor = extras.getString("bookAuthor");
		bookPublisher = extras.getString("bookPublisher");
		bookISBN = extras.getString("bookISBN");
		bookDescription = extras.getString("bookDescription");
		Log.d("title", bookTitle);
		Log.d("bokAuthor", bookAuthor);
		Log.d("title", bookPublisher);
		Log.d("title", bookISBN);
		title = (TextView)findViewById(R.id.title);
		author = (TextView)findViewById(R.id.author);
		description = (TextView)findViewById(R.id.description);
		ratings = (TextView)findViewById(R.id.rating);
		bookcover = (ImageView)findViewById(R.id.bookcover);
		publisher = (TextView)findViewById(R.id.publisher);
		pages = (TextView)findViewById(R.id.pages);
		category = (TextView)findViewById(R.id.categories);
		isbncode = (TextView)findViewById(R.id.isbncode);
		onShare = (Button)findViewById(R.id.onShare);
		
		onShare.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToCheck();
			}
		});
		
		title.setText(bookTitle);
		author.setText(bookAuthor);
		publisher.setText(bookPublisher);
		isbncode.setText(bookISBN);
		description.setText(bookDescription);
		ratings.setText(bookRatings);
		category.setText(bookCategory);
		pages.setText(bookPages);
		bookcover.setImageResource(R.drawable.thumbnail);
		
		progressDialog = new ProgressDialog(this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(false);
		
	    mDbHelper = new SharedBooksDbAdapter(this);
		mDbHelper.open();
		
	}
	
	/*public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.book_menu, menu);
		return true;
	}
	*/

	public void putOnShare()
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	String RollNo = preferences.getString("RollNo", null);
    	String Email = preferences.getString("Email", null);
    	String Room = preferences.getString("Room", null);
    	httpclient= new DefaultHttpClient();
		httppost = new HttpPost("http://10.2.2.125/Market.php");
		nameValuePairs = new ArrayList<NameValuePair>(10);
		nameValuePairs.add(new BasicNameValuePair("RollNo",RollNo ));
		nameValuePairs.add(new BasicNameValuePair("Email", Email));
		nameValuePairs.add(new BasicNameValuePair("Room", Room));
		nameValuePairs.add(new BasicNameValuePair("Title", title.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("Author", author.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("ISBN", isbncode.getText().toString()));
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
		nameValuePairs.add(new BasicNameValuePair("shareDate", currentDateTimeString));
		nameValuePairs.add(new BasicNameValuePair("lend", "0"));
		nameValuePairs.add(new BasicNameValuePair("borrow", "1"));
		nameValuePairs.add(new BasicNameValuePair("price",price.getText().toString().trim() ));
		
		new BooksShare().execute(nameValuePairs);
	}
	public void onPause()
	{
		super.onPause();
		mDbHelper.close();
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
	
	
	
	/*public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        case R.id.BookShare:
        	goToCheck();
    		return true;

		}

		return false;
	}*/
	public class BooksShare extends AsyncTask<java.util.List<NameValuePair>, Void, Void>{

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
	                    	String desc;
	                    	if(description.getText().toString().length()>30)
	                    	{
	                    		desc = description.getText().toString().substring(0, 150)+"...";
	                    	}
	                    	else
	                    		desc = description.getText().toString();
	                    	
	                    	String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
	                    		mDbHelper.createEntry(title.getText().toString(), author.getText().toString(), currentDateTimeString, bookISBN,price.getText().toString());
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
			ManualShareActivity.this.finish();
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
					putOnShare();
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
	
}