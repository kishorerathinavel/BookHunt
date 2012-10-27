package com.androidhive.loginandregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ManualActivity extends Activity implements TextWatcher{

	private static final int REQUEST_CODE = 0;
	EditText bookTitle;
	EditText bookAuthor;
	EditText bookPublisher;
	EditText bookISBN;
	EditText bookDesc;
	Button done;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual);
		
		bookTitle = (EditText)findViewById(R.id.bookTitle);
		bookAuthor = (EditText)findViewById(R.id.bookAuthor);
		bookPublisher = (EditText)findViewById(R.id.bookPublisher);
		bookISBN = (EditText)findViewById(R.id.bookISBN);
		bookDesc = (EditText)findViewById(R.id.bookDesc);
		done = (Button)findViewById(R.id.done);
		
		bookTitle.addTextChangedListener(this);
		bookAuthor.addTextChangedListener(this);
		bookISBN.addTextChangedListener(this);
		
		done.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(bookTitle.getText().toString().trim().length()>0 && bookAuthor.getText().toString().trim().length()>0 && bookISBN.getText().toString().length()>0)
				{
				Intent manualshare = new Intent(ManualActivity.this,ManualShareActivity.class);
				manualshare.putExtra("bookTitle", bookTitle.getText().toString().trim());
				manualshare.putExtra("bookAuthor", bookAuthor.getText().toString().trim());
				manualshare.putExtra("bookPublisher", bookPublisher.getText().toString().trim());
				manualshare.putExtra("bookISBN", bookISBN.getText().toString().trim());
				manualshare.putExtra("bookDescription", bookDesc.getText().toString().trim());
				startActivityForResult(manualshare,REQUEST_CODE);
				setResult(RESULT_OK);
				}
			}
		});
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			ManualActivity.this.finish();
		}
	}


	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if(bookTitle.getText().toString().trim().length()==0)
			bookTitle.setError("this field cannot be empty");
		if(bookAuthor.getText().toString().trim().length()==0)
			bookAuthor.setError("this field cannot be empty");
		if(bookISBN.getText().toString().trim().length()==0)
			bookISBN.setError("this field cannot be empty");
	}


	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}


	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}