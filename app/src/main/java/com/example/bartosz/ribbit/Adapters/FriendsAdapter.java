package com.example.bartosz.ribbit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bartosz.ribbit.R;
import com.parse.ParseUser;

import java.util.List;
import java.util.Map;

/**
 * Created by Bartosz on 05.11.2015.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private static final String TAG = FriendsAdapter.class.getSimpleName();

    private List<ParseUser> mUsers;
    private List<ParseUser> mFriends;


    public FriendsAdapter(List<ParseUser> users, List<ParseUser> friends){
        mUsers = users;
        mFriends = friends;
    }

    public void updateFriendsLists(List<ParseUser> users, List<ParseUser> friends){
        mUsers = users;
        mFriends = friends;
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

        public void bindFriends (ParseUser user){
            mNameLabel.setText(user.getUsername());
            mCheckBox.setChecked(false);

            for (ParseUser friend : mFriends){
                    if(friend.getObjectId().equals(user.getObjectId())){
                        mCheckBox.setChecked(true);
                        break;
                    }
            }
            Log.i(TAG, "Name: " + user.getUsername() + " Checked: " + mCheckBox.isChecked());
        }
    }

}
