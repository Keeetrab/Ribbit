package com.example.bartosz.ribbit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bartosz.ribbit.R;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Bartosz on 05.11.2015.
 */
public class RecipientsAdapter extends RecyclerView.Adapter<RecipientsAdapter.RecipientsViewHolder> {

    private List<ParseUser> mUsers;
    private List<ParseUser> mFriends;

    public RecipientsAdapter(List<ParseUser> users, List<ParseUser> friends){
        mUsers = users;
        mFriends = friends;
    }



    @Override
    public RecipientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecipientsViewHolder holder, int position) {
        holder.bindRecipients(null, true);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecipientsViewHolder extends RecyclerView.ViewHolder {

        public TextView mNameLabel;
        public CheckBox mCheckBox;

        public RecipientsViewHolder(View itemView) {
            super(itemView);

            mNameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }

        public void bindRecipients (String userName, boolean checked){
            mNameLabel.setText(userName);
            mCheckBox.setChecked(checked);

        }
    }
}
