package com.rockgecko.nshortcutbugs;

import android.content.Intent;

import java.util.Objects;

public class SubActivity1 extends BaseActivity {

    @Override
    protected String getDescription() {
        String msg;
        if(Objects.equals(ACTION_FROM_PREFERENCES, getIntent().getAction())) {
            msg = "Launched from preferences.\n";
            msg += "Android issue 226188: Target package does work as string resource when defined in preferences as @string/app_package";
        }
        else{
            msg = "Launched from shortcut. Action Launcher issue: On pressing back, MainActivity1 should appear, as it is listed in shortcut_1.xml id main1_sub1\n";
            msg += "Android issue 226188: This shortcut does not work on 7.1 emulator, because targetPackage is specified as @string/app_package";
        }
        return msg;
    }
}
