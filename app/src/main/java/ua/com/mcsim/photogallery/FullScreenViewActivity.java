package ua.com.mcsim.photogallery;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ua.com.mcsim.photogallery.utils.Constant;
import ua.com.mcsim.photogallery.utils.MyPageTransformer;
import ua.com.mcsim.photogallery.utils.MyPagerAdpter;
import ua.com.mcsim.photogallery.utils.Utils;

public class FullScreenViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        Utils utils = new Utils(this);
        int position = getIntent().getIntExtra(Constant.POSITION_KEY, 1);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        View imageView = findViewById(R.id.pager);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pager.setTransitionName(getString(R.string.imageview_transition));
        }

        MyPagerAdpter adapter = new MyPagerAdpter(this, utils.getFilePaths(), getScreenSize());
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
        pager.setOffscreenPageLimit(5);
        pager.setPageTransformer(true, new MyPageTransformer());
    }

    private Point getScreenSize() {

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {
            point.x = display.getWidth();
            point.y = display.getHeight();

        }
        return point;


    }


}
