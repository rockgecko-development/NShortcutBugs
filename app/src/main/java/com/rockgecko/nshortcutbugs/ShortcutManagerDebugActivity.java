package com.rockgecko.nshortcutbugs;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by bramleyt on 27/10/2016.
 */

@TargetApi(Build.VERSION_CODES.N_MR1)
public class ShortcutManagerDebugActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);

        TextView textView = (TextView) findViewById(R.id.text1);
        ShortcutManager sm = getSystemService(ShortcutManager.class);
        ShortcutInfo shortcut = findManifestShortcut(sm.getManifestShortcuts(), "main1_sub1");

        Intent[] intents = shortcut.getIntents();
        Intent subActivity2Intent =  intents[intents.length-1];
        String msg="System read main1_sub1. Intent:\n"+subActivity2Intent;
        msg+="\n"+assertEq("Target Package", getPackageName(), subActivity2Intent.getComponent()==null?null:subActivity2Intent.getComponent().getPackageName());
        msg+="\n"+assertEq("Target Class", SubActivity1.class.getName(), subActivity2Intent.getComponent()==null?null:subActivity2Intent.getComponent().getClassName());

        try {
            //parse them with Intent.parseIntent
            msg+="\n\nManual parsing main1_sub1 with Intent.parseIntent()\n";
            ArrayList<ShortcutInfo> myShortcutInfos = new ShortcutXMLParser(this).parseXml(R.xml.shortcut_1);
            ShortcutInfo myParsedShortcut = findManifestShortcut(myShortcutInfos, "main1_sub1");

            Intent[] myIntents = myParsedShortcut.getIntents();
            Intent mySubActivity2Intent =  myIntents[myIntents.length-1];
            msg+="MyIntent:\n"+mySubActivity2Intent;
            msg+="\n"+assertEq("Target Package", getPackageName(), mySubActivity2Intent.getComponent()==null?null:mySubActivity2Intent.getComponent().getPackageName());
            msg+="\n"+assertEq("Target Class", SubActivity1.class.getName(), mySubActivity2Intent.getComponent()==null?null:mySubActivity2Intent.getComponent().getClassName());


        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            msg+=e.getMessage();
        }


        ShortcutInfo main2_uri = findManifestShortcut(sm.getManifestShortcuts(), "main2_uri");

        Intent[] main2_uri_intents = main2_uri.getIntents();
        Intent main2_uri_intent =  main2_uri_intents[main2_uri_intents.length-1];
        msg+="\n\nSystem read main2_uri. Intent:\n"+main2_uri_intent;
        msg+="\n"+assertEq("URI", getString(R.string.shortcut_main2_uri_link), main2_uri_intent.getDataString());
        try {
            //parse them with Intent.parseIntent
            msg+="\n\nManual parsing shortcut_2 with Intent.parseIntent\n";
            ArrayList<ShortcutInfo> myShortcutInfos = new ShortcutXMLParser(this).parseXml(R.xml.shortcut_2);
            ShortcutInfo myParsedShortcut = findManifestShortcut(myShortcutInfos, "main2_uri");

            Intent[] myIntents = myParsedShortcut.getIntents();
            Intent mySubActivity2Intent =  myIntents[myIntents.length-1];
            msg+="MyIntent:\n"+mySubActivity2Intent;
            msg+="\n"+assertEq("URI", getString(R.string.shortcut_main2_uri_link), mySubActivity2Intent.getDataString());



        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            msg+=e.getMessage();
        }

        textView.setText(msg);

    }

    private String assertEq(String name, Object expected, Object actual){
        if(Objects.equals(expected, actual)){
            return name+" eq "+actual;
        }
        else{
            return name +" expected:\n"+expected+"\nactual:\n"+actual;
        }
    }

    private ShortcutInfo findManifestShortcut(List<ShortcutInfo> list, String id){
        for(ShortcutInfo s : list){
            if(Objects.equals(s.getId(), id))
                return s;
        }
        return null;
    }
}
