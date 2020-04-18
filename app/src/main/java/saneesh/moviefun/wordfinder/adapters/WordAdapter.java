package saneesh.moviefun.wordfinder.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import saneesh.moviefun.wordfinder.R;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder>
{

    private Context context;
    private int answerCount;

    public WordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return answerCount;
    }

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_word,viewGroup,false);

        return new WordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder wordHolder, int position) {


    }

    public void update(int answerCount) {
        this.answerCount = answerCount;
        notifyDataSetChanged();
    }

    public class WordHolder extends RecyclerView.ViewHolder {

        public WordHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
