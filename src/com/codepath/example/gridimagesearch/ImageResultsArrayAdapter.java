package com.codepath.example.gridimagesearch;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImageResultsArrayAdapter extends ArrayAdapter<ImageResult> {
	
	public ImageResultsArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageResult imageInfo = this.getItem(position);
		ImageView ivImage;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			ivImage = (ImageView) inflator.inflate(R.layout.item_image_result, parent, false);
		} else {
			ivImage = (ImageView) convertView;
			ivImage.setImageResource(android.R.color.transparent);
		}
		//ivImage.setImageUrl(imageInfo.getThumbUrl());
		String imageUrl = imageInfo.getThumbUrl();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ivImage.getContext()));
		imageLoader.displayImage(imageUrl, ivImage);
		//imageLoader.destroy();
		return ivImage;
	}
	
	

}
