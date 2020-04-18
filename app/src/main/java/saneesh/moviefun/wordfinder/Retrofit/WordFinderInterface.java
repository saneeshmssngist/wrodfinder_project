package saneesh.moviefun.wordfinder.Retrofit;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import saneesh.moviefun.wordfinder.models.ResponseResult;
import saneesh.moviefun.wordfinder.models.WordData;

/**
 * Created by saNeesH on 2018-04-17.
 */

public interface WordFinderInterface {


    //@POST("game_data.txt?alt=media&token=6934f80b-83da-438f-9a22-f356087f1a30")

    @GET
    Call<ResponseResult<ArrayList<WordData>>> getBannerDatas(@Url String url);

    @GET
    Call<ResponseResult<ArrayList<WordData>>> getDialogDatas(@Url String dialogData);

    @GET
    Call<ResponseResult<ArrayList<WordData>>> getMusicDatas(@Url String dialogData);


}
