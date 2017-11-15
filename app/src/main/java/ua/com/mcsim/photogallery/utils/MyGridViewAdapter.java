package ua.com.mcsim.photogallery.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;

import ua.com.mcsim.photogallery.FullScreenViewActivity;


/**
 * Created by mcsim on 13.11.2017.
 */

public class MyGridViewAdapter extends BaseAdapter {

    private Activity _activity;
    private ArrayList<String> _filePaths = new ArrayList<String>();
    private int imageWidth;

    public MyGridViewAdapter (Activity activity, ArrayList<String> filePaths,int imageWidth) {
        this._activity = activity;
        this._filePaths = filePaths;
        this.imageWidth = imageWidth;
    }


    @Override
    public int getCount() {
        return _filePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return this._filePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("mLog", "getView method..");
        ImageView imageView;
        File file;
        if (convertView == null) {
            imageView = new ImageView(_activity);
        } else {
            imageView = (ImageView) convertView;
        }

        file = new File(_filePaths.get(position));
        if (file.length()>0) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
            Picasso.with(_activity.getApplicationContext())
                    .load(file)
                    .resize(imageWidth,imageWidth)
                    .centerCrop()
                    .into(imageView);
            Log.d("mLog", _filePaths.get(position) + " pos#" + position + " grid. Filesize " + file.length());

            imageView.setOnClickListener(new OnImageClickListener(position));
        } else {
            Log.d("mLog", "EMPTY FILE" +_filePaths.get(position) + " grid. Filesize " + file.length());
        }

        return imageView;
    }

    class OnImageClickListener implements View.OnClickListener {

        int pos;

        public OnImageClickListener(int position) {
            this.pos = position;
            Log.d("mLog", "listener created with pos: " + position);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(_activity, FullScreenViewActivity.class);
            intent.putExtra(Constant.POSITION_KEY, pos);
            _activity.startActivity(intent);
            Log.d("mLog", "onClick pos:" + pos);
        }
    }
}
