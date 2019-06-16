package saneesh.moviefun.worldfinder.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import saneesh.moviefun.worldfinder.DataManager;
import saneesh.moviefun.worldfinder.R;
import saneesh.moviefun.worldfinder.Sessions;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        txtView = findViewById(R.id.txtView);

//        Base.setStatusBarGradiant(this);

        Sessions.setSession(this);
        DataManager.getDatamanager().init(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);

//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Pair pairs = new Pair<View, String>(txtView, "homemovie");
                ActivityOptions activityOptions = null;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)

                {

                    activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, pairs);

                }

                startActivity(intent, activityOptions.toBundle());

                finish();
            }
        }, 3000);

    }
}
