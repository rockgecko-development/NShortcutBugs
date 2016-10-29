package com.rockgecko.nshortcutbugs;

import java.util.Objects;

public class SubActivity2 extends BaseActivity {

    @Override
    protected String getDescription() {
        String msg;
        if(Objects.equals(ACTION_FROM_PREFERENCES, getIntent().getAction())) {
            msg = "Launched from preferences.\n";
            msg += "Android issue 226188: Target package also works when defined in preferences as com.rockgecko.nshortcutbugs.flavour1";
        }
        else {
            msg = "Launched from shortcut. Action Launcher issue: On pressing back, MainActivity2 should appear, as it is listed in shortcut_2xml id main2_sub2\n";
            msg += "Android issue 226188: This shortcut does work on 7.1 emulator, because targetPackage is specified directly";
        }
        return msg;
    }
}
