package net.xeill.elpuig.thinkitapp.view.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by dam2a on 06/02/18.
 */

public class LocaleManager {

    public static void setLocale(Context c) {
        setNewLocale(c, getLanguage(c));
    }

    public static void setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        updateResources(c, language);
    }

    public static String getLanguage(Context c) {
        SharedPreferences preferences = c.getSharedPreferences("prefs", 0);
        return preferences.getString("language","en");
    }

    private static void persistLanguage(Context c, String language) {
        SharedPreferences preferences = c.getSharedPreferences("prefs", 0);
        preferences.edit().putString("language",language).apply();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }
}