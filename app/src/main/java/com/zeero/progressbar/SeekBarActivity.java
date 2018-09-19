package com.zeero.progressbar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import java.util.ArrayList;

public class SeekBarActivity extends Activity {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static final Zxcvbn ZXCVBN = new Zxcvbn();

    private float totalSpan = 100;
    private float greenSpan = 32;
    private float transparentSpan = 2;

    private CustomSeekBar seekbar;
    private EditText etPassword;

    private ArrayList<ProgressItem> progressItemList;
    private ProgressItem mProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sb);

        seekbar = ((CustomSeekBar) findViewById(R.id.seekBar));
        etPassword = ((EditText) findViewById(R.id.et_password));

        seekbar.getThumb().mutate().setAlpha(0);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                initDataToSeekbar(R.color.green, Integer.valueOf(etPassword.getText().toString()));
            }
        });
        initDataToSeekbar(R.color.grey, 0);
    }

    private void initDataToSeekbar(int color, int progress) {
            progressItemList = new ArrayList<ProgressItem>();
            for (int i = 0; i < 5; i++) {
                mProgressItem = new ProgressItem();
                mProgressItem.progressItemPercentage = (greenSpan / totalSpan) * 100;
                mProgressItem.color = R.color.grey;
                if (i == 1 || i == 3) {
                    mProgressItem.progressItemPercentage = (transparentSpan / totalSpan) * 100;
                    mProgressItem.color = R.color.transparent;
                } else if (i <= progress) {
                    mProgressItem.color = color;
                }
                progressItemList.add(mProgressItem);
            }
            seekbar.initData(progressItemList);
            seekbar.invalidate();
    }

    private static class MeasureTask extends AsyncTask<String, Void, Void> {

        private final ProgressBar progressBar;

        public MeasureTask(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected Void doInBackground(String... params) {
            final Strength strength = ZXCVBN.measure(params[0]);
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    int score = strength.getScore();
                    progressBar.setProgress(score);
//                    initDataToSeekbar(R.color.green, Integer.valueOf(score));
                }
            });
            return null;
        }
    }
}