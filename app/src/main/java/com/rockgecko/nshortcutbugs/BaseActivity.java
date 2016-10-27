package com.rockgecko.nshortcutbugs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doInfoText();
    }
    protected void doInfoText(){
        TextView textView = (TextView) findViewById(R.id.text1);
        String text = String.format("Activity: %s\n%s\nFlavour: %s\nIntent:\n%s", getClass().getSimpleName(), getPackageName(), BuildConfig.FLAVOR, getIntent());
        textView.setText(text);
    }
}
