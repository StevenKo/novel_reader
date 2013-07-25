package com.novel.reader;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.novel.db.SQLiteNovel;
import com.novel.reader.api.Setting;

public class SettingActivity extends SherlockFragmentActivity {

    // private SharedPreferences prefs;
    private int                 textSize;
    private int                 textLanguage;           // 0 for 繁體, 1 for 簡體
    private int                 readingDirection;       // 0 for 直向, 1 for 橫向
    private int                 clickToNextPage;        // 0 for yes, 1 for no
    private int                 stopSleeping;           // 0 for yes, 1 for no
    private SeekBar             mSeekBar;
    private RadioGroup          langRadioGroup;
    private RadioGroup          directionRadioGroup;
    private RadioGroup          tapRadioGroup;
    private RadioGroup          stopSleepRadioGroup;
    private RadioGroup          themeRadioGroup;
    private TextView            textPreView;
    private ImageView           imageviewTextColor;
    private ImageView           imageviewTextBackground;
    private int                 textColor;
    private int                 textBackground;
    private int                 appTheme;

    private AlertDialog.Builder finishDialog;
    private Button              dbResetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Setting.setApplicationActionBarTheme(this);
        setContentView(R.layout.layout_setting);

        textSize = Setting.getSetting(Setting.keyTextSize, SettingActivity.this);
        textLanguage = Setting.getSetting(Setting.keyTextLanguage, SettingActivity.this);
        readingDirection = Setting.getSetting(Setting.keyReadingDirection, SettingActivity.this);
        clickToNextPage = Setting.getSetting(Setting.keyClickToNextPage, SettingActivity.this);
        stopSleeping = Setting.getSetting(Setting.keyStopSleeping, SettingActivity.this);
        textColor = Setting.getSetting(Setting.keyTextColor, SettingActivity.this);
        textBackground = Setting.getSetting(Setting.keyTextBackground, SettingActivity.this);
        appTheme = Setting.getSetting(Setting.keyAppTheme, SettingActivity.this);

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
        themeRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup_theme);
        textPreView = (TextView) findViewById(R.id.text_preview);
        imageviewTextColor = (ImageView) findViewById(R.id.imageview_textcolor);
        imageviewTextBackground = (ImageView) findViewById(R.id.imageview_textbackground);
        dbResetButton = (Button) findViewById(R.id.dbResetButton);

        textPreView.setTextSize(textSize);
        textPreView.setTextColor(textColor);
        textPreView.setBackgroundColor(textBackground);
        mSeekBar.setProgress(textSize);
        imageviewTextColor.setBackgroundColor(textColor);
        imageviewTextBackground.setBackgroundColor(textBackground);

        ((RadioButton) langRadioGroup.getChildAt(textLanguage)).setChecked(true);
        ((RadioButton) directionRadioGroup.getChildAt(readingDirection)).setChecked(true);
        ((RadioButton) tapRadioGroup.getChildAt(clickToNextPage)).setChecked(true);
        ((RadioButton) stopSleepRadioGroup.getChildAt(textLanguage)).setChecked(true);
        ((RadioButton) themeRadioGroup.getChildAt(appTheme)).setChecked(true);

        dbResetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDbResetDialog();
            }
        });

        imageviewTextColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showTextColorPicker();
            }
        });

        imageviewTextBackground.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showTextBackgroundPicker();
            }
        });

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

    private void showTextColorPicker() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, textColor, new OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // color is the color selected by the user.
                textColor = color;
                imageviewTextColor.setBackgroundColor(textColor);
                textPreView.setTextColor(textColor);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });
        dialog.show();
    }

    private void showTextBackgroundPicker() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, textBackground, new OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // color is the color selected by the user.
                textBackground = color;
                imageviewTextBackground.setBackgroundColor(textBackground);
                textPreView.setBackgroundColor(textBackground);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

    private void showDbResetDialog() {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.reset_db_hint))
                .setMessage(getResources().getString(R.string.reset_db_message))
                .setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteNovel db = new SQLiteNovel(SettingActivity.this);
                        boolean reset = db.resetDB();
                        if (reset) {
                            Toast.makeText(SettingActivity.this, getResources().getString(R.string.reset_db_success), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SettingActivity.this, getResources().getString(R.string.reset_db_fail), Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton(getResources().getString(R.string.report_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

    }

    private void setFinishDialog() {
        finishDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.setting_living))
                .setMessage(getResources().getString(R.string.setting_message))
                .setPositiveButton(getResources().getString(R.string.setting_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Setting.saveSetting(Setting.keyTextSize, mSeekBar.getProgress(), SettingActivity.this);
                        Setting.saveSetting(Setting.keyTextColor, textColor, SettingActivity.this);
                        Setting.saveSetting(Setting.keyTextBackground, textBackground, SettingActivity.this);
                        saveRadioGroupValue(langRadioGroup, Setting.keyTextLanguage);
                        saveRadioGroupValue(directionRadioGroup, Setting.keyReadingDirection);
                        saveRadioGroupValue(tapRadioGroup, Setting.keyClickToNextPage);
                        saveRadioGroupValue(stopSleepRadioGroup, Setting.keyStopSleeping);
                        saveRadioGroupValue(themeRadioGroup, Setting.keyAppTheme);
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
    
    @Override
    public void onStart() {
      super.onStart();
      EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
      super.onStop();
      EasyTracker.getInstance().activityStop(this);
    }

}