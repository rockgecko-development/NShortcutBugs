package com.rockgecko.nshortcutbugs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity2 extends BaseActivity {

    @Override
    protected String getDescription() {
        String msg="";
        if(getIntent().getCategories()!=null && getIntent().getCategories().contains(Intent.CATEGORY_LAUNCHER)){
            msg = "Launched directly\n";
        }

            msg += "Android issue 226188: This shortcut does work on 7.1 emulator, because targetPackage is specified directly\n";
            msg += "Android issue 229162: Unable to disable pinned shortcut (see overflow menu)";


        return msg;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.disable_shortcut_main2:
                String msg;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1){
                    //issue 229162
                    ShortcutManager sm = getSystemService(ShortcutManager.class);
                    ShortcutInfo shortcut = ShortcutManagerDebugActivity.findManifestShortcut(sm.getManifestShortcuts(), "main2");
                    if(shortcut.isPinned()){
                        try{
                            List<String> list = new ArrayList<>();
                            list.add(shortcut.getId());
                            sm.disableShortcuts(list);
                            msg="OK";
                        }catch (Exception e){
                            msg="Exception: "+e.getMessage();
                        }
                    }
                    else{
                        msg="Please pin "+shortcut.getShortLabel()+" to homescreen first";
                    }
                }
                else{
                    msg="ShortcutManager API requires Android 7.1";
                }
                new AlertDialog.Builder(this).setMessage(msg).setPositiveButton("OK",null).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
