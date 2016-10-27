package com.rockgecko.nshortcutbugs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends BaseActivity {

    @Override
    protected String getDescription() {
        String msg="";
        if(getIntent().getCategories().contains(Intent.CATEGORY_LAUNCHER)){
            msg = "Launched directly\n";
        }

            msg += "Android issue 226188: This shortcut does work on 7.1 emulator, because targetPackage is specified directly";


        return msg;
    }
}
