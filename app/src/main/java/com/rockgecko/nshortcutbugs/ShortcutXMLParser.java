package com.rockgecko.nshortcutbugs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Parses shortcut xml files manually, and uses Intent.parseIntent for intents.
 */
@TargetApi(Build.VERSION_CODES.N_MR1)
public class ShortcutXMLParser {
    public static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";

    

    protected Context mContext;
    public ShortcutXMLParser(Context c){
        mContext = c;

    }

    public ArrayList<ShortcutInfo> parseXml(int resourceId) throws IOException, XmlPullParserException {
        ArrayList<ShortcutInfo.Builder> mMenuItems = new ArrayList<ShortcutInfo.Builder>();
        ArrayList<Intent> intentStack = new ArrayList<>();

            XmlResourceParser xpp = mContext.getResources().getXml(resourceId);

            xpp.next();
            int eventType = xpp.getEventType();


            while(eventType != XmlPullParser.END_DOCUMENT){

                if(eventType == XmlPullParser.START_TAG){

                    String elemName = xpp.getName();



                    if(elemName.equals("shortcut")){

                        ShortcutInfo.Builder item = parseItem(xpp);

                        mMenuItems.add(item);
                        intentStack.clear();

                    }
                    else if (elemName.equals("intent")){
                        intentStack.add(parseIntent(xpp));
                    }


                }
                else if (eventType==XmlPullParser.END_TAG){
                    String elemName = xpp.getName();
                    if(elemName.equals("shortcut")){
                        mMenuItems.get(mMenuItems.size()-1).setIntents(intentStack.toArray(new Intent[intentStack.size()]));
                        intentStack.clear();
                    }

                }

                eventType = xpp.next();


            }

            xpp.close();


        ArrayList<ShortcutInfo> results = new ArrayList<>();
        for(ShortcutInfo.Builder item : mMenuItems){
            results.add(item.build());
        }
        return results;

    }

    protected ShortcutInfo.Builder parseItem(XmlResourceParser xpp) {
        //android.R.attr.shortcutId
        String shortcutId =xpp.getAttributeValue(NAMESPACE_ANDROID, "shortcutId");
        ShortcutInfo.Builder builder = new ShortcutInfo.Builder(mContext,shortcutId);
        String textId = xpp.getAttributeValue(NAMESPACE_ANDROID, "shortcutShortLabel");
        String text = resourceIdToString(textId);
        builder.setShortLabel(text);

        textId = xpp.getAttributeValue(NAMESPACE_ANDROID, "shortcutLongLabel");
        text = resourceIdToString(textId);
        builder.setLongLabel(text);

        String iconId = xpp.getAttributeValue(NAMESPACE_ANDROID, "icon");
        int icon;
        if(iconId!=null){
            icon= Integer.parseInt(iconId.replace("@", ""));
        }
        else icon=0;
        builder.setIcon(Icon.createWithResource(mContext, icon));

        //enabled
        String enabledId = xpp.getAttributeValue(NAMESPACE_ANDROID, "enabled");
        if(enabledId!=null){
            boolean enabledBool = resourceIdToBool(enabledId);
            //ShortcutInfo and ShortcutInfo.Builder don't have a method to setEnabled :(
            //ignore.
        }

        return builder;
    }


    protected Intent parseIntent(XmlResourceParser xpp) throws XmlPullParserException, IOException {

            return Intent.parseIntent(mContext.getResources(), xpp, Xml.asAttributeSet(xpp));
    }


    protected String resourceIdToString(String text){

        if(!text.startsWith("@")){
            return text;
        } else {

            String id = text.replace("@", "");

            return mContext.getString(Integer.parseInt(id));

        }

    }

    protected boolean resourceIdToBool(String text){

        if(!text.startsWith("@")){
            return Boolean.parseBoolean(text);
        } else {

            String id = text.replace("@", "");

            return mContext.getResources().getBoolean(Integer.parseInt(id));

        }

    }

}
