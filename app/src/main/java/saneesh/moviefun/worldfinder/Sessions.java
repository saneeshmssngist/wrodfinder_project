package saneesh.moviefun.worldfinder;

import android.content.Context;
import android.content.SharedPreferences;

public class Sessions
{

    private static final String PREFERENCE = "Preference";

    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;

    public static void setSession(Context context)
    {
        sharedPreferences = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void setQuestionNo(String type, String questionNo)
    {
        editor.putString(type,questionNo);
        editor.commit();
    }

    public static String getQuestionNo(String type)
    {
       return sharedPreferences.getString(type,"0");
    }

}
