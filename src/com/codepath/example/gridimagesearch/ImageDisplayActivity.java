package com.codepath.example.gridimagesearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageDisplayActivity extends Activity {

	String tempSaveUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);

		ImageResult result = (ImageResult) getIntent().getSerializableExtra(
				"result");
		ImageView ivImage = (ImageView) findViewById(R.id.ivResult);

		String imageUrl = result.getFullUrl();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ivImage
				.getContext()));
		imageLoader.displayImage(imageUrl, ivImage);

		tempSaveUrl = imageUrl;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.action_save_image:
			DownloadManager.Request r = new DownloadManager.Request(
					Uri.parse(tempSaveUrl));
			r.setDestinationInExternalFilesDir(this,
					Environment.DIRECTORY_PICTURES,
					"Image_downloaded_from_google.jpg");
			r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
			dm.enqueue(r);
			Toast.makeText(this, "Save selected", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_email:
			ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
			Bitmap bitmap = ((BitmapDrawable) ivImage.getDrawable())
					.getBitmap();

			Uri bmpUri = null;

			File file = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
					"share_image.jpg");
			try {
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.close();
				bmpUri = Uri.fromFile(file);

				if (bmpUri != null) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
					sendIntent.setType("image/*");
					startActivity(Intent.createChooser(sendIntent,
							"Share Content"));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (file.exists()) {
				file.delete();
			}
			// finish();
			// Toast.makeText(this, "Email selected",
			// Toast.LENGTH_SHORT).show();
			break;
		default:

			break;
		}
		return false;
	}

}
