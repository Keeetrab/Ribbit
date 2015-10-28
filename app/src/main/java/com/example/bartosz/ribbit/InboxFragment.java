package com.example.bartosz.ribbit;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bartosz on 22.10.2015.
 */
public class InboxFragment extends ListFragment {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public InboxFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        return rootView;
    }
}
