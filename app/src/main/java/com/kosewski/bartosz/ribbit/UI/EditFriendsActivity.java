package com.kosewski.bartosz.ribbit.UI;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.kosewski.bartosz.ribbit.Adapters.UserAdapter;
import com.kosewski.bartosz.ribbit.R;
import com.kosewski.bartosz.ribbit.Utilities.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;




public class EditFriendsActivity extends AppCompatActivity {

    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected GridView mGridView;
    private ProgressBar mProgressBar;

    private Animation mJumpFromDown;
    private Animation mJumpToDown;
    private Animation mFadeIn;
    private GridLayoutAnimationController mController;

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Navigation and Toolbar
        setContentView(R.layout.activity_edit_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mGridView = (GridView) findViewById(R.id.friendsGrid);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mGridView.setOnItemClickListener(mOnItemClickListener);

        // Animations
        mJumpFromDown = AnimationUtils.loadAnimation(this, R.anim.jump_from_down);
        mJumpToDown = AnimationUtils.loadAnimation(this, R.anim.jump_to_down);
        mFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        mController = new GridLayoutAnimationController(mFadeIn);

    }


    @Override
    protected void onResume() {
        super.onResume();

        // Populating the GridView with users
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);

                if (e == null) {
                    // Success
                    mUsers = users;

                    if (mGridView.getAdapter() == null) {
                        UserAdapter adapter = new UserAdapter(EditFriendsActivity.this, mUsers);
                        mGridView.setAdapter(adapter);

                        mGridView.setLayoutAnimation(mController);
                        mController.start();
                    } else {
                        ((UserAdapter) mGridView.getAdapter()).refill(mUsers);
                    }
                    addFriendCheckmarks();

                } else {

                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    // list returned - look for a match
                    for (int i = 0; i < mUsers.size(); i++) {
                        ParseUser user = mUsers.get(i);

                        for (ParseUser friend : friends) {
                            if (friend.getObjectId().equals(user.getObjectId())) {
                                mGridView.setItemChecked(i, true);
                            }
                        }
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    protected OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ImageView checkImageView = (ImageView) view.findViewById(R.id.checkImageView);

            if (mGridView.isItemChecked(position)) {
                // add the friend
                mFriendsRelation.add(mUsers.get(position));
                checkImageView.setVisibility(View.VISIBLE);
                checkImageView.startAnimation(mJumpFromDown);

            } else {
                // remove the friend
                mFriendsRelation.remove(mUsers.get(position));
                checkImageView.startAnimation(mJumpToDown);
                checkImageView.setVisibility(View.INVISIBLE);

            }

            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });

        }
    };
}