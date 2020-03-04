package com.foxifinder.infinity.infinitysdkdemo.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foxifinder.infinity.infinitysdkdemo.ExampleDetailActivity;
import com.foxifinder.infinity.infinitysdkdemo.ExampleDetailFragment;
import com.foxifinder.infinity.infinitysdkdemo.ExampleListActivity;
import com.foxifinder.infinity.infinitysdkdemo.R;

/**
 * A fragment representing a single Example detail screen.
 * This fragment is either contained in a {@link ExampleListActivity}
 * in two-pane mode (on tablets) or a {@link ExampleDetailActivity}
 * on handsets.
 */
public class ExampleInitDetailFragment extends ExampleDetailFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.example_detail_init, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.example_text)).setText(mItem.demoText);

        }

        return rootView;
    }
}
