package saneesh.moviefun.wordfinder.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import saneesh.moviefun.wordfinder.R;
import saneesh.moviefun.wordfinder.Sessions;
import saneesh.moviefun.wordfinder.activities.WordFinderActivity;
import saneesh.moviefun.wordfinder.models.WordData;

public class LevelsSelectionAdapter extends RecyclerView.Adapter<LevelsSelectionAdapter.LevelHolder> {

    private Context context;
    private ArrayList<WordData> wordData;
    private EventClickListener eventClickListener;
    private String type = "";
    private MediaPlayer plauButtonClick;

    public interface EventClickListener {
        void onTapped(int position);
    }

    public LevelsSelectionAdapter(Context context, String type, ArrayList<WordData> wordData, EventClickListener eventClickListener) {
        this.context = context;
        this.type = type;
        this.wordData = wordData;
        this.eventClickListener = eventClickListener;
        plauButtonClick = MediaPlayer.create(context, R.raw.buttonclick);
        plauButtonClick.setVolume(0.2f, 0.2f);

    }

    @Override
    public int getItemCount() {
        return wordData.size();
    }

    @NonNull
    @Override
    public LevelHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_level_selection, viewGroup, false);
        return new LevelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LevelHolder levelHolder, final int i) {

        if (Integer.parseInt(Sessions.getQuestionNo(type)) < (i + 1)) {
            levelHolder.cardView2.setVisibility(View.VISIBLE);
            levelHolder.imageInner.setVisibility(View.GONE);
            levelHolder.txtLevel.setText("");

        } else {
            levelHolder.cardView2.setVisibility(View.GONE);
            levelHolder.imageInner.setVisibility(View.VISIBLE);
            levelHolder.txtLevel.setText(String.valueOf(i + 1));

            levelHolder.imageInner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Animation zoomin = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    levelHolder.imageInner.startAnimation(zoomin);
                    plauButtonClick.start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            eventClickListener.onTapped(i);

                        }
                    }, 200);

                }
            });
        }

    }

    public void update(ArrayList<WordData> wordData) {
        this.wordData = wordData;
        notifyDataSetChanged();
    }

    public class LevelHolder extends RecyclerView.ViewHolder {

        private CardView cardView2;
        private CircleImageView imageInner;
        private TextView txtLevel;

        public LevelHolder(@NonNull View itemView) {
            super(itemView);

            cardView2 = (CardView) itemView.findViewById(R.id.cardView2);
            imageInner = (CircleImageView) itemView.findViewById(R.id.imageInner);
            txtLevel = (TextView) itemView.findViewById(R.id.txtLevel);
        }
    }
}
