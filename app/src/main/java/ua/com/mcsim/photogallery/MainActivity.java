package ua.com.mcsim.photogallery;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
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

        TextView tvEmpty = (TextView) findViewById(R.id.tv_empty);
        tvEmpty.setVisibility(View.GONE);

        gridView = (GridView) findViewById(R.id.grid_view);
        utils = new Utils(this);
        initializeGridLayout();

        if (utils.isNoImages) tvEmpty.setVisibility(View.VISIBLE);
        gridViewAdapter = new MyGridViewAdapter(MainActivity.this,utils.getFilePaths(), columnWidth);
        gridView.setAdapter(gridViewAdapter);

        cameraUtils = new CameraUtils(this);
    }

    private void initializeGridLayout() {
        Resources resources = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constant.GRID_PADDING, resources.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((Constant.NUM_OF_COLUMNS +1) * padding))/ Constant.NUM_OF_COLUMNS);

        gridView.setNumColumns(Constant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding,(int) padding,(int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            cameraUtils.galleryAddPic();
            utils = new Utils(this);
            gridViewAdapter = new MyGridViewAdapter(MainActivity.this,utils.getFilePaths(), columnWidth);
            gridView.setAdapter(gridViewAdapter);
        }
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
