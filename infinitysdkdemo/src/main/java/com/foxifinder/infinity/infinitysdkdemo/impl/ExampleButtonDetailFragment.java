package com.foxifinder.infinity.infinitysdkdemo.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.foxifinder.infinity.infinitysdkdemo.ExampleDetailActivity;
import com.foxifinder.infinity.infinitysdkdemo.ExampleDetailFragment;
import com.foxifinder.infinity.infinitysdkdemo.ExampleListActivity;
import com.foxifinder.infinity.infinitysdkdemo.R;
import com.foxifinder.infinity.infinitysdkdemo.content.ExampleContent;
import com.foxifinder.infinity.sdk.InfinitySDK;

/**
 * A fragment representing a single Example detail screen.
 * This fragment is either contained in a {@link ExampleListActivity}
 * in two-pane mode (on tablets) or a {@link ExampleDetailActivity}
 * on handsets.
 */
public class ExampleButtonDetailFragment extends ExampleDetailFragment {

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.example_detail_button, container, false);
    }

    @Override
    protected void onInitView(View rootView) {
        super.onInitView(rootView);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.example_text)).setText(mItem.demoText);
            ViewGroup exampleControl  = rootView.findViewById((R.id.example_controlpanel));
            final TextView exampleResultText = rootView.findViewById(R.id.example_result);

            initButtons(exampleControl, exampleResultText);
        }
    }


    protected void initButtons(final ViewGroup exampleControl, final TextView exampleResultText) {

            if (mItem.buttons != null) {
            int btnId = 1000;
            for (final ExampleContent.ExampleButtonItem btnItem : mItem.buttons) {

                final Button button = new Button(getContext());
                button.setText(btnItem.title);
                button.setId(++btnId);
                button.setEnabled(mSdk != null && mSdk.isConnected());

                    button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        button.setEnabled(false);
                        exampleResultText.setText("");
                        btnItem.testCode.testCode(mSdk, new InfinitySDK.CommandCallback() {
                            @Override
                            public void onSuccess(final String descrtiption, Bundle params) {
                                showReply(false, descrtiption, exampleResultText, button);
                            }

                            @Override
                            public void onError(String error, Bundle params) {
                                showReply(true, error, exampleResultText, button);
                            }
                        }, null);
                    }
                });

                exampleControl.addView(button);
            }
        }

    }

    private void showReply(final boolean isError, final String text, final TextView exampleResultText, final Button button) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                exampleResultText.setText(String.format("%s", text));
                exampleResultText.setTextColor(
                        ContextCompat.getColor(getContext(),
                                (isError ? R.color.colorFailText : R.color.colorOkText))
                );
                button.setEnabled(true);
            }
        });
    }

}
