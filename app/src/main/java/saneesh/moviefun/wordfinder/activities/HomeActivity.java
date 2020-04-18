package saneesh.moviefun.wordfinder.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import saneesh.moviefun.wordfinder.DataManager;
import saneesh.moviefun.wordfinder.NetworkConnection;
import saneesh.moviefun.wordfinder.NetworkStateReceiver;
import saneesh.moviefun.wordfinder.R;
import saneesh.moviefun.wordfinder.Sessions;
import saneesh.moviefun.wordfinder.WordFinderManager;
import saneesh.moviefun.wordfinder.interfaces.RetrofitCallBack;
import saneesh.moviefun.wordfinder.models.WordData;

public class HomeActivity extends BaseActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private RelativeLayout layoutBanner, layoutDialog, layoutBGM;
    private TextView txtView, txtViewStatus1, txtViewStatus2, txtViewStatus3;
    private NetworkStateReceiver networkStateReceiver;
    private LottieAnimationView lottieRefresh1, lottieRefresh2, lottieRefresh3;
    private LinearLayout lottieShare;

    private LinearLayout layoutProgress;
    private AVLoadingIndicatorView avilayoutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        setStatusBarGradiant(this,getResources().getDrawable(R.drawable.home_bg_status));

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

        lottieRefresh1 = (LottieAnimationView) findViewById(R.id.lottieRefresh1);
        lottieRefresh2 = (LottieAnimationView) findViewById(R.id.lottieRefresh2);
        lottieRefresh3 = (LottieAnimationView) findViewById(R.id.lottieRefresh3);
        lottieShare = (LinearLayout) findViewById(R.id.layoutShare);

        layoutProgress = findViewById(R.id.layoutProgress);
        avilayoutProgress = findViewById(R.id.avilayoutProgress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txtView.setTransitionName("homemovie");
        }
        txtView.setTypeface(Typeface.createFromAsset(getAssets(), "pt_sans.ttf"));

        lottieShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareApplication();
            }
        });

        layoutBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, LevelSelectionActivity.class)
                        .putExtra("type", "dialog"));

//                if (WordFinderManager.getInstance().getBannerDatas().size() != 0) {
//                    if (!Sessions.getQuestionNo("banner").contentEquals(
//                            String.valueOf(WordFinderManager.getInstance().getBannerDatas().size())))
//                        startActivity(new Intent(HomeActivity.this, WordFinderActivity.class
//                        ).putExtra("type", "banner"));
//                    else
//                        Toast.makeText(HomeActivity.this, "Section completed", Toast.LENGTH_SHORT);
//                }
            }
        });

        layoutDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(HomeActivity.this, LevelSelectionActivity.class)
                        .putExtra("type", "bgm"));

//                if (WordFinderManager.getInstance().getDialogDatas().size() != 0) {
//
//                    if (!Sessions.getQuestionNo("dialog").contentEquals(
//                            String.valueOf(WordFinderManager.getInstance().getDialogDatas().size())))
//                        startActivity(new Intent(HomeActivity.this, WordFinderActivity.class
//                        ).putExtra("type", "dialog"));
//                    else
//                        Toast.makeText(HomeActivity.this, "Section completed", Toast.LENGTH_SHORT);
//
//                }
            }
        });

        layoutBGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, LevelSelectionActivity.class)
                        .putExtra("type", "banner"));

