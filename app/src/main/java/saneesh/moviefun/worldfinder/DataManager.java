package saneesh.moviefun.worldfinder;

import android.content.Context;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneesh.moviefun.worldfinder.Retrofit.WordFinderClient;
import saneesh.moviefun.worldfinder.Retrofit.WordFinderInterface;
import saneesh.moviefun.worldfinder.interfaces.RetrofitCallBack;
import saneesh.moviefun.worldfinder.models.ResponseResult;
import saneesh.moviefun.worldfinder.models.WordData;

import static saneesh.moviefun.worldfinder.AppConstants.BANNER_DATA;
import static saneesh.moviefun.worldfinder.AppConstants.DIALOG_DATA;
import static saneesh.moviefun.worldfinder.AppConstants.MUSIC_DATA;


/**
 * Created by saNeesH on 2018-05-31.
 */

public class DataManager {


    private static DataManager dataManger = null;
    private WordFinderInterface cabApiInterface;
    private Context mContext;


    public static DataManager getDatamanager() {
        if (dataManger == null)
            dataManger = new DataManager();

        return dataManger;
    }

    public void init(Context applicationContext) {
        cabApiInterface = WordFinderClient.getAPiClient().create(WordFinderInterface.class);
        mContext = applicationContext;

    }

    public void getBannerDatas(final RetrofitCallBack<ArrayList<WordData>> retrofitCallBack) {

        Call<ResponseResult<ArrayList<WordData>>> resultCall = cabApiInterface.getBannerDatas(BANNER_DATA);

        resultCall.enqueue(new Callback<ResponseResult<ArrayList<WordData>>>() {
            @Override
            public void onResponse(Call<ResponseResult<ArrayList<WordData>>> call, Response<ResponseResult<ArrayList<WordData>>> response) {

                if (response.isSuccessful()) {
                    if (response.body().getCode().equals("100")) {
                        retrofitCallBack.Success(response.body().getData());
                    } else {
                        retrofitCallBack.Failure(response.body().getStatus());
                    }
                } else {
                    retrofitCallBack.Failure("Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult<ArrayList<WordData>>> call, Throwable t) {
                retrofitCallBack.Failure("Error");
            }
        });

    }

    public void getDialogDatas(final RetrofitCallBack<ArrayList<WordData>> retrofitCallBack) {

        Call<ResponseResult<ArrayList<WordData>>> resultCall = cabApiInterface.getDialogDatas(DIALOG_DATA);

        resultCall.enqueue(new Callback<ResponseResult<ArrayList<WordData>>>() {
            @Override
            public void onResponse(Call<ResponseResult<ArrayList<WordData>>> call, Response<ResponseResult<ArrayList<WordData>>> response) {

                if (response.isSuccessful()) {
                    if (response.body().getCode().equals("100")) {
                        retrofitCallBack.Success(response.body().getData());
                    } else {
                        retrofitCallBack.Failure(response.body().getStatus());
                    }
                } else {
                    retrofitCallBack.Failure("Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult<ArrayList<WordData>>> call, Throwable t) {
                retrofitCallBack.Failure("Error");
            }
        });

    }

    public void getMusicDatas(final RetrofitCallBack<ArrayList<WordData>> retrofitCallBack) {

        Call<ResponseResult<ArrayList<WordData>>> resultCall = cabApiInterface.getMusicDatas(MUSIC_DATA);

        resultCall.enqueue(new Callback<ResponseResult<ArrayList<WordData>>>() {
            @Override
            public void onResponse(Call<ResponseResult<ArrayList<WordData>>> call, Response<ResponseResult<ArrayList<WordData>>> response) {

                if (response.isSuccessful()) {
                    if (response.body().getCode().equals("100")) {
                        retrofitCallBack.Success(response.body().getData());
                    } else {
                        retrofitCallBack.Failure(response.body().getStatus());
                    }
                } else {
                    retrofitCallBack.Failure("Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult<ArrayList<WordData>>> call, Throwable t) {
                retrofitCallBack.Failure("Error");
            }
        });

    }


}
