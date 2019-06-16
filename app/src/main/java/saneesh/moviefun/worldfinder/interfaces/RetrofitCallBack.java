package saneesh.moviefun.worldfinder.interfaces;

/**
 * Created by saNeesH on 2018-05-31.
 */

public interface RetrofitCallBack<T> {

    public void Success(T status);

    public void Failure(String error);


}
