package ua.com.mcsim.photogallery.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import ua.com.mcsim.photogallery.R;

/**
 * Created by mcsim on 13.11.2017.
 */

public class MyPagerAdpter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private int targetHeight;
    private int targetWidth;
    private File file;
    private String filePath;
    private Point _point;

    public MyPagerAdpter(Activity activity, ArrayList<String> imagePaths, Point point) {
        this._activity = activity;
        this._imagePaths = imagePaths;
        this._point = point;
    }

    @Override
    public int getCount() {
        return _imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        this.filePath = _imagePaths.get(position);
        this.file = new File(filePath);

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        resizeImage();

        Picasso.with(_activity)
                .load(file)
                .resize(targetWidth,targetHeight)
                .noFade()
                .into(imgDisplay);


        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }


    private void resizeImage() {
        float scale;
        int fileWidth, fileHeight;
        int screenWidth = _point.x;
        int screenHeight = _point.y;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        fileWidth = options.outWidth;
        fileHeight = options.outHeight;

        scale = Math.max((float)fileHeight/(float)screenHeight , (float)fileWidth/(float)screenWidth);

        targetWidth = (int)((float)fileWidth/scale);
        targetHeight = (int)((float)fileHeight/scale);
    }
}
