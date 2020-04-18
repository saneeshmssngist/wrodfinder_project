package saneesh.moviefun.wordfinder.activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import saneesh.moviefun.wordfinder.DataManager;
import saneesh.moviefun.wordfinder.NetworkStateReceiver;
import saneesh.moviefun.wordfinder.R;
import saneesh.moviefun.wordfinder.Sessions;
import saneesh.moviefun.wordfinder.WordFinderManager;
import saneesh.moviefun.wordfinder.adapters.WordAdapter;
import saneesh.moviefun.wordfinder.models.Coordinate;
import saneesh.moviefun.wordfinder.models.WordData;

public class WordFinderActivity extends BaseActivity implements NetworkStateReceiver.NetworkStateReceiverListener {


    private FlexboxLayout layoutContent;
    private WordAdapter wordAdapterFirst;
    private FrameLayout frameLayout, layoutMaster;
    private RecyclerView recyclerView;
    private int originalPoss = 0;
    private int letters = 0, enlargeFlagX = 50, enlargeFlagY = 50;
    private ArrayList<WordData> wordDataArray;
    private WordData wordData;
    private CardView cardViewBottom;
    private StringBuilder answerData = new StringBuilder();
    private boolean flag = true;
    private int centerX = 0, centerY = 0;
    private int countFirst = 0, countSecond = 0, i = 0, j = 0;
    private ArrayList<Coordinate> coordinates = new ArrayList<>();
    private TextView textVwQuestion, textVwAnswer, texttt, txtClue1, txtClue2, txtViewNetwork, txtViewQuestionNumber, txtViewQuestionNumber2;
    private ImageView imgView;
    private int questionNo = 0;
    private CardView cardContinue, cardViewImg;

    private MediaPlayer playAudio, plauButtonClick;
    private LottieAnimationView lottieWave, lottieRefresh, lottieRefresh2, lottieImageLoader;

    private RelativeLayout clueLayout, clueLayout2;
    private LinearLayout layoutDialog, layoutBanner;
    private String moveFindType = "";
    private AsyncTask asyncTask;
    private boolean isActvityFlag = true, isMusicPlayed = false;
    private NetworkStateReceiver networkStateReceiver;
    private RelativeLayout layoutLoadingAudio;
    private ImageView imagPlay;
    private boolean audioPlaying = false;

    private AdView adMobView;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        layoutContent = (FlexboxLayout) findViewById(R.id.layoutContent);
        cardViewBottom = (CardView) findViewById(R.id.cardViewBottom);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        layoutMaster = (FrameLayout) findViewById(R.id.layoutMaster);
        txtViewNetwork = (TextView) findViewById(R.id.txtViewNetwork);
        txtViewQuestionNumber = (TextView) findViewById(R.id.txtViewQuestionNumber);
        txtViewQuestionNumber2 = (TextView) findViewById(R.id.txtViewQuestionNumber2);
        imgView = (ImageView) findViewById(R.id.imgView);
        cardViewImg = (CardView) findViewById(R.id.cardViewImg);
        lottieWave = (LottieAnimationView) findViewById(R.id.lottie);
//        lottiePlay = (LottieAnimationView) findViewById(R.id.lottiePlay);
        lottieRefresh = (LottieAnimationView) findViewById(R.id.lottieRefresh);
        lottieRefresh2 = (LottieAnimationView) findViewById(R.id.lottieRefresh2);
        lottieImageLoader = (LottieAnimationView) findViewById(R.id.lottieImageLoader);
        layoutLoadingAudio = (RelativeLayout) findViewById(R.id.layoutLoadingAudio);
        imagPlay = (ImageView) findViewById(R.id.imagPlay);

        layoutBanner = (LinearLayout) findViewById(R.id.layoutBanner);
        layoutDialog = (LinearLayout) findViewById(R.id.layoutDialog);
        clueLayout = (RelativeLayout) findViewById(R.id.clueLayout);
        clueLayout2 = (RelativeLayout) findViewById(R.id.clueLayout2);
        txtClue1 = (TextView) findViewById(R.id.txtClue1);
        txtClue2 = (TextView) findViewById(R.id.txtClue2);
        texttt = (TextView) findViewById(R.id.texttt);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        FlexboxLayoutManager flexboxLayout = new FlexboxLayoutManager(this);
        flexboxLayout.setFlexDirection(FlexDirection.ROW);
        flexboxLayout.setJustifyContent(JustifyContent.CENTER);
        flexboxLayout.setAlignItems(AlignItems.CENTER);
        recyclerView.setLayoutManager(flexboxLayout);

//        setUpAdmob();
        wordAdapterFirst = new WordAdapter(this);
        recyclerView.setAdapter(wordAdapterFirst);

