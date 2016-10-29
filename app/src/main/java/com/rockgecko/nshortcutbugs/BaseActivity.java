package com.rockgecko.nshortcutbugs;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public abstract class BaseActivity extends Activity {
    public static final String ACTION_FROM_PREFERENCES = "com.rockgecko.nshotcutbugs.ACTION_FROM_PREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
        doInfoText();
    }
    protected void doInfoText(){
        TextView textView = (TextView) findViewById(R.id.text1);
        String text = String.format(Locale.getDefault(),"Activity: %s\n%s\nBuild %d Flavour: %s\nIntent:\n%s\n\n%s", getClass().getSimpleName(), getPackageName(), BuildConfig.VERSION_CODE, BuildConfig.FLAVOR, getIntent(), getDescription());
        textView.setText(text);
    }
    protected abstract String getDescription();
}
