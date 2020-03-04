package com.foxifinder.infinity.infinitysdkdemo;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foxifinder.infinity.infinitysdkdemo.content.ExampleContent;
import com.foxifinder.infinity.sdk.InfinitySDK;

/**
 * A fragment representing a single Example detail screen.
 * This fragment is either contained in a {@link ExampleListActivity}
 * in two-pane mode (on tablets) or a {@link ExampleDetailActivity}
 * on handsets.
 */
public class ExampleDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    protected static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    protected ExampleContent.ExampleItem mItem;

    protected InfinitySDK mSdk;

    protected Handler mHandler;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExampleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler(Looper.getMainLooper());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ExampleContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            mSdk = ((App)getContext().getApplicationContext()).getSdk();

            AppCompatActivity activity = (AppCompatActivity) this.getActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(mItem.content);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = getRootView(inflater, container);

        onInitView(rootView);

        return rootView;
    }

    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.example_detail_init, container, false);
    }

    protected void onInitView(View rootView) {
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.example_text)).setText(mItem.demoText);
        }
    }




}
