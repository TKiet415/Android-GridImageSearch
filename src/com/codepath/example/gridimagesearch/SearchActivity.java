package com.codepath.example.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultsArrayAdapter imageAdapter;
	
	String strImageSize = "",
		strColor = "",
		strImageType = "",
		strSiteFilter = "",
		query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		setupViews();
		
		imageAdapter = new ImageResultsArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
		
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// TODO Auto-generated method stub
				customLoadMoreDataFromApi(page);
			}
		});
	}

	protected void customLoadMoreDataFromApi(final int page) {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		// "https://ajax.googleapis.com/ajax/services/search/images?q=Android&v=1.0"
		
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
				"start=" + ((page == 0) ? 0 : (page * 8 + 1)) + "&v=1.0&q=" + Uri.encode(query)
				+ strColor + strImageSize + strImageType + strSiteFilter,
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject(
							"responseData").getJSONArray("results");
					//if (page == 0)
						//imageResults.clear();
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					Log.d("DEBUG", strColor + strImageSize + strImageType + strSiteFilter + gvResults.getCount());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void setupViews() {
		// TODO Auto-generated method stub
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
	}
	
	public void onImageSearch(View v) {
		query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT).show();
		
		imageResults.clear();
		
		customLoadMoreDataFromApi(0);
		
	}
	
	public void onClearFilters(View v) {
		strImageSize = "";
		strColor = "";
		strImageType = "";
		strSiteFilter = "";
		Toast.makeText(this, "All filters have been cleared", Toast.LENGTH_SHORT).show();
	}
	
	public void onSettingsAction(MenuItem mi) {
		// handle click here
		
		Intent i = new Intent(SearchActivity.this, SetFiltersActivity.class);
		startActivityForResult(i, 2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		// Is the requestCode the same?
		if (requestCode == 2) {
			if (null != data) {
				strImageSize = data.getStringExtra(SetFiltersActivity.imageSize);
				strColor = data.getStringExtra(SetFiltersActivity.color);
				strImageType = data.getStringExtra(SetFiltersActivity.imageType);
				strSiteFilter = data.getStringExtra(SetFiltersActivity.filterSite);
				query = data.getStringExtra(SetFiltersActivity.image);
				Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT).show();
				
				imageResults.clear();
				
				customLoadMoreDataFromApi(0);
				
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
