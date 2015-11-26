package com.example.bartosz.ribbit.UI;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bartosz.ribbit.Adapters.UserAdapter;
import com.example.bartosz.ribbit.Utilities.FileHelper;
import com.example.bartosz.ribbit.Utilities.ParseConstants;
import com.example.bartosz.ribbit.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class RecipientsActivity2 extends AppCompatActivity {

    private static final String TAG = RecipientsActivity2.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected Uri mMediaUri;
    protected String mFileType;
    private ProgressBar mProgressBar;
    private GridView mGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMediaUri = getIntent().getData();
        mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);

        mGridView = (GridView) findViewById(R.id.friendsGrid);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                ParseObject message = createMessage();
                if(message == null){
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity2.this);
                    builder.setMessage(R.string.error_selecting_file)
                            .setTitle(R.string.title_selecting_file_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    send(message);
                }

            }
        });

        fab.hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mGridView.getCheckedItemCount() > 0) {
                    fab.show();
                } else {
                    fab.hide();
                }

                ImageView checkImageView = (ImageView)view.findViewById(R.id.checkImageView);

                if (mGridView.isItemChecked(position)) {
                    // add the friend
                    checkImageView.setVisibility(View.VISIBLE);
                }
                else {
                    // remove the friend
                    checkImageView.setVisibility(View.INVISIBLE);
                }
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        mProgressBar.setVisibility(View.VISIBLE);
        mFriendsRelation.getQuery()
                .addAscendingOrder(ParseConstants.KEY_USERNAME)
                .findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if (e == null) {
                            // Success
                            mFriends = list;
                            if (mGridView.getAdapter() == null) {
                                UserAdapter adapter = new UserAdapter(RecipientsActivity2.this, mFriends);
                                mGridView.setAdapter(adapter);
                            } else {
                                ((UserAdapter) mGridView.getAdapter()).refill(mFriends);
                            }
                        } else {
                            Log.i(TAG, e.getMessage());
                            Toast.makeText(RecipientsActivity2.this, R.string.error_toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void send(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (e == null) {
                    //success
                    Toast.makeText(RecipientsActivity2.this, R.string.success_message, Toast.LENGTH_SHORT).show();
                    sendPushNotification();
                    finish();

                } else {
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity2.this);
                    builder.setMessage(R.string.error_sending_message)
                            .setTitle(R.string.title_selecting_file_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void sendPushNotification() {
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereContainedIn(ParseConstants.KEY_USER_ID, getRecipientsIds());

        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(getString(R.string.push_message, ParseUser.getCurrentUser().getUsername()));
        push.sendInBackground();
    }

    private ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_SENDER_ID, mCurrentUser.getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, mCurrentUser.getUsername());
        message.put(ParseConstants.KEY_RECIPIENTS_IDS, getRecipientsIds());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
        if(fileBytes == null){
            return null;
        } else {
            if(fileBytes.equals(ParseConstants.TYPE_IMAGE)) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }

            String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
            ParseFile file = new ParseFile(fileName, fileBytes);
            message.put(ParseConstants.KEY_FILE, file);

            return message;
        }
    }

    private ArrayList<String> getRecipientsIds() {
        ArrayList<String> recipientsIds = new ArrayList<String>();
        for(int i = 0; i < mGridView.getCount(); i++){
            if(mGridView.isItemChecked(i)){
                recipientsIds.add(mFriends.get(i).getObjectId());
            }
        }

        return recipientsIds;
    }



}
