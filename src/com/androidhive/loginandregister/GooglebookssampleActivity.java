package com.androidhive.loginandregister;

import java.io.InputStream;
import java.net.URLEncoder;
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
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.VolumeVolumeInfo;
import com.google.api.services.books.model.VolumeVolumeInfoImageLinks;
import com.google.api.services.books.model.Volumes;

public class GooglebookssampleActivity extends Activity {
    /** Called when the activity is first created. */
    TextView title;
    TextView author;
	ImageView bookcover;
	TextView publisher;
	TextView pages;
	TextView description;
	TextView ratings;
	TextView category;
	TextView isbncode;
	EditText price;
	Button onShare;
	private ProgressDialog progressDialog;
	
	String isbnNo,err;
	boolean flag = true;
	
	byte[] data;
	
	SharedPreferences preferences;
	
	HttpPost httppost;
	StringBuffer buffer;
	HttpClient httpclient;
	HttpResponse response;
	InputStream input;
	public static String arr[] = new String [50];
	
	java.util.List<NameValuePair> nameValuePairs;
	
	String bookTitle = "";
	String bookAuthor = "";
	String bookPublisher = "";
	String bookPages = "";
	String bookDescription = "";
	String bookCategory = "";
	Bitmap bmp;
	String bookRatings= "";
	String infoLinks = "";
	
	private SharedBooksDbAdapter mDbHelper;
	
	
	public void finish() {
		super.finish();
	}


	/**
	 * A sample application that demonstrates how Google Books Client Library for
	 * Java can be used to query Google Books. It accepts queries in the command
	 * line, and prints the results to the console.
	 *
	 * $ java com.google.sample.books.BooksSample [--author|--isbn|--title] "<query>"
	 *
	 * Please start by reviewing the Google Books API documentation at:
	 * http://code.google.com/apis/books/docs/getting_started.html
	 */
//	  private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
//	  private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();

