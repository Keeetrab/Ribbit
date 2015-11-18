package com.example.bartosz.ribbit.UI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bartosz.ribbit.Adapters.FriendsAdapter;
import com.example.bartosz.ribbit.Utilities.ParseConstants;
import com.example.bartosz.ribbit.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class EditFriendsActivty extends AppCompatActivity {

    private static final String TAG = EditFriendsActivty.class.getSimpleName();
    private ParseUser mCurrentUser;
    private ParseRelation<ParseUser> mFriendsRelation;
    private List<ParseUser> mFriends;
    private List<ParseUser> mUsers;
    private FriendsAdapter adapter;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends_activty2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new FriendsAdapter(mUsers, mFriends, mFriendsRelation, mCurrentUser);

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditFriendsActivty.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        showFriendsList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFriendsList();
    }

    private void showFriendsList() {
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    // Success
                    mUsers = list;
                    downloadFriends();
                } else {
                    Toast.makeText(EditFriendsActivty.this, R.string.error_toast, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, e.getMessage());
                }
            }
        });
    }

    private void downloadFriends() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (e == null) {
                    mFriends = list;
                    adapter.updateFriendsLists(mUsers, mFriends, mFriendsRelation, mCurrentUser);

                } else {
                    Log.i(TAG, e.getMessage());
                }
            }
        });
    }




}