        txtViewQuestionNumber.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        plauButtonClick = MediaPlayer.create(WordFinderActivity.this, R.raw.buttonclick);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        plauButtonClick.setVolume(0.2f, 0.2f);

        imagPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (audioPlaying) {
                    playAudio.pause();
                    lottieWave.pauseAnimation();
                    audioPlaying = false;
                    imagPlay.setImageResource(R.drawable.ic_play_arrow);
                } else {
                    playAudio.start();
                    lottieWave.playAnimation();
                    audioPlaying = true;
                    imagPlay.setImageResource(R.drawable.ic_stop_arrow);
                }
            }
        });
        lottieRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lottieRefresh.playAnimation();
                reSetContent();
            }
        });

        lottieRefresh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lottieRefresh2.playAnimation();
                reSetContent();
            }
        });

        moveFindType = getIntent().getStringExtra("type");

        switch (moveFindType) {
            case "banner":
                wordDataArray = WordFinderManager.getInstance().getBannerDatas();
                break;
            case "dialog":
                wordDataArray = WordFinderManager.getInstance().getDialogDatas();
                break;
            case "bgm":
                wordDataArray = WordFinderManager.getInstance().getMusicDatas();
                break;
        }

        final ViewTreeObserver observer = layoutMaster.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (flag) {

                            clueLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialogHelp();
                                }
                            });

                            clueLayout2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialogHelp();
                                }
                            });
                            flag = false;
                            setContent();
                        }
                    }
                });

        lottieWave.pauseAnimation();

        questionNo = getIntent().getIntExtra("level", 0);
        setDatas();

    }

    private void showDialogHelp() {

        final Dialog dialog = new Dialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.layout_help_option, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        CardView cardGetOne = (CardView) view.findViewById(R.id.cardGetOne);
        CardView cardGetShare = (CardView) view.findViewById(R.id.cardGetShare);

        cardGetOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                reSetContent();
                clueLayout2.setClickable(false);
                enableClues();
            }
        });
        cardGetShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                downloadFile();
            }
        });


    }

    private String fileName = "";
    private void downloadFile() {

        playAudio.pause();
        lottieWave.pauseAnimation();
        audioPlaying = false;
        imagPlay.setImageResource(R.drawable.ic_play_arrow);
        Toast.makeText(this,"File Downloading...",Toast.LENGTH_SHORT).show();

        Uri downloadUri = Uri.parse(wordData.getQuestion());

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setMimeType(getMimeType(downloadUri.toString()));
        request.setTitle("File Downloading ...");
        request.setVisibleInDownloadsUi(true);
        fileName = "/WordFinder/" + new SimpleDateFormat("dd_hh_mm_ss").format(new Date())+ ".mp3";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        if (downloadManager != null) {
            long downloadId = downloadManager.enqueue(request);
        }
    }


    BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                String sharePath = Environment.getExternalStorageDirectory().getPath() +"/Download/"+fileName;
                Uri uri = Uri.parse(sharePath);
                Intent shareMedia = new Intent(Intent.ACTION_SEND_MULTIPLE);
                shareMedia.setPackage("com.whatsapp");
                shareMedia.setType("*/*");
                String[] extraMimeTypes = {"audio/*","text/plain"};
                shareMedia.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
                shareMedia.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                shareMedia.putExtra(Intent.EXTRA_STREAM, uri);
                shareMedia.putExtra(Intent.EXTRA_SUBJECT,"SUBJECT" );
                shareMedia.putExtra(Intent.EXTRA_TEXT, "Guess the movie name!!");
                startActivity(Intent.createChooser(shareMedia, "Share Sound File"));

            }
        }
    };

    private void setUpAdmob() {

        //admob sync..
        MobileAds.initialize(this, getResources().getString(R.string.APPID));

        adMobView = (AdView) findViewById(R.id.adMobView);
        adMobView.loadAd(new AdRequest.Builder().build());

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.INTERTITIAID));
        interstitialAd.loadAd(new AdRequest.Builder().build());

    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (playAudio != null && !playAudio.isPlaying()) {
//            playAudio.start();
//
//            audioPlaying = true;
//            imagPlay.setImageResource(R.drawable.ic_stop_arrow);
//        }

    }

    private void setDatas() {

//        lottiePlay.setEnabled(false);
        lottieRefresh.playAnimation();
        lottieRefresh2.playAnimation();

        lottieWave.setVisibility(View.GONE);
        layoutLoadingAudio.setVisibility(View.VISIBLE);

//        lottiePlay.setSpeed(1);
//        lottiePlay.playAnimation();
//        switch (moveFindType) {
//            case "banner":
//                questionNo = Integer.parseInt(Sessions.getQuestionNo("banner"));
//                break;
//            case "dialog":
//                questionNo = Integer.parseInt(Sessions.getQuestionNo("dialog"));
//                break;
//            case "bgm":
//                questionNo = Integer.parseInt(Sessions.getQuestionNo("bgm"));
//                break;
//        }

        txtClue1.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        txtClue2.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        texttt.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));

        txtViewQuestionNumber.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        txtViewQuestionNumber2.setTypeface(Typeface.createFromAsset(getAssets(), "josefinsans.ttf"));
        txtViewQuestionNumber.setText("Level " + questionNo);
        txtViewQuestionNumber2.setText("Level " + questionNo);

        if (questionNo <= wordDataArray.size())
            wordData = wordDataArray.get(questionNo - 1);


        layoutDialog.setVisibility(View.GONE);
        layoutBanner.setVisibility(View.GONE);

        if (moveFindType.contentEquals("banner")) {
            layoutBanner.setVisibility(View.VISIBLE);
            lottieImageLoader.setVisibility(View.VISIBLE);

            Glide
                    .with(this)
                    .load(wordData.getQuestion())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            lottieImageLoader.setVisibility(View.GONE);
                            imgView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imgView);

            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkTimeDelay())
                        return;
                    Dialog dialog = new Dialog(WordFinderActivity.this);
                    View view = LayoutInflater.from(WordFinderActivity.this).inflate(R.layout.dialog_image_layout, null);
                    final ImageView imgViews = view.findViewById(R.id.imgView);
                    final LottieAnimationView imgViewLoader = view.findViewById(R.id.lottieImageLoader);

                    Glide
                            .with(WordFinderActivity.this)
                            .load(wordData.getQuestion())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    imgViewLoader.setVisibility(View.GONE);
                                    imgViews.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            })
                            .into(imgViews);


                    dialog.setContentView(view);
                    dialog.show();
                }
            });
        } else if (moveFindType.contentEquals("")) {

//            txtQuestion.setText(wordData.getQuestion());
//            txtQuestion.setTypeface(Typeface.createFromAsset(getAssets(), "sourcesanprosamibold.ttf"));

        } else if (moveFindType.contentEquals("dialog") || moveFindType.contentEquals("bgm")) {
            layoutDialog.setVisibility(View.VISIBLE);

//            int sourceId = getResources().getIdentifier(wordData.getQuestion(), "raw", WordFinderActivity.this.getPackageName());
//
//            asyncTask = new AsyncTask<Void, Void, String>() {
//
//                @Override
//                protected String doInBackground(Void... voids) {

            playAudio = new MediaPlayer();
            playAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            playAudio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
//                    lottiePlay.setEnabled(true);

                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    lottieWave.playAnimation();
                                    layoutLoadingAudio.setVisibility(View.GONE);
                                    lottieWave.setVisibility(View.VISIBLE);
                                }
                            });
                }
            });
            playAudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    lottieWave.pauseAnimation();
