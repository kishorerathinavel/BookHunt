package com.androidhive.loginandregister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MarketView extends ListActivity {
	
	
	byte[] data;
	String arr[];
	
	HttpPost httppost;
	StringBuffer buffer;
	HttpClient httpclient;
	HttpResponse response;
	InputStream input;
	ListView list;
	
	private ProgressDialog progressDialog;
	boolean flag = true;
	String err;
	SimpleAdapter mSchedule;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map = new HashMap<String, String>();
	List<NameValuePair> nameValuePairs;
	
	SharedPreferences preferences;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marketview);
        setTitle("MarketView");
        
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        progressDialog = new ProgressDialog(this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(false);
		goToCheck();
		ListView list = (ListView) findViewById(android.R.id.list);
        
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
					putOnMarket();
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

    public void putOnMarket()
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	String RollNo = preferences.getString("RollNo", null);
    	
    	httpclient= new DefaultHttpClient();
		httppost = new HttpPost("http://10.2.2.125/MarketFetch.php");
		nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("RollNo",RollNo.toString() ));
		new MarketUpdate().execute(nameValuePairs);
	}
    
    public void onResume(){
    	super.onResume();
    	Tabs.tabHost.getTabWidget().getChildAt(0).setVisibility(View.VISIBLE);
        Tabs.tabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);
        Tabs.tabHost.getTabWidget().getChildAt(2).setVisibility(View.VISIBLE);}	
    
    public class MarketUpdate extends AsyncTask<java.util.List<NameValuePair>, Void, Void>{

		public Void doInBackground(java.util.List<NameValuePair>... params) {
			// TODO Auto-generated method stub
			try{
				httppost.setEntity(new UrlEncodedFormEntity(params[0])); 
	            response = httpclient.execute(httppost);
	            input = response.getEntity().getContent();
				
				data = new byte[256];
	          	arr = new String [150];
	            buffer = new StringBuffer();
	               
	                    int len = 0;
	                    while (-1 != (len = input.read(data)) )
	                    {
	                        buffer.append(new String(data, 0, len));
	                        
	                    }
	                    String bf = buffer.toString();
	                    Log.d("buffer", bf.toString());
	                    arr = bf.split("\n");
	                    input.close();
	                    int j;
	                    if (arr[0].equals("1"))
	                    {    	
	                    	for(int i=1;i<arr.length;i+=3)
	                    	{	
	                    		
	                    		map = new HashMap<String, String>();
	                    	j=i;
	                    	Log.d("buffer", arr[j].toString());
	                    	map.put("email", arr[j]);
	                    	map.put("title", arr[j+1]);
	                    	map.put("author", arr[j+2]);
	                    	mylist.add(map);
	                    	
	                    	
	                    	// ...
	                    	
	                    	}
	                    	
	                    	
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
		public void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(flag)
			{
				setListAdapter(new SimpleAdapter(getApplicationContext(), mylist, R.layout.bookssharedrow,
        	            new String[] {"email", "title", "author"}, new int[] {R.id.bt, R.id.ba, R.id.bd}));
			Toast.makeText(getApplicationContext(),"updated", Toast.LENGTH_LONG).show();
			setResult(RESULT_OK);
			//Marke.this.finish();
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
