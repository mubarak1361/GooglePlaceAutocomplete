package com.example.placeautocomplete;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class PlaceActivity extends Activity implements OnClickListener {

	private AutoCompleteTextView source,destination;
	private PlacesAutoCompleteAdapter Padapter;
	private Button go;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        
        source =  (AutoCompleteTextView) findViewById(R.id.source);
        destination = (AutoCompleteTextView) findViewById(R.id.dest);
        go = (Button) findViewById(R.id.go);
        
        source.setThreshold(3);
        destination.setThreshold(3);
        
        Padapter =  new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1);
        source.setAdapter(Padapter);
        destination.setAdapter(Padapter);
        
        go.setOnClickListener(this);       
       
      
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go:
			try {
			if((source.getText().toString()!= null) && (destination.getText()!=null))
			{
				 DistanceApi dApi = new DistanceApi(this);
			      dApi.execute(new String[]{source.getText().toString(),destination.getText().toString()});
			}
			}catch(NullPointerException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		
	}

}
