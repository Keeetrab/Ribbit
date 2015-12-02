package com.kosewski.bartosz.ribbit.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kosewski.bartosz.ribbit.Adapters.UserAdapter;
import com.kosewski.bartosz.ribbit.R;
import com.kosewski.bartosz.ribbit.Utilities.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.friendsGrid) GridView mGridView;
    @Bind(android.R.id.empty) TextView mEmptyTextView;

    public FriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, rootView);
        mProgressBar.setVisibility(View.INVISIBLE);
        mGridView.setEmptyView(mEmptyTextView);

        return rootView;
    }

    @Override
    public void onResume() {
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
                            mFriends = list;

                            if (mGridView.getAdapter() == null) {
                                UserAdapter adapter = new UserAdapter(getActivity(), mFriends);
                                mGridView.setAdapter(adapter);
                            } else {
                                ((UserAdapter) mGridView.getAdapter()).refill(mFriends);
                            }
                        } else {
                            Log.i(TAG, e.getMessage());
                            Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
