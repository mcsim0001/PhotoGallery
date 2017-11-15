package ua.com.mcsim.photogallery;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.com.mcsim.photogallery.utils.CameraUtils;
import ua.com.mcsim.photogallery.utils.Constant;
import ua.com.mcsim.photogallery.utils.MyGridViewAdapter;
import ua.com.mcsim.photogallery.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private Utils utils;
    private MyGridViewAdapter gridViewAdapter;
    private GridView gridView;
    private int columnWidth;
    static final int REQUEST_TAKE_PHOTO = 1;
    private CameraUtils cameraUtils;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraUtils.makePhoto();
            }
        });

        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        tvEmpty.setVisibility(View.GONE);

        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FullScreenViewActivity.class);
                intent.putExtra(Constant.POSITION_KEY, position);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    view.setTransitionName(getString(R.string.imageview_transition));

                    Pair<View, String> pair1 = Pair.create(view, view.getTransitionName());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                    Log.d("mLog", "onClick pos:" + position);
                }
            }
        });

        initializeGridLayout();
        refreshGridLayout();

        cameraUtils = new CameraUtils(this);
    }

    private void initializeGridLayout() {
        Resources resources = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constant.GRID_PADDING, resources.getDisplayMetrics());
        utils = new Utils(this);
        columnWidth = (int) ((utils.getScreenWidth() - ((Constant.NUM_OF_COLUMNS + 1) * padding)) / Constant.NUM_OF_COLUMNS);

        gridView.setNumColumns(Constant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);

    }

    private void refreshGridLayout() {
        utils = new Utils(this);
        gridViewAdapter = new MyGridViewAdapter(MainActivity.this, utils.getFilePaths(), columnWidth);
        gridView.setAdapter(gridViewAdapter);
        if (utils.isNoImages) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
        } else cameraUtils.deleteImage();
        refreshGridLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
