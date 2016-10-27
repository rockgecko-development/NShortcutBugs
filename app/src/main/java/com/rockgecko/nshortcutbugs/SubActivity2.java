package com.rockgecko.nshortcutbugs;

public class SubActivity2 extends BaseActivity {

    @Override
    protected String getDescription() {
        String msg="Launched from shortcut. Action Launcher issue: On pressing back, MainActivity2 should appear, as it is listed in shortcut_2xml id main2_sub2\n";
        msg += "Android issue 226188: This shortcut does work on 7.1 emulator, because targetPackage is specified directly";
        return msg;
    }
}
