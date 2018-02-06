package net.xeill.elpuig.thinkitapp.view.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by dam2a on 06/02/18.
 */

public class LocaleManager {
    public static void setLocale(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        SharedPreferences preferences = context.getSharedPreferences("prefs", 0);
        preferences.edit().putString("language",lang).apply();
    }
}