//                if (WordFinderManager.getInstance().getMusicDatas().size() != 0) {
//
//                    if (!Sessions.getQuestionNo("bgm").contentEquals(
//                            String.valueOf(WordFinderManager.getInstance().getMusicDatas().size())))
//                        startActivity(new Intent(HomeActivity.this, WordFinderActivity.class
//                        ).putExtra("type", "bgm"));
//                    else
//                        Toast.makeText(HomeActivity.this, "Section completed", Toast.LENGTH_SHORT);
//                }

            }
        });


        txtViewStatus1.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        txtViewStatus2.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        txtViewStatus3.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));


        lottieRefresh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lottieRefresh1.playAnimation();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottieRefresh1.setVisibility(View.GONE);
                        Sessions.setQuestionNo("banner", "1");
                        txtViewStatus1.setText("Let's Play");
                    }
                }, 1000);


            }
        });

        lottieRefresh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lottieRefresh2.playAnimation();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottieRefresh2.setVisibility(View.GONE);
                        Sessions.setQuestionNo("dialog", "1");
                        txtViewStatus2.setText("Let's Play");
                    }
                }, 1000);


            }
        });

        lottieRefresh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lottieRefresh3.playAnimation();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottieRefresh3.setVisibility(View.GONE);
                        Sessions.setQuestionNo("bgm", "1");
                        txtViewStatus3.setText("Let's Play");
                    }
                }, 1000);


            }
        });

        lottieRefresh1.playAnimation();
        lottieRefresh2.playAnimation();
        lottieRefresh3.playAnimation();

    }

    private void shareApplication() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "movie fun");

        String message = "Hey !!  I just reached *Level " + Sessions.getQuestionNo("banner") + "* on *movie fun* " + getEmoji(0x1F60D) + getEmoji(0x1F60D) + ". " +
                "Its very entertaining movie game Application" + getEmoji(0x1F44C) + ". Try beat my record at " + getEmoji(0x1F449) + "\n" +
                "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName();

        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Choose one"));

    }

    public String getEmoji(int unicode) {
        return new String(Character.toChars(unicode));
    }


    @Override
    protected void onStop() {
        super.onStop();

        try {
            networkStateReceiver.removeListener(this);
            this.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        getAllDatas();
        setHomeStatus();

    }

    public void setHomeStatus() {
        if (Sessions.getQuestionNo("dialog").contentEquals("1")) {
            txtViewStatus1.setText("Let's Play");
        } else if (WordFinderManager.getInstance().getDialogDatas().size() != 0 &&
                WordFinderManager.getInstance().getDialogDatas().size() <= Integer.parseInt(Sessions.getQuestionNo("dialog"))) {
            txtViewStatus1.setText("Game completed");
            lottieRefresh1.setVisibility(View.VISIBLE);
            lottieRefresh1.playAnimation();

        } else
            txtViewStatus1.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("dialog"))));

        if (Sessions.getQuestionNo("bgm").contentEquals("1")) {
            txtViewStatus2.setText("Let's Play");
        } else if (WordFinderManager.getInstance().getMusicDatas().size() != 0 &&
                WordFinderManager.getInstance().getMusicDatas().size() <= Integer.parseInt(Sessions.getQuestionNo("bgm"))) {
            txtViewStatus2.setText("Game completed");
            lottieRefresh2.setVisibility(View.VISIBLE);
            lottieRefresh2.playAnimation();

        } else
            txtViewStatus2.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("bgm"))));

        if (Sessions.getQuestionNo("banner").contentEquals("1")) {
            txtViewStatus3.setText("Let's Play");
        } else if (WordFinderManager.getInstance().getBannerDatas().size() != 0 &&
                WordFinderManager.getInstance().getBannerDatas().size() <= Integer.parseInt(Sessions.getQuestionNo("banner"))) {
            txtViewStatus3.setText("Game completed");
            lottieRefresh3.setVisibility(View.VISIBLE);
            lottieRefresh3.playAnimation();
        } else
            txtViewStatus3.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("banner"))));

    }