//                    lottiePlay.setSpeed(-1);
                    audioPlaying = false;
//                    lottiePlay.playAnimation();

                    if (isActvityFlag)
                        new Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.d("TAG", "run: isMusicPlayed");
//                                        if (isMusicPlayed)
//                                            return;
                                        isMusicPlayed = true;
//                                        lottiePlay.setSpeed(1);
//                                        lottiePlay.playAnimation();
                                        imagPlay.setImageResource(R.drawable.ic_stop_arrow);
                                        playAudio.start();
                                        lottieWave.playAnimation();
                                        audioPlaying = true;

                                    }
                                }, 2000
                        );

                }
            });

            playAudio.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    Log.d("", "onSeekComplete: ");
                }
            });

            playAudio.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });

            try {
                playAudio.setDataSource(wordData.getQuestion());
                playAudio.prepareAsync();


            } catch (IllegalStateException exception) {
                // Output expected IllegalStateException.
            } catch (IOException e) {
                e.printStackTrace();
            }

            imagPlay.setImageResource(R.drawable.ic_stop_arrow);
            playAudio.setVolume(1, 1);
            playAudio.start();


//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(String s) {
//                    super.onPostExecute(s);
//                }
//            }.execute();

        }

        int count;
        if (moveFindType.contentEquals("banner"))
            count = wordData.getContentDatas().size();
        else
            count = wordData.getContentsArray().size();

