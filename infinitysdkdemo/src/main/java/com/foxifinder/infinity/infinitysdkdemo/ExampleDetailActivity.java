package com.foxifinder.infinity.infinitysdkdemo;

import android.content.Intent;
import android.os.Bundle;

import com.foxifinder.infinity.infinitysdkdemo.content.ExampleContent;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

import static com.foxifinder.infinity.infinitysdkdemo.ExampleDetailFragment.ARG_ITEM_ID;

/**
 * An activity representing a single Example detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ExampleListActivity}.
 */
public class ExampleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {

            ExampleContent.ExampleItem mItem = ExampleContent.ITEM_MAP.get(getIntent().getStringExtra(ARG_ITEM_ID));

            Bundle arguments = new Bundle();
            arguments.putString(ARG_ITEM_ID, mItem.id);
            Fragment fragment = null;
            try {
                fragment = mItem.fragmentClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            new ExampleDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.example_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ExampleListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
