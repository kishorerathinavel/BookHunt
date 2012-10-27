package com.androidhive.loginandregister;

import org.apache.http.NameValuePair;

import com.library.zxing.IntentIntegrator;
import com.library.zxing.IntentResult;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Lend extends ListActivity {
	
	ImageButton addLend;
	java.util.List<NameValuePair> nameValuePairs;
	private SharedBooksDbAdapter mDbHelper1;
	
	SharedPreferences preferences;
	String isbn;
	Cursor c;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend);
        mDbHelper1 = new SharedBooksDbAdapter(this);
		mDbHelper1.open();
        //String [] mString= new String[]{"Hei","HI","Bye"};
        //int to [] = new int[] {R.id.text1,R.id.text2,R.id.text3};
        //setListAdapter(new ArrayAdapter<String>(this,R.layout.libraryrow, mString));
        //getListView().setTextFilterEnabled(true);
        addLend = (ImageButton)findViewById(R.id.addLend);
        fillSharedBookData();
        mDbHelper1.close();
        addLend.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final CharSequence[] items = {"Scan ISBN", "Search by ISBN", "Manual Entry"};

	        	AlertDialog.Builder builder = new AlertDialog.Builder(Lend.this);
	        	builder.setTitle("Get Book Details");
	        	builder.setItems(items, new DialogInterface.OnClickListener() {
	        	    public void onClick(DialogInterface dialog, int item) {
	        	        switch (item)
	        	        {
	        	        case 0:
	        	        	/*final boolean scanAvailable = isIntentAvailable(getApplicationContext(),"com.google.zxing.client.android.SCAN");
	        	        	if(scanAvailable)
	        	        	{
	        	        		Log.d("into if", "into if");
	        	        		useScanner(4);
	        	        	}
	        	        	else
	        	        	{*/
	        	        		Log.d("into else", "into else");
	        	            	IntentIntegrator integrator = new IntentIntegrator(Lend.this);
	        	    			integrator.initiateScan();
	        	    			
	        	        	//}
	        	        	/*Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        	            intent.setPackage("com.google.zxing.client.android");
	        	            //intent.putExtra("SCAN_MODE", "ONE_D_MODE");
	        	            startActivityForResult(intent, 4);*/
	        	            
	        	        	/*IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
	        				integrator.initiateScan();
	        				integrator.setMessageByID(4);*/
	        	            break;
	        	        case 1:
	        	        	Intent intent1 = new Intent(Lend.this,SearchISBNActivity.class);
	        	        	startActivityForResult(intent1,5);
	        	        	break;
	        	        case 2:
	        	        	Intent manual = new Intent(Lend.this, ManualActivity.class);
	        	        	startActivityForResult(manual,6);
	        	        }
	        	    }
	        	});
	        	AlertDialog alert = builder.create();
	        	alert.show();
	        	//Intent bookShare = new Intent(MainActivity.this,SharedBooks.class);
	        	//startActivity(bookShare);
			}
		});
       
    }
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == IntentIntegrator.REQUEST_CODE)
		{
			Log.d("into 4", "working");
			String contents = null;
			if (resultCode == RESULT_OK) {
	             
	             IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
	 			Log.d("scan results", scanResult.getContents()); 
	 			if (scanResult != null) {
	 				isbn = scanResult.getContents();
	 				Intent internet = new Intent(Lend.this,GooglebookssampleActivity.class);
			    		internet.putExtra("query",isbn);
			    		startActivityForResult(internet,1);
	 			}
	 			else
	 			{
	 				Toast.makeText(getApplicationContext(), "nuthing", Toast.LENGTH_LONG).show();
	 			}
			}
			else if (resultCode == RESULT_CANCELED) {
	            // Handle cancel
	        	Log.d("cancelled", "cancel");
	        	Log.i("Barcode Result", "Result cancelled");
	            // Handle successful scan
	        } 
		}		
	}
	
	private void fillSharedBookData(){
		c = mDbHelper1.fetchAllEntries();
		startManagingCursor(c);

		// loop through cursor 
		while(c.moveToNext()) {
		  //  allTitles= c.getString(c.getColumnIndex("_id"));
		   // Log.d("allTitles", allTitles+"");
	}
		String [] from = new String [] {SharedBooksDbAdapter.BOOK_TITLE,SharedBooksDbAdapter.BOOK_AUTHOR,SharedBooksDbAdapter.SHARING_DATE};
		int to [] = new int [] {R.id.bt,R.id.ba,R.id.bd};
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.bookssharedrow, c, from, to);
		setListAdapter(notes);
		registerForContextMenu(getListView());
	}
    public void onResume(){
    	super.onResume();
    	Tabs.tabHost.getTabWidget().getChildAt(0).setVisibility(View.VISIBLE);
        Tabs.tabHost.getTabWidget().getChildAt(1).setVisibility(View.VISIBLE);
        Tabs.tabHost.getTabWidget().getChildAt(2).setVisibility(View.GONE);
        mDbHelper1.open();
		fillSharedBookData();
		mDbHelper1.close();
    
    }	
    protected void onStop() {
	    try {
	      super.onStop();

	      if ( c!= null) {
	        c.close();
	      }

	      if (mDbHelper1 != null) {
	        mDbHelper1.close();
	      }
	    } catch (Exception error) {
	      /** Error Handler Code **/
	    }// end try/catch (Exception error)
	  }// end onStop

}
