package com.novel.reader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.novel.reader.api.Setting;

public class SettingActivity extends SherlockFragmentActivity {

    // private SharedPreferences prefs;
    private int                 textSize;
    private int                 textLanguage;       // 0 for 繁體, 1 for 簡體
    private int                 readingDirection;   // 0 for 直向, 1 for 橫向
    private int                 clickToNextPage;    // 0 for yes, 1 for no
    private int                 stopSleeping;       // 0 for yes, 1 for no
    private SeekBar             mSeekBar;
    private RadioGroup          langRadioGroup;
    private RadioGroup          directionRadioGroup;
    private RadioGroup          tapRadioGroup;
    private RadioGroup          stopSleepRadioGroup;
    private TextView            textPreView;

    private AlertDialog.Builder finishDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        textSize = Setting.getSetting(Setting.keyTextSize, SettingActivity.this);
        textLanguage = Setting.getSetting(Setting.keyTextLanguage, SettingActivity.this);
        readingDirection = Setting.getSetting(Setting.keyReadingDirection, SettingActivity.this);
        clickToNextPage = Setting.getSetting(Setting.keyClickToNextPage, SettingActivity.this);
        stopSleeping = Setting.getSetting(Setting.keyStopSleeping, SettingActivity.this);

        setViews();

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(getResources().getString(R.string.title_reading_setting));
        ab.setDisplayHomeAsUpEnabled(true);

    }

    private void setViews() {
        // TODO Auto-generated method stub
        mSeekBar = (SeekBar) findViewById(R.id.seekBar1);
        langRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup_lan);
        directionRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup_reading_direction);
        tapRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup_tap);
        stopSleepRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup_stop_sleep);
        textPreView = (TextView) findViewById(R.id.text_preview);

        textPreView.setTextSize(textSize);
        mSeekBar.setProgress(textSize);

        ((RadioButton) langRadioGroup.getChildAt(textLanguage)).setChecked(true);
        ((RadioButton) directionRadioGroup.getChildAt(readingDirection)).setChecked(true);
        ((RadioButton) tapRadioGroup.getChildAt(clickToNextPage)).setChecked(true);
        ((RadioButton) stopSleepRadioGroup.getChildAt(textLanguage)).setChecked(true);

        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                textPreView.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });

        setFinishDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            // finish();
            finishDialog.show();
            break;
        }
        return true;
    }

    private void saveRadioGroupValue(RadioGroup theRadioGroup, String key) {

        int radioButtonID = theRadioGroup.getCheckedRadioButtonId();
        View radioButton = theRadioGroup.findViewById(radioButtonID);
        int idx = theRadioGroup.indexOfChild(radioButton);
        Setting.saveSetting(key, idx, SettingActivity.this);

    }

    private void setFinishDialog() {
        finishDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.setting_living))
                .setMessage(getResources().getString(R.string.setting_message))
                .setPositiveButton(getResources().getString(R.string.setting_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Setting.saveSetting(Setting.keyTextSize, mSeekBar.getProgress(), SettingActivity.this);
                        saveRadioGroupValue(langRadioGroup, Setting.keyTextLanguage);
                        saveRadioGroupValue(directionRadioGroup, Setting.keyReadingDirection);
                        saveRadioGroupValue(tapRadioGroup, Setting.keyClickToNextPage);
                        saveRadioGroupValue(stopSleepRadioGroup, Setting.keyStopSleeping);
                        finish();

                    }
                }).setNeutralButton(getResources().getString(R.string.setting_neutral), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setNegativeButton(getResources().getString(R.string.setting_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finishDialog.show();
    }

}