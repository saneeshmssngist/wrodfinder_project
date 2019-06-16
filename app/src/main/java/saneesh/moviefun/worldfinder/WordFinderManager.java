package saneesh.moviefun.worldfinder;

import java.util.ArrayList;

import saneesh.moviefun.worldfinder.models.WordData;

public class WordFinderManager {
    public static WordFinderManager wordFinderManager;
    public static ArrayList<WordData> bannerwordDataList = new ArrayList<>();
    public static ArrayList<WordData> dialogwordDataList = new ArrayList<>();
    public static ArrayList<WordData> musicwordDataList = new ArrayList<>();

    public static WordFinderManager getInstance() {
        if (wordFinderManager == null)
            wordFinderManager = new WordFinderManager();

        return wordFinderManager;
    }

    public static void insertBannerDatas(ArrayList<WordData> wordData) {
        bannerwordDataList.clear();
        bannerwordDataList.addAll(wordData);
    }

    public static ArrayList<WordData> getBannerDatas() {
        return bannerwordDataList;
    }

    public static void insertDialogDatas(ArrayList<WordData> wordData) {
        dialogwordDataList.clear();
        dialogwordDataList.addAll(wordData);
    }

    public static ArrayList<WordData> getDialogDatas() {
        return dialogwordDataList;
    }

    public static void insertMusicDatas(ArrayList<WordData> wordData) {
        musicwordDataList.clear();
        musicwordDataList.addAll(wordData);
    }

    public static ArrayList<WordData> getMusicDatas() {
        return musicwordDataList;
    }


}
