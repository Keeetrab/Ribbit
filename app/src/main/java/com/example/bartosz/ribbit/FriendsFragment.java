package com.example.bartosz.ribbit;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bartosz on 22.10.2015.
 */
public class FriendsFragment extends ListFragment {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public FriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        return rootView;
    }
}