//        if (count > 6) {
//            countFirst = count - (count / 2);
//            countSecond = count / 2;
//            wordAdapterSecond.update(count / 2);
//            wordAdapterFirst.update(count - (count / 2));
//        } else {
//            countFirst = count;
        wordAdapterFirst.update(count);
//        }


    }

    private void setContent() {

        int flag = 0, x = 0, y = 0;

        centerX = layoutMaster.getWidth() / 2;
        centerY = (layoutMaster.getHeight() - convertDpToPixel(100)) / 2;

        int arraySize = 0;
        switch (moveFindType) {
            case "banner":
                arraySize = wordData.getContentDatas().size();
                break;
            case "bgm":
            case "dialog":
                arraySize = wordData.getContentsArray().size();
                break;
        }

        for (int i = 0; i < arraySize; i++) {
            final View view = LayoutInflater.from(this).inflate(R.layout.letter_content, null);
            TextView txtLetter = (TextView) view.findViewById(R.id.txtLetter);
            CardView circleView1 = (CardView) view.findViewById(R.id.circleView1);

            switch (moveFindType) {
                case "banner":
                    view.setTag(wordData.getContentDatas().get(i).getId());
                    txtLetter.setText(wordData.getContentDatas().get(i).getData());
                    break;
                case "bgm":
                case "dialog":
                    view.setTag(wordData.getContentsArray().get(i));
                    txtLetter.setText(wordData.getContentsArray().get(i));
                    break;
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (checkTimeDelay())
                        return;

                    plauButtonClick.start();

//                    final Animation zoomout = AnimationUtils.loadAnimation(WordFinderActivity.this, R.anim.zoomout);
                    final Animation zoomin = AnimationUtils.loadAnimation(WordFinderActivity.this, R.anim.zoom_in);
                    v.startAnimation(zoomin);
//                    v.startAnimation(zoomout);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveContent(v);

                        }
                    }, 200);

                }
            });

//            if (flag > 3) {
//                flag = 0;
//                enlargeFlagX += 200;
//                enlargeFlagY += 250;
//            }
//
//
//            switch (flag) {
//                case 0:
//                    x = centerX - getRandomX();
//                    y = centerY - getRandomY();
//                    break;
//                case 1:
//                    x = centerX - getRandomX();
//                    y = centerY + getRandomY();
//                    break;
//                case 2:
//                    x = centerX + getRandomX();
//                    y = centerY + getRandomY();
//                    break;
//                case 3:
//                    x = centerX + getRandomX();
//                    y = centerY - getRandomY();
//                    break;
//
//            }

