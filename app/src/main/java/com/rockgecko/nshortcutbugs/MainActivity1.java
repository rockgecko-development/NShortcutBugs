package com.rockgecko.nshortcutbugs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity1 extends BaseActivity {


    @Override
    protected String getDescription() {
        String msg="";
        if(getIntent().getCategories()!=null && getIntent().getCategories().contains(Intent.CATEGORY_LAUNCHER)){
            msg = "Launched directly\n";
        }
        msg += "Android issue 226188: This shortcut does not work on 7.1 emulator, because targetPackage is specified as @string/app_package";

        return msg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.activity_shortcutmanager_debug:
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1){
                    startActivity(new Intent(this, ShortcutManagerDebugActivity.class));
                }
                else{
                    new AlertDialog.Builder(this).setMessage("ShortcutManager API requires Android 7.1").setPositiveButton("OK",null).show();
                }
                return true;
            case R.id.activity_preferences_debug:
                startActivity(new Intent(this, PreferencesDebugActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