	  public void queryGoogleBooks(JsonFactory jsonFactory, String query) throws Exception {
	    // Set up Books client.
	    final Books books = Books.builder(new NetHttpTransport(), jsonFactory)
	        .setApplicationName("Google-BooksSample/1.0")
	        .setJsonHttpRequestInitializer(new JsonHttpRequestInitializer() {
	          public void initialize(JsonHttpRequest request) {
//	            BooksRequest booksRequest = (BooksRequest) request;
	          //  booksRequest.setKey(ClientCredentials.KEY);
	          }
	        })
	        .build();
	    
	    List volumesList = books.volumes().list(query);
	   // volumesList.setFilter("ebooks");
	    
	    // Execute the query.
	    Volumes volumes = volumesList.execute();
	    if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
	      //Toast.makeText(getApplicationContext(), "no matches" , Toast.LENGTH_LONG).show();
	      return;
	    }
	    // Output results.
	    for (Volume volume : volumes.getItems()) {
	      VolumeVolumeInfo volumeInfo = volume.getVolumeInfo();
	      bookTitle = volumeInfo.getTitle();
	      //title.setText(volumeInfo.getTitle());
	      //Toast.makeText(getApplicationContext(), volumeInfo.getTitle(), Toast.LENGTH_LONG).show();
	      // Author(s).
	      
	      java.util.List<String> authors = volumeInfo.getAuthors();
	      if (authors != null && !authors.isEmpty()) {
	    	  //author.setText("");
	    	  bookAuthor = "";
	          System.out.print("Author(s): ");
	          for (int i = 0; i < authors.size(); ++i) {
	         //author.append(authors.get(i));
	        	  bookAuthor+=authors.get(i);
	  //          System.out.print(authors.get(i));
	        	  if (i < authors.size() - 1) {
	        	 // author.append(",");
	        		  bookAuthor+=",";
	  //          System.out.print(", ");
	        	  }
	        }
	  //       System.out.println();
	      }
	      infoLinks = volumeInfo.getInfoLink();
	      //isbncode.setText("ISBN: "+isbnNo);
	      java.util.List<String> categories = volumeInfo.getCategories();
	      if(categories !=null && !categories.isEmpty()){
	    	//  category.setText("");
	    	  bookCategory = "";
	    	  for(int i=0;i<categories.size();++i)
	    	  {
	    		  bookCategory +=categories.get(i);
	    		  //category.append(categories.get(i));
	    		  if(i<categories.size() -1){
	    			  bookCategory+= ",";
	    			  category.append(",");
	    		  }
	    	  }
	      }
	      if(volumeInfo.getPageCount()!=null){
	    	  int number = volumeInfo.getPageCount();
	    	 // pages.setText(number+" pages");
	    	  bookPages=number+"";
	    	  
	      }
	      
          if(volumeInfo.getImageLinks()!=null)
          {
	      VolumeVolumeInfoImageLinks image = volumeInfo.getImageLinks();
	      if(image.getThumbnail()!= null){
	      bmp = BitmapFactory.decodeStream(new java.net.URL(image.getThumbnail()).openStream());
	      //bookcover.setImageBitmap(bmp);
	      }
          }
          else{
        	  bmp = BitmapFactory.decodeResource(getResources(), R.drawable.thumbnail);
          }
          if(volumeInfo.getPublisher()!=null && volumeInfo.getPublisher().length()>0){
        	  //publisher.setText(volumeInfo.getPublisher());
        	  bookPublisher = volumeInfo.getPublisher();
          }
          
          
	      // Description (if any).
	      if (volumeInfo.getDescription() != null && volumeInfo.getDescription().length() > 0) {
	        System.out.println("Description: " + volumeInfo.getDescription());
	        bookDescription = volumeInfo.getDescription();
	        //description.setText(volumeInfo.getDescription());
	      }
	      // Ratings (if any).
	      if (volumeInfo.getRatingsCount() != null && volumeInfo.getRatingsCount() > 0) {
	        int fullRating = (int) Math.round(volumeInfo.getAverageRating().doubleValue());
	        System.out.print("User Rating: ");
	        //ratings.setText("");
	        bookRatings = "";
	        for (int i = 0; i < fullRating; ++i) {
	        	//ratings.append("*");
	        	bookRatings+="*";
	          System.out.print("*");
	        }
	        if (fullRating == 0)
	        	//ratings.setText("no ratings");
	        	bookRatings = "0";
	        System.out.println(" (" + volumeInfo.getRatingsCount() + " rating(s))");
	      }
	      
	      // Price (if any).
	    /*  if ("FOR_SALE".equals(saleInfo.getSaleability())) {
	        double save = saleInfo.getListPrice().getAmount() - saleInfo.getRetailPrice().getAmount();
	        if (save > 0.0) {
	          System.out.print("List: " + CURRENCY_FORMATTER.format(saleInfo.getListPrice().getAmount())
	              + "  ");
	        }
	        System.out.print("Google eBooks Price: "
	            + CURRENCY_FORMATTER.format(saleInfo.getRetailPrice().getAmount()));
	        if (save > 0.0) {
	          System.out.print("  You Save: " + CURRENCY_FORMATTER.format(save) + " ("
	              + PERCENT_FORMATTER.format(save / saleInfo.getListPrice().getAmount()) + ")");
	        }
	        System.out.println();
	      }*/
	      // Access status.
	     /* String accessViewStatus = volume.getAccessInfo().getAccessViewStatus();
	      String message = "Additional information about this book is available from Google eBooks at:";
	      if ("FULL_PUBLIC_DOMAIN".equals(accessViewStatus)) {
	        message = "This public domain book is available for free from Google eBooks at:";
	      } else if ("SAMPLE".equals(accessViewStatus)) {
	        message = "A preview of this book is available from Google eBooks at:";
	      }
	      System.out.println(message);
	      // Link to Google eBooks.
	      System.out.println(volumeInfo.getInfoLink());*/
	    }
	    System.out.println("==========");
	    System.out.println(
	        volumes.getTotalItems() + " total results at http://books.google.com/ebooks?q="
	        + URLEncoder.encode(query, "UTF-8"));
	  }

	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.bookdisplay);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		price = (EditText)findViewById(R.id.price);
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
		
		progressDialog = new ProgressDialog(this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(false);
		
	    mDbHelper = new SharedBooksDbAdapter(this);
		mDbHelper.open();
    	JsonFactory jsonFactory = new JacksonFactory();
	    try {
	      String query = "";
	      Bundle extras = getIntent().getExtras();
	      isbnNo = extras.getString("query"); 
	      isbnNo = isbnNo.trim();
	      query = "isbn:"+ isbnNo;
	     // try {
	    	  new BookSearch().execute(jsonFactory);
	        //queryGoogleBooks(jsonFactory, query);
	        // Success!
	        return;
	     // } //catch (GoogleJsonResponseException e) {
	        // message already includes parsed response
	       // System.err.println(e.getMessage());
	     // } catch (HttpResponseException e) {
	        // message doesn't include parsed response
	      //  System.err.println(e.getMessage());
	      //  System.err.println(e.getResponse().parseAsString());
	    //  }
	    } catch (Throwable t) {
	      t.printStackTrace();
	    }
	    
	    System.exit(0);
	  }
    protected void onStop() {
	    try {
	      super.onStop();

	      if (mDbHelper != null) {
	        mDbHelper.close();
	      }
	    } catch (Exception error) {
	      /** Error Handler Code **/
	    }// end try/catch (Exception error)
	  }// end onStop
	/*
    public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.book_menu, menu);
		return true;
	}*/

   public void ShareAfterCheck()
   {
	   	
   		String RollNo = preferences.getString("RollNo", null);
   		Log.d("roll before", RollNo);
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
		nameValuePairs.add(new BasicNameValuePair("ISBN", isbnNo.toString()));
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
		nameValuePairs.add(new BasicNameValuePair("shareDate", currentDateTimeString));
		nameValuePairs.add(new BasicNameValuePair("lend", "0"));
		nameValuePairs.add(new BasicNameValuePair("borrow", "1"));
		nameValuePairs.add(new BasicNameValuePair("price",price.getText().toString().trim() ));
		new BooksShare().execute(nameValuePairs);
   }
   /* 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	                    	// textView is the TextView view that should display it
	                    	//textView.setText(currentDateTimeString);
	                    	mDbHelper.open();
	                    	mDbHelper.createEntry(title.getText().toString(), author.getText().toString(), currentDateTimeString, isbnNo, price.getText().toString().trim() );
	                    	
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
			Toast.makeText(getApplicationContext(),arr[1], Toast.LENGTH_LONG).show();
			else
			{
				Toast.makeText(getApplicationContext(),err , Toast.LENGTH_LONG).show();
			}
			GooglebookssampleActivity.this.finish();
        	setResult(RESULT_OK);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("Contacting server for sharing...");
		}
		
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
	
	
    public class BookSearch extends AsyncTask<JsonFactory, Void, Void>{

    	GooglebookssampleActivity obj = new GooglebookssampleActivity();
    	
		protected Void doInBackground(JsonFactory... params){
			// TODO Auto-generated method stub
			
			try {
				queryGoogleBooks(params[0], "isbn:"+isbnNo);
			} 
			catch(GoogleJsonResponseException e){
				System.err.println(e.getMessage());
			}
			catch(HttpResponseException e){
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("fetching book details\nPlease wait...");
			//show();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//hide();
			progressDialog.dismiss();
			if(bookTitle.equals(""))
			{
				Toast.makeText(getApplicationContext(), "no book found", Toast.LENGTH_LONG).show();
				GooglebookssampleActivity.this.finish();
			}
			else{
			title.setText(bookTitle);
			author.setText(bookAuthor);
			publisher.setText(bookPublisher);
			isbncode.setText("ISBN: "+isbnNo);
			category.setText(bookCategory);
			description.setText(bookDescription);
			ratings.setText(bookRatings);
			bookcover.setImageBitmap(bmp);
			Toast.makeText(getApplicationContext(), infoLinks, Toast.LENGTH_LONG).show();
			Log.d("infolinks : ", infoLinks);
			}
			
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
				flag =true;
			Toast.makeText(getApplicationContext(),err, Toast.LENGTH_LONG).show();
			}
			else
			{
				if (arr[0].equalsIgnoreCase("1"))
				{
					ShareAfterCheck();
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