//            view.setX(x);
//            view.setY(y);
//
//            flag++;

            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int x1 = getRandomX();
            int y1 = getRandomX();
            layoutParams.setMargins(x1, y1, 0, 0);
            view.setLayoutParams(layoutParams);

            Coordinate coordinate = new Coordinate();
            coordinate.setXCord(x1);
            coordinate.setYCord(y1);
            coordinates.add(i, coordinate);

            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
            layoutContent.setLayoutAnimation(controller);
            layoutContent.addView(view);
        }

    }

    public void enableClues() {

        for (int j = 0; j < 1; j++)
            for (int i = 0; i < layoutContent.getChildCount(); i++) {
                View view = layoutContent.getChildAt(i);

                if (moveFindType.contentEquals("banner")) {
                    if (String.valueOf(wordData.getAnswerCount().get(0).charAt(j)).toLowerCase().contentEquals(view.getTag().toString().toLowerCase())) {
                        moveContent(view);
                        break;
                    }

                } else {
                    if (String.valueOf(wordData.getAnswer().charAt(j)).toLowerCase().contentEquals(view.getTag().toString().toLowerCase())) {
                        moveContent(view);
                        break;
                    }
                }
            }

    }


    private int getRandomX() {

        float density = getResources().getDisplayMetrics().density;
        int movePixeel = 0;
        if (density == 0.75f) {
            // LDPI
        } else if (density >= 1.0f && density < 1.5f) {
            enlargeFlagX = 10;
            movePixeel = 30;
            // MDPI
        } else if (density == 1.5f) {
            enlargeFlagX = 10;
            movePixeel = 30;
            // HDPI
        } else if (density > 1.5f && density <= 2.0f) {
            enlargeFlagX = 20;
            movePixeel = 50;
            // XHDPI
        } else if (density > 2.0f && density <= 3.0f) {
            enlargeFlagX = 50;
            movePixeel = 70;
            // XXHDPI
        } else {
            enlargeFlagX = 50;
            movePixeel = 70;
            // XXXHDPI
        }
        switch (moveFindType) {
            case "banner":
                if (wordData.getContentDatas().size() > 10)
                    movePixeel -= 30;
                break;
            case "bgm":
            case "dialog":
                if (wordData.getContentsArray().size() > 10)
                    movePixeel -= 30;
                break;
        }

        return new Random().nextInt(movePixeel) + enlargeFlagX;
    }

    private int getRandomY() {
        return new Random().nextInt(250) + enlargeFlagY;
    }

    private void moveContent(View circleView) {

        circleView.setClickable(false);

        if (moveFindType.contentEquals("banner")) {
            if (originalPoss < Integer.parseInt(wordData.getLetterCount())) {
                answerData.append(circleView.getTag());
            }
        } else {
            if (originalPoss < wordData.getContentsArray().size()) {
                answerData.append(circleView.getTag());
            }
        }

//or view.getLocationOnScreen(originalPos)
        float x = frameLayout.getX() + cardViewBottom.getX() + recyclerView.getX() + recyclerView.getChildAt(originalPoss).getX();
        float y = frameLayout.getY() + cardViewBottom.getY() + recyclerView.getY() + recyclerView.getChildAt(originalPoss).getY();

        circleView.animate()
                .setDuration(1000)
                .x(x)
                .y(y)
                .start();

        int count = 0;
        if (moveFindType.contentEquals("banner"))
            count = Integer.parseInt(wordData.getLetterCount());
        else
            count = wordData.getContentsArray().size();

        if (originalPoss == count - 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (checkAnswer()) {

                        if (playAudio != null)
                            playAudio.stop();
                        lottieWave.pauseAnimation();
//                        lottiePlay.setSpeed(-1);
                        audioPlaying = false;
                        imagPlay.setImageResource(R.drawable.ic_play_arrow);
//                        lottiePlay.playAnimation();
                        showScratchDialog();

                    } else {
                        Toast.makeText(WordFinderActivity.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                        reSetContent();
                    }
                    return;
                }

            }, 1000);

        }

        originalPoss++;
    }

    private boolean checkAnswer() {

        switch (moveFindType) {
            case "banner":
                for (int i = 0; i < wordData.getAnswerCount().size(); i++)
                    if (wordData.getAnswerCount().get(i).contentEquals(answerData))
                        return true;
                break;
            case "bgm":
            case "dialog":
                if (wordData.getAnswer().replaceAll("\\s+", "").toLowerCase().contentEquals(answerData.toString().toLowerCase()))
                    return true;
                break;
        }

        return false;
    }

    private void reSetContent() {

        originalPoss = 0;
        i = 0;
        j = 0;
        answerData.setLength(0);
        answerData = new StringBuilder();
        layoutContent.removeAllViews();

        int arraySize = 0;
        switch (moveFindType) {
            case "banner":
                arraySize = wordData.getContentDatas().size();
                break;
            case "bgm":
            case "dialog":
                arraySize = wordData.getContentsArray().size();
                break;
        }

        for (int i = 0; i < arraySize; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.letter_content, null);
            TextView txtLetter = (TextView) view.findViewById(R.id.txtLetter);
            CardView circleView1 = (CardView) view.findViewById(R.id.circleView1);

            switch (moveFindType) {
                case "banner":
                    view.setTag(wordData.getContentDatas().get(i).getId());
                    txtLetter.setText(wordData.getContentDatas().get(i).getData());
                    break;
                case "bgm":
                case "dialog":
                    view.setTag(wordData.getContentsArray().get(i));
                    txtLetter.setText(wordData.getContentsArray().get(i));
                    break;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (checkTimeDelay())
                        return;

                    plauButtonClick.start();
//                    final Animation zoomout = AnimationUtils.loadAnimation(WordFinderActivity.this, R.anim.zoomout);
                    final Animation zoomin = AnimationUtils.loadAnimation(WordFinderActivity.this, R.anim.zoom_in);
                    v.startAnimation(zoomin);
//                    v.startAnimation(zoomout);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveContent(v);

                        }
                    }, 200);

                }
            });

            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(coordinates.get(i).getXCord(), coordinates.get(i).getYCord(), 0, 0);
            view.setLayoutParams(layoutParams);

            layoutContent.addView(view);

        }
    }

    private void showScratchDialog() {

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_scratchcard, null);
        cardContinue = (CardView) view.findViewById(R.id.cardContinue);
        textVwQuestion = (TextView) view.findViewById(R.id.textVwQuestion);
        textVwAnswer = (TextView) view.findViewById(R.id.textVwAnswer);

        RelativeLayout layoutImage = (RelativeLayout) view.findViewById(R.id.layoutImage);
        final ImageView imgViews = view.findViewById(R.id.imgView);
        final LottieAnimationView imgViewLoader = view.findViewById(R.id.lottieImageLoader);

        if (moveFindType.contentEquals("banner")) {
            layoutImage.setVisibility(View.VISIBLE);
            textVwQuestion.setVisibility(View.GONE);

            Glide
                    .with(WordFinderActivity.this)
                    .load(wordData.getQuestion())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imgViewLoader.setVisibility(View.GONE);
                            imgViews.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imgViews);

        } else if (moveFindType.contentEquals("dialog") || moveFindType.contentEquals("bgm")) {
            layoutImage.setVisibility(View.GONE);
            textVwQuestion.setVisibility(View.GONE);
        }
        textVwQuestion.setTypeface(Typeface.createFromAsset(getAssets(), "sourcesanspro.ttf"));

        textVwAnswer.setText(wordData.getAnswer());
        textVwAnswer.setTypeface(Typeface.createFromAsset(getAssets(), "sourcesanprosamibold.ttf"));

        cardContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                originalPoss = 0;
                i = 0;
                j = 0;
                answerData.setLength(0);
                answerData = new StringBuilder();
                layoutContent.removeAllViews();

                questionNo++;

                if (questionNo <= wordDataArray.size()) {

                    switch (moveFindType) {
                        case "banner":
                            if (Integer.valueOf(Sessions.getQuestionNo("banner")) < questionNo)
                                Sessions.setQuestionNo("banner", String.valueOf(questionNo));
                            break;
                        case "dialog":
                            if (Integer.valueOf(Sessions.getQuestionNo("dialog")) < questionNo)
                                Sessions.setQuestionNo("dialog", String.valueOf(questionNo));
                            break;
                        case "bgm":
                            if (Integer.valueOf(Sessions.getQuestionNo("bgm")) < questionNo)
                                Sessions.setQuestionNo("bgm", String.valueOf(questionNo));
                            break;
                    }

                    clueLayout.setClickable(true);
                    clueLayout2.setClickable(true);

                    if (questionNo % 5 == 0) {
                        setInterstitialAd();
                    } else {
                        setDatas();
                        setContent();
                    }

                    dialog.hide();
                } else {
                    Toast.makeText(WordFinderActivity.this, "Section completed", Toast.LENGTH_SHORT);
                    finish();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();


    }

    public int convertDpToPixel(int dp) {
        return dp * (getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public int convertPixelsToDp(int px) {
        return px / (getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public String loadJSONFromAsset() {
        String json = null;
        InputStream is = null;
        try {

            if (moveFindType.contentEquals("banner")) {
                is = getAssets().open("sample.json");
            } else if (moveFindType.contentEquals("dialog")) {
                is = getAssets().open("movie_dialog.json");
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private long mLastClickTime = 0;

    public boolean checkTimeDelay() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 200) {
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // Handle button clicks

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if(asyncTask != null)
//            asyncTask.cancel(true);

//        if(downloadReceiver != null)
//            unregisterReceiver(downloadReceiver);
//          if(networkStateReceiver != null)
//            unregisterReceiver(networkStateReceiver);

        isActvityFlag = false;
        if (playAudio != null)
            playAudio.stop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (playAudio != null)
            playAudio.stop();
    }

    @Override
    public void networkAvailable() {
        txtViewNetwork.setVisibility(View.GONE);
    }

    @Override
    public void networkUnavailable() {
        txtViewNetwork.setVisibility(View.VISIBLE);
    }

    private void setInterstitialAd() {

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                setDatas();
                setContent();
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {

            setDatas();
            setContent();
        }
    }

    public String getEmoji(int unicode) {
        return new String(Character.toChars(unicode));
    }


}
