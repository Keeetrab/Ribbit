package com.example.bartosz.ribbit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bartosz.ribbit.R;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Bartosz on 05.11.2015.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private static final String TAG = FriendsAdapter.class.getSimpleName();

    private List<ParseUser> mUsers;
    private List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;


    public FriendsAdapter(List<ParseUser> users, List<ParseUser> friends, ParseRelation<ParseUser> friendsRelation, ParseUser currentUser){
        mUsers = users;
        mFriends = friends;
        mFriendsRelation = friendsRelation;
        mCurrentUser = currentUser;
    }

    public void updateFriendsLists(List<ParseUser> users, List<ParseUser> friends, ParseRelation<ParseUser> friendsRelation, ParseUser currentUser){
        mUsers = users;
        mFriends = friends;
        mFriendsRelation = friendsRelation;
        mCurrentUser = currentUser;
        notifyDataSetChanged();
    }


    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate((R.layout.friends_list_item), parent, false);
        FriendsViewHolder viewHolder = new FriendsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        if(mUsers != null) {
            holder.bindFriends(mUsers.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(mUsers != null) {
            return mUsers.size();
        } else {
            return 0;
        }

    }


    public class FriendsViewHolder extends RecyclerView.ViewHolder {

        public TextView mNameLabel;
        public CheckBox mCheckBox;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mNameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }

        public void bindFriends (final ParseUser user){
            mNameLabel.setText(user.getUsername());
            mCheckBox.setChecked(false);

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCheckBox.isChecked()) {
                        mFriendsRelation.add(user);
                    } else {
                        mFriendsRelation.remove(user);
                    }

                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    });

                }
            });

            for (ParseUser friend : mFriends){
                    if(friend.getObjectId().equals(user.getObjectId())){
                        mCheckBox.setChecked(true);
                        break;
                    }
            }
        }
    }

}
