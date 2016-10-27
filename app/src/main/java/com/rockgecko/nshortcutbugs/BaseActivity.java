package com.rockgecko.nshortcutbugs;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
        doInfoText();
    }
    protected void doInfoText(){
        TextView textView = (TextView) findViewById(R.id.text1);
        String text = String.format("Activity: %s\n%s\nFlavour: %s\nIntent:\n%s\n\n%s", getClass().getSimpleName(), getPackageName(), BuildConfig.FLAVOR, getIntent(), getDescription());
        textView.setText(text);
    }
    protected abstract String getDescription();
}
