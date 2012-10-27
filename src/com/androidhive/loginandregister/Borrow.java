        		  
package com.androidhive.loginandregister;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

public class Borrow extends ListActivity {
	
	private BorrowBooksDbAdapter mDbHelper1;
	Cursor c;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1)
			if(resultCode == RESULT_OK)
			{
				
			}
				
	}

	ImageButton addBorrow;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow);    

        addBorrow = (ImageButton)findViewById(R.id.addBorrow);
        mDbHelper1 = new BorrowBooksDbAdapter(this);
		mDbHelper1.open();
		fillSharedBookData();
        addBorrow.setOnClickListener(new OnClickListener() {
        	
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent borrowIntent = new Intent(Borrow.this,BorrowActivity.class);
				startActivityForResult(borrowIntent, 1);
			}
		});
    }
    
    public void onResume(){
    	super.onResume();
    	Tabs.tabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
        Tabs.tabHost.getTabWidget().getChildAt(1).setVisibility(View.VISIBLE);
        Tabs.tabHost.getTabWidget().getChildAt(2).setVisibility(View.VISIBLE);}	
    	
    private void fillSharedBookData(){
		c = mDbHelper1.fetchAllEntries();
		startManagingCursor(c);

		// loop through cursor 
		while(c.moveToNext()) {
		  //  allTitles= c.getString(c.getColumnIndex("_id"));
		   // Log.d("allTitles", allTitles+"");
	}
		String [] from = new String [] {BorrowBooksDbAdapter.BOOK_TITLE,BorrowBooksDbAdapter.BOOK_AUTHOR,BorrowBooksDbAdapter.SHARING_DATE};
		int to [] = new int [] {R.id.bt,R.id.ba,R.id.bd};
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.bookssharedrow, c, from, to);
		setListAdapter(notes);
		registerForContextMenu(getListView());
	}
}




