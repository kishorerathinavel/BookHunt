package com.androidhive.loginandregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SearchISBNActivity extends Activity{
	
	EditText isbnNo;
	Button submit;
	String isbn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		isbnNo = (EditText)findViewById(R.id.isbnNo);
		submit = (Button)findViewById(R.id.fetch);
		
		submit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isbn = isbnNo.getText().toString().trim();
				Intent bookIntent = new Intent(SearchISBNActivity.this,GooglebookssampleActivity.class);
				bookIntent.putExtra("query", isbn);
				startActivity(bookIntent);
				setResult(RESULT_OK);
				SearchISBNActivity.this.finish();
				
			}
		});
	}
	
	

}
