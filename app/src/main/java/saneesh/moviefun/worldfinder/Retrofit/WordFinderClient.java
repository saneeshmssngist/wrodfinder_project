package saneesh.moviefun.worldfinder.Retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by saNeesH on 2018-04-17.
 */

public class WordFinderClient {

    public static final String MASTEERURL = "http://192.168.43.59/psc_kerala/";
    public static final String MASTEERURL_LIVE = "https://astalavistasan.000webhostapp.com/PSC_KERALA/";
    public static final String FIREBASE_URL = "https://firebasestorage.googleapis.com/v0/b/quizrr-dd785.appspot.com/o/";

    public static Retrofit retrofit = null;

    public static Retrofit getAPiClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(15, TimeUnit.SECONDS);
        httpClient.readTimeout(15, TimeUnit.SECONDS);
        httpClient.writeTimeout(15, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request().newBuilder()
                        .build();

                Response response = chain.proceed(request);
                String rawJson = response.body().string();

                /* Recreating response before returning because response body can be read only once */
                return response.newBuilder()
                        .body(okhttp3.ResponseBody.create(response.body().contentType(), rawJson)).build();

            }
        });
        httpClient.addInterceptor(interceptor);
        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(FIREBASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }
}