//
//    public void getAllDatas() {
//
//        NetworkConnection networkConnection = new NetworkConnection();
//        if (!networkConnection.isConnected(this)) {
//            networkConnection.buildDialog(this);
//            return;
//        }
//
//        if (WordFinderManager.getInstance().getBannerDatas().size() == 0) {
//
//            layoutProgress.setVisibility(View.VISIBLE);
//            avilayoutProgress.show();
//
//            DataManager.getDatamanager().getBannerDatas(new RetrofitCallBack<ArrayList<WordData>>() {
//                @Override
//                public void Success(ArrayList<WordData> wordData) {
//
//                    avilayoutProgress.hide();
//                    layoutProgress.setVisibility(View.GONE);
//
//
//                    WordFinderManager.getInstance().insertBannerDatas(wordData);
//                    if (Sessions.getQuestionNo("banner").contentEquals("1")) {
//                        txtViewStatus1.setText("Let's Play");
//                    } else if (WordFinderManager.getInstance().getBannerDatas().size() != 0 &&
//                            WordFinderManager.getInstance().getBannerDatas().size() <= Integer.parseInt(Sessions.getQuestionNo("banner"))) {
//                        txtViewStatus1.setText("Game completed");
//                        lottieRefresh1.setVisibility(View.VISIBLE);
//                        lottieRefresh1.playAnimation();
//                    } else
//                        txtViewStatus1.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("banner"))));
//
//                }
//
//                @Override
//                public void Failure(String error) {
//                    avilayoutProgress.hide();
//                    layoutProgress.setVisibility(View.GONE);
//                }
//            });
//        }
//
//        if (WordFinderManager.getInstance().getDialogDatas().size() == 0) {
//
//            layoutProgress.setVisibility(View.VISIBLE);
//            avilayoutProgress.show();
//
//            DataManager.getDatamanager().getDialogDatas(new RetrofitCallBack<ArrayList<WordData>>() {
//                @Override
//                public void Success(ArrayList<WordData> wordData) {
//
//                    avilayoutProgress.hide();
//                    layoutProgress.setVisibility(View.GONE);
//
//                    WordFinderManager.getInstance().insertDialogDatas(wordData);
//                    if (Sessions.getQuestionNo("dialog").contentEquals("1")) {
//                        txtViewStatus2.setText("Let's Play");
//                    } else if (WordFinderManager.getInstance().getDialogDatas().size() != 0 &&
//                            WordFinderManager.getInstance().getDialogDatas().size() <= Integer.parseInt(Sessions.getQuestionNo("dialog"))) {
//                        txtViewStatus2.setText("Game completed");
//                        lottieRefresh2.setVisibility(View.VISIBLE);
//                        lottieRefresh2.playAnimation();
//                    } else
//                        txtViewStatus2.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("dialog"))));
//
//                }
//
//                @Override
//                public void Failure(String error) {
//                    avilayoutProgress.hide();
//                    layoutProgress.setVisibility(View.GONE);
//                }
//            });
//
//        }
//
//        if (WordFinderManager.getInstance().getMusicDatas().size() == 0) {
//
//            layoutProgress.setVisibility(View.VISIBLE);
//            avilayoutProgress.show();
//
//            DataManager.getDatamanager().getMusicDatas(new RetrofitCallBack<ArrayList<WordData>>() {
//                @Override
//                public void Success(ArrayList<WordData> wordData) {
//
//                    avilayoutProgress.hide();
//                    layoutProgress.setVisibility(View.GONE);
//
//                    WordFinderManager.getInstance().insertMusicDatas(wordData);
//                    if (Sessions.getQuestionNo("bgm").contentEquals("1")) {
//                        txtViewStatus3.setText("Let's Play");
//                    } else if (WordFinderManager.getInstance().getMusicDatas().size() != 0 &&
//                            WordFinderManager.getInstance().getMusicDatas().size() <= Integer.parseInt(Sessions.getQuestionNo("bgm"))) {
//                        txtViewStatus3.setText("Game completed");
//                        lottieRefresh3.setVisibility(View.VISIBLE);
//                        lottieRefresh3.playAnimation();
//                    } else
//                        txtViewStatus3.setText("You are on level " + String.valueOf(Integer.parseInt(Sessions.getQuestionNo("bgm"))));
//
//                }
//
//                @Override
//                public void Failure(String error) {
//
//                    avilayoutProgress.hide();
//                    layoutProgress.setVisibility(View.GONE);
//                }
//            });
//        }
//
//    }

    @Override
    public void networkAvailable() {

        Log.d("NETWORK", "networkAvailable: ");
//        getAllDatas();
    }

    @Override
    public void networkUnavailable() {


        NetworkConnection networkConnection = new NetworkConnection();
        if (!networkConnection.isConnected(this)) {
            networkConnection.buildDialog(this);
        }
        Log.d("NETWORK", "networkUnavailable: ");
    }
}
