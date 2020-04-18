package saneesh.moviefun.wordfinder;

import android.content.Context;
import android.content.SharedPreferences;

public class Sessions {

    private static final String PREFERENCE = "Preference";

    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;

    public static void setSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void setQuestionNo(String type, String questionNo) {
        editor.putString(type, questionNo);
        editor.commit();
    }

    public static String getQuestionNo(String type) {
        return sharedPreferences.getString(type, "1");
    }

//    public static void setBannerLevel(String no) {
//        editor.putString("banner", no).commit();
//    }
//    public static int getBannerLevel() {
//        return sharedPreferences.getInt("banner", 1);
//    }
//
//    public static void setDialogLevel(int no) {
//        editor.putInt("dialog", no).commit();
//    }
//    public static int getDialogLevel() {
//        return sharedPreferences.getInt("dialog", 1);
//    }
//
//    public static void setMusicLevel(int no) {
//        editor.putInt("music", no).commit();
//    }
//    public static int getMusicLevel() {
//        return sharedPreferences.getInt("music", 1);
//    }


}
