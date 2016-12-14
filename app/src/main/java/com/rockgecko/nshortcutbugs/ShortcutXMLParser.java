package com.rockgecko.nshortcutbugs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
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
    private static final String TAG = "ShortcutXMLParser";

    

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

    protected ShortcutInfo.Builder parseItem(XmlResourceParser xpp) throws IOException{
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1){
            try {
                return parseItemAPI25(xpp);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        return parseItemBase(xpp);
    }
    protected ShortcutInfo.Builder parseItemBase(XmlResourceParser xpp) {
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
        //Android Issue 229163 nable to use boolean resource for enabled attribute
        String enabledId = xpp.getAttributeValue(NAMESPACE_ANDROID, "enabled");
        if(enabledId!=null){
            boolean enabledBool = resourceIdToBool(enabledId);
            //ShortcutInfo and ShortcutInfo.Builder don't have a method to setEnabled :(
            //ignore.
        }

        return builder;
    }
    protected ShortcutInfo.Builder parseItemAPI25(XmlResourceParser xpp) throws Exception{
        //android.R.attr.shortcutId
        final AttributeSet attrs = Xml.asAttributeSet(xpp);
       Class styleable = Class.forName("com.android.internal.R$styleable");

        styleable.getDeclaredField("ShortcutCategories").get(null);
        String shortcutId =xpp.getAttributeValue(NAMESPACE_ANDROID, "shortcutId");
        ShortcutInfo.Builder builder = new ShortcutInfo.Builder(mContext,shortcutId);


        final TypedArray sa = mContext.getResources().obtainAttributes(attrs,
                (int[])styleable.getDeclaredField("Shortcut").get(null));

            if (sa.getType(styleable.getDeclaredField("Shortcut_shortcutId").getInt(null)) != TypedValue.TYPE_STRING) {
                Log.w(TAG, "android:shortcutId must be string literal. activity=" );
                return null;
            }
            final String id = sa.getNonResourceString(styleable.getDeclaredField("Shortcut_shortcutId").getInt(null));
            final boolean enabled = sa.getBoolean(styleable.getDeclaredField("Shortcut_enabled").getInt(null), true);
            final int iconResId = sa.getResourceId(styleable.getDeclaredField("Shortcut_icon").getInt(null), 0);
            final int titleResId = sa.getResourceId(styleable.getDeclaredField("Shortcut_shortcutShortLabel").getInt(null), 0);
            final int textResId = sa.getResourceId(styleable.getDeclaredField("Shortcut_shortcutLongLabel").getInt(null), 0);
            final int disabledMessageResId = sa.getResourceId(
                    styleable.getDeclaredField("Shortcut_shortcutDisabledMessage").getInt(null), 0);
            if (TextUtils.isEmpty(id)) {
                Log.w(TAG, "android:shortcutId must be provided. activity=" );
                return null;
            }
            if (titleResId == 0) {
                Log.w(TAG, "android:shortcutShortLabel must be provided. activity=" );
                return null;
            }

        builder.setShortLabel(mContext.getString(titleResId));
        if(textResId!=0)builder.setLongLabel(mContext.getString(textResId));
        if(disabledMessageResId!=0)builder.setDisabledMessage(mContext.getString(disabledMessageResId));
        builder.setIcon(Icon.createWithResource(mContext, iconResId));

        sa.recycle();

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
