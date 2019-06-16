package saneesh.moviefun.worldfinder.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import saneesh.moviefun.worldfinder.DataManager;
import saneesh.moviefun.worldfinder.NetworkConnection;
import saneesh.moviefun.worldfinder.NetworkStateReceiver;
import saneesh.moviefun.worldfinder.R;
import saneesh.moviefun.worldfinder.Sessions;
import saneesh.moviefun.worldfinder.WordFinderManager;
import saneesh.moviefun.worldfinder.interfaces.RetrofitCallBack;
import saneesh.moviefun.worldfinder.models.WordData;

public class HomeActivity extends BaseActivity implements NetworkStateReceiver.NetworkStateReceiverListener {


    private RelativeLayout layoutBanner, layoutDialog, layoutBGM;
    private TextView txtView, txtViewStatus1, txtViewStatus2, txtViewStatus3;
    private NetworkStateReceiver networkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        setStatusBarGradiant(this);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        layoutBanner = (RelativeLayout) findViewById(R.id.layoutBanner);
        layoutDialog = (RelativeLayout) findViewById(R.id.layoutDialog);
        layoutBGM = (RelativeLayout) findViewById(R.id.layoutBGM);
        txtView = (TextView) findViewById(R.id.txtView);
        txtViewStatus1 = (TextView) findViewById(R.id.txtViewStatus1);
        txtViewStatus2 = (TextView) findViewById(R.id.txtViewStatus2);
        txtViewStatus3 = (TextView) findViewById(R.id.txtViewStatus3);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txtView.setTransitionName("homemovie");
        }
        txtView.setTypeface(Typeface.createFromAsset(getAssets(), "pt_sans.ttf"));

        layoutBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WordFinderManager.getInstance().getBannerDatas().size() != 0) {
                    if (!Sessions.getQuestionNo("banner").contentEquals(
                            String.valueOf(WordFinderManager.getInstance().getBannerDatas().size())))
                        startActivity(new Intent(HomeActivity.this, WordFinderActivity.class
                        ).putExtra("type", "banner"));
                    else
                        Toast.makeText(HomeActivity.this, "Section completed", Toast.LENGTH_SHORT);
                }
            }
        });

        layoutDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (WordFinderManager.getInstance().getDialogDatas().size() != 0) {

                    if (!Sessions.getQuestionNo("dialog").contentEquals(
                            String.valueOf(WordFinderManager.getInstance().getDialogDatas().size())))
                        startActivity(new Intent(HomeActivity.this, WordFinderActivity.class
                        ).putExtra("type", "dialog"));
                    else
                        Toast.makeText(HomeActivity.this, "Section completed", Toast.LENGTH_SHORT);

                }
            }
        });

        layoutBGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WordFinderManager.getInstance().getMusicDatas().size() != 0) {

                    if (!Sessions.getQuestionNo("bgm").contentEquals(
                            String.valueOf(WordFinderManager.getInstance().getMusicDatas().size())))
                        startActivity(new Intent(HomeActivity.this, WordFinderActivity.class
                        ).putExtra("type", "bgm"));
                    else
                        Toast.makeText(HomeActivity.this, "Section completed", Toast.LENGTH_SHORT);
                }
            }
        });


        txtViewStatus1.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        txtViewStatus2.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        txtViewStatus3.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));


    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            networkStateReceiver.removeListener(this);
            this.unregisterReceiver(networkStateReceiver);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllDatas();

        if (Sessions.getQuestionNo("banner").contentEquals("0"))
            txtViewStatus1.setText("Let's Play");
        else
            txtViewStatus1.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("banner"))));

        if (Sessions.getQuestionNo("dialog").contentEquals("0"))
            txtViewStatus2.setText("Let's Play");
        else
            txtViewStatus2.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("dialog"))));

        if (Sessions.getQuestionNo("bgm").contentEquals("0"))
            txtViewStatus3.setText("Let's Play");
        else
            txtViewStatus3.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("bgm"))));

    }

    public void getAllDatas() {

        NetworkConnection networkConnection = new NetworkConnection();
        if (!networkConnection.isConnected(this)) {
            networkConnection.buildDialog(this);
            return;
        }

        if (WordFinderManager.getInstance().getBannerDatas().size() == 0)
            DataManager.getDatamanager().getBannerDatas(new RetrofitCallBack<ArrayList<WordData>>() {
                @Override
                public void Success(ArrayList<WordData> wordData) {

                    WordFinderManager.getInstance().insertBannerDatas(wordData);
                }

                @Override
                public void Failure(String error) {

                }
            });

        if (WordFinderManager.getInstance().getDialogDatas().size() == 0)
            DataManager.getDatamanager().getDialogDatas(new RetrofitCallBack<ArrayList<WordData>>() {
                @Override
                public void Success(ArrayList<WordData> wordData) {

                    WordFinderManager.getInstance().insertDialogDatas(wordData);
                }

                @Override
                public void Failure(String error) {

                }
            });

        if (WordFinderManager.getInstance().getMusicDatas().size() == 0)
            DataManager.getDatamanager().getMusicDatas(new RetrofitCallBack<ArrayList<WordData>>() {
                @Override
                public void Success(ArrayList<WordData> wordData) {

                    WordFinderManager.getInstance().insertMusicDatas(wordData);
                }

                @Override
                public void Failure(String error) {

                }
            });

    }

    @Override
    public void networkAvailable() {

        Log.d("NETWORK", "networkAvailable: ");
        getAllDatas();
    }

    @Override
    public void networkUnavailable() {

        Log.d("NETWORK", "networkUnavailable: ");
    }
}
