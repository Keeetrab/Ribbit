package com.example.bartosz.ribbit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    protected Uri mMediaUri;


    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which){
                case 0: //Take Photo
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if (mMediaUri == null){
                        Toast.makeText(MainActivity.this, R.string.external_storage_access_error, Toast.LENGTH_SHORT).show();
                    } else {
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    }
                    break;
                case 1: //Take Video
                    Intent takeVideoIntent = new Intent (MediaStore.ACTION_VIDEO_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    if (mMediaUri == null){
                        Toast.makeText(MainActivity.this, R.string.external_storage_access_error, Toast.LENGTH_SHORT).show();
                    } else {
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                    }
                    break;
                case 2: //Choose Picture

                    break;
                case 3: //Choose Video
                    break;
            }
        }

        private Uri getOutputMediaFileUri(int mediaType) {
            // Check if external storage is mounted

            if(isExternalStorageAvailable()){
                // get the uri

                // 1. Get the external storage directory
                String appName = MainActivity.this.getString(R.string.app_name);
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);

                // 2. Create our subdirectory
                if (! mediaStorageDir.exists()){
                    if (! mediaStorageDir.mkdirs()){
                        Log.d(TAG, "Failed to create directory");
                        return null;
                    }
                }

                // 3. Create a file name
                // 4. Create the file
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File mediaFile;

                if (mediaType == MEDIA_TYPE_IMAGE){
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "IMG_"+ timeStamp + ".jpg");
                } else if(mediaType == MEDIA_TYPE_VIDEO) {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "VID_"+ timeStamp + ".mp4");
                } else {
                    return null;
                }

                // 5. Return the file's Uri
                Log.i(TAG, "File: " + Uri.fromFile(mediaFile));

                return Uri.fromFile(mediaFile);
            } else {
                return null;
            }
        }

        private boolean isExternalStorageAvailable(){
            String state = Environment.getExternalStorageState();

            if(state.equals(Environment.MEDIA_MOUNTED)){
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            navigateToLogin();
        } else {
            Toast.makeText(MainActivity.this, "Hello " + currentUser.getUsername() + "!", Toast.LENGTH_SHORT).show();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Floating Action Buttons

        final FloatingActionButton fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        final FloatingActionButton fabFriends = (FloatingActionButton) findViewById(R.id.fabFriends);
        fabFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditFriendsActivity.class);
                startActivity(intent);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        fabFriends.hide();

                        fabCamera.show();
                        break;
                    case 1:
                        fabCamera.hide();

                        fabFriends.show();
                        break;

                    default:
                        fabCamera.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            //add to gallery
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(mMediaUri);
            sendBroadcast(mediaScanIntent);

        } else if (resultCode != RESULT_CANCELED){
            Toast.makeText(MainActivity.this, R.string.general_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logout_activity) {
            ParseUser.logOut();
            navigateToLogin();
        }
        return super.onOptionsItemSelected(item);
    }
}
