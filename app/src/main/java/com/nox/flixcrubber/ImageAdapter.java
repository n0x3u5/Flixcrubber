package com.nox.flixcrubber;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by james on 27-09-2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mThumbPaths = {
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg",
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg",
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg",
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg",
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg",
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg",
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg",
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg",
            "http://alternativefundingpartners.com/wp-content/uploads/2015/09/placeholder_png.jpg"
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbPaths.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            imageView.setAdjustViewBounds(true);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mThumbPaths[position])
                .into(imageView);

        return imageView;
    }

    public void setmThumbPaths(String[] mThumbPaths) {
        this.mThumbPaths = mThumbPaths;
    }

}
