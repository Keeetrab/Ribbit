package com.example.bartosz.ribbit.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bartosz.ribbit.UI.FriendsFragment;
import com.example.bartosz.ribbit.UI.InboxFragment;
import com.example.bartosz.ribbit.R;

/**
 * Created by Bartosz on 28.10.2015.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    protected Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a InboxFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.inbox);

            case 1:
                return mContext.getString(R.string.friends);
        }
        return null;
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        ImageView tabImage = (ImageView) tab.findViewById(R.id.icon);
        tabImage.setImageResource(getImageId(position));
        if (position == 0) {
            tab.setSelected(true);
        }
        return tab;
    }

    public int getImageId(int position){
        switch(position) {
            case 0:
                return R.drawable.ic_tab_inbox;
            case 1:
                return R.drawable.ic_tab_friends;
        }
        return 0;
    }
}
