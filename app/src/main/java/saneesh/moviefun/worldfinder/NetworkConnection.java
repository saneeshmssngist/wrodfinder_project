package saneesh.moviefun.worldfinder;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import com.irozon.sneaker.Sneaker;


public class NetworkConnection {


    private Button button;
    private Context context;

//To check whether that internet connection is available or not.

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    public void buildDialog(Activity context) {

        Sneaker.
                with(context)
                .setTitle("Please check your network !!")
                .setDuration(2000)
                .autoHide(true)
                .sneak(R.color.red);

    }


}