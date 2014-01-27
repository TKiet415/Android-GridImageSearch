package com.codepath.example.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SetFiltersActivity extends Activity {
	
	Spinner spImageSize, spColors, spImageType;
	ArrayAdapter<CharSequence> imageSizeAdapter, colorsAdapter, imageTypeAdapter;
	EditText etSiteFilter, etSearchFor;
	int prefs;
	
	public static final String imageSize = "i_size",
		color = "c",
		imageType = "i_type",
		filterSite = "some_site",
		image = "some_image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_filters);
		
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		etSearchFor = (EditText) findViewById(R.id.etSearchFor);
		spImageSize = (Spinner) findViewById(R.id.spImageSize);
		spColors = (Spinner) findViewById(R.id.spColors);
		spImageType = (Spinner)	findViewById(R.id.spImageType);
		
		int [] arrays = {R.array.image_size_array,
			R.array.colors_array,
			R.array.image_type_array};
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		imageSizeAdapter = ArrayAdapter.createFromResource(this, arrays[0], android.R.layout.simple_spinner_item);
		colorsAdapter = ArrayAdapter.createFromResource(this, arrays[1], android.R.layout.simple_spinner_item);
		imageTypeAdapter = ArrayAdapter.createFromResource(this, arrays[2], android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		imageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		colorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		spImageSize.setAdapter(imageSizeAdapter);
		spColors.setAdapter(colorsAdapter);
		spImageType.setAdapter(imageTypeAdapter);
		
	}
	
	public void onButtonSave(View v) {
		Log.d("DEBUG", "imgsize: " + spImageSize.getSelectedItem().toString());
		
		Intent i = new Intent(SetFiltersActivity.this, SearchActivity.class);
		i.putExtra(imageSize, "&imgsz=" + spImageSize.getSelectedItem().toString());
		i.putExtra(color, "&imgcolor=" + spColors.getSelectedItem().toString());
		i.putExtra(imageType, "&imgtype=" + spImageType.getSelectedItem().toString());
		i.putExtra(filterSite, "&as_sitesearch=" + etSiteFilter.getText().toString());
		i.putExtra(image, etSearchFor.getText().toString());
		
		setResult(2, i);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_filters, menu);
		return true;
	}

}
