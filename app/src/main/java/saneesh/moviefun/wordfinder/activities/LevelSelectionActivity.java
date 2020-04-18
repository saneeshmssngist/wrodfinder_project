package saneesh.moviefun.wordfinder.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Random;

import saneesh.moviefun.wordfinder.DataManager;
import saneesh.moviefun.wordfinder.NetworkConnection;
import saneesh.moviefun.wordfinder.NetworkStateReceiver;
import saneesh.moviefun.wordfinder.R;
import saneesh.moviefun.wordfinder.Sessions;
import saneesh.moviefun.wordfinder.WordFinderManager;
import saneesh.moviefun.wordfinder.adapters.LevelsSelectionAdapter;
import saneesh.moviefun.wordfinder.interfaces.RetrofitCallBack;
import saneesh.moviefun.wordfinder.models.WordData;

import static saneesh.moviefun.wordfinder.activities.BaseActivity.setStatusBarGradiant;

public class LevelSelectionActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    private RecyclerView recyclerView;

    private LinearLayout layoutProgress;
    private AVLoadingIndicatorView avilayoutProgress;
    private LevelsSelectionAdapter selectionAdapter;
    private ArrayList<WordData> wordDataArray;
    private String type = "";

    private RelativeLayout layoutTop;
    private TextView txtLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        type = getIntent().getStringExtra("type");
        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDatas();
    }

    private void initViews() {


        layoutProgress = findViewById(R.id.layoutProgress);
        avilayoutProgress = findViewById(R.id.avilayoutProgress);
        layoutTop = findViewById(R.id.layoutTop);
        txtLevel = findViewById(R.id.txtLevel);
        txtLevel.setTypeface(Typeface.createFromAsset(getAssets(), "pt_sans.ttf"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        selectionAdapter = new LevelsSelectionAdapter(this, type, new ArrayList<WordData>(), new LevelsSelectionAdapter.EventClickListener() {
            @Override
            public void onTapped(int position) {

                startActivity(new Intent(LevelSelectionActivity.this, WordFinderActivity.class)
                        .putExtra("type", type)
                        .putExtra("level", position + 1)
                );

            }
        });
        recyclerView.setAdapter(selectionAdapter);

    }

    private void setDatas() {

        avilayoutProgress.show();
        layoutProgress.setVisibility(View.VISIBLE);

        if (type.contentEquals("banner")) {

            setStatusBarGradiant(this, getResources().getDrawable(R.drawable.card_bg1_status));
            layoutTop.setBackground(getResources().getDrawable(R.drawable.card_bg1_status));

            DataManager.getDatamanager().getBannerDatas(new RetrofitCallBack<ArrayList<WordData>>() {
                @Override
                public void Success(ArrayList<WordData> wordData) {

                    avilayoutProgress.hide();
                    layoutProgress.setVisibility(View.GONE);

                    wordDataArray = wordData;
                    txtLevel.setText(Sessions.getQuestionNo("banner") + "/" + wordDataArray.size());
                    WordFinderManager.getInstance().insertBannerDatas(wordData);
                    selectionAdapter.update(wordData);

                }

                @Override
                public void Failure(String error) {
                    avilayoutProgress.hide();
                    layoutProgress.setVisibility(View.GONE);
                }
            });

        } else if (type.contentEquals("dialog")) {
            setStatusBarGradiant(this, getResources().getDrawable(R.drawable.card_bg2_status));
            layoutTop.setBackground(getResources().getDrawable(R.drawable.card_bg2_status));

            DataManager.getDatamanager().getDialogDatas(new RetrofitCallBack<ArrayList<WordData>>() {
                @Override
                public void Success(ArrayList<WordData> wordData) {

                    avilayoutProgress.hide();
                    layoutProgress.setVisibility(View.GONE);

                    wordDataArray = setParseData(wordData);
                    txtLevel.setText(Sessions.getQuestionNo("dialog") + "/" + wordDataArray.size());
                    WordFinderManager.getInstance().insertDialogDatas(wordData);
                    selectionAdapter.update(wordData);

                }

                @Override
                public void Failure(String error) {
                    avilayoutProgress.hide();
                    layoutProgress.setVisibility(View.GONE);
                }
            });
        } else if (type.contentEquals("bgm")) {

            setStatusBarGradiant(this, getResources().getDrawable(R.drawable.card_bg3_status));
            layoutTop.setBackground(getResources().getDrawable(R.drawable.card_bg3_status));

            DataManager.getDatamanager().getMusicDatas(new RetrofitCallBack<ArrayList<WordData>>() {
                @Override
                public void Success(ArrayList<WordData> wordData) {

                    avilayoutProgress.hide();
                    layoutProgress.setVisibility(View.GONE);

                    wordDataArray = setParseData(wordData);
                    txtLevel.setText(Sessions.getQuestionNo("bgm") + "/" + wordDataArray.size());
                    WordFinderManager.getInstance().insertMusicDatas(wordData);
                    selectionAdapter.update(wordData);

                }

                @Override
                public void Failure(String error) {
                    avilayoutProgress.hide();
                    layoutProgress.setVisibility(View.GONE);
                }
            });
        }

    }

    private ArrayList<WordData> setParseData(ArrayList<WordData> wordData) {

        for (int i = 0; i < wordData.size(); i++) {

            String data = wordData.get(i).getAnswer().replaceAll(" ", "");

            ArrayList<String> strings = new ArrayList<>();
            for (int j = 0; j < data.length(); j++) {
                strings.add(String.valueOf(data.charAt(j)));
            }

            wordData.get(i).setContentsArray(reorderData(strings));
        }

        return wordData;

    }

    private ArrayList<String> reorderData(ArrayList<String> strings) {

        Random rand = new Random();

        for (int i = 0; i < strings.size(); i++) {
            int randomIndexToSwap = rand.nextInt(strings.size());
            String temp = strings.get(randomIndexToSwap);
            strings.set(randomIndexToSwap, strings.get(i));
            strings.set(i, temp);
        }

        return strings;

    }


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
