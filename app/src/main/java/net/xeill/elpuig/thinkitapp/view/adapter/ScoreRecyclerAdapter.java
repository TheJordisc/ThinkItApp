package net.xeill.elpuig.thinkitapp.view.adapter;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.model.Score;
import net.xeill.elpuig.thinkitapp.view.ScoreActivity;
import net.xeill.elpuig.thinkitapp.viewmodel.ScoreViewModel;

import java.util.List;

public class ScoreRecyclerAdapter extends RecyclerView.Adapter<ScoreRecyclerAdapter.ScoreViewHolder>{
    ScoreViewModel scoreViewModel;
    List<Score> scoreList;
    Long scoreId;
    Context context;

    public ScoreRecyclerAdapter(List<Score> scoreList, Long scoreId, Context context){
        this.scoreList = scoreList;
        this.scoreId = scoreId;
        this.context = context;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        scoreViewModel = ViewModelProviders.of((ScoreActivity)context).get(ScoreViewModel.class);
        View itemScore = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(itemScore);
    }

    @Override
    public void onBindViewHolder(final ScoreViewHolder holder, int position) {
        //AQUI
        holder.position.setText(position+1 +"");
        holder.name.setText(scoreList.get(position).getUser());
        holder.score.setText(scoreList.get(position).getScore() + "");

        switch (position) {
            case 0 :
                holder.trophy.setVisibility(View.VISIBLE);
                ViewCompat.setBackgroundTintList(holder.trophy, ColorStateList.valueOf(context.getResources().getColor(R.color.color_trophy_gold)));
                break;
            case 1:
                holder.trophy.setVisibility(View.VISIBLE);
                ViewCompat.setBackgroundTintList(holder.trophy, ColorStateList.valueOf(context.getResources().getColor(R.color.color_trophy_silver)));
                break;
            case 2:
                holder.trophy.setVisibility(View.VISIBLE);
                ViewCompat.setBackgroundTintList(holder.trophy, ColorStateList.valueOf(context.getResources().getColor(R.color.color_trophy_bronze)));
                break;
        }

        if (scoreId==scoreList.get(position).getId()) {
            holder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.color_score_highlight));
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreViewModel.deleteScore(scoreList.get(holder.getAdapterPosition()));
                reloadList();
            }
        });
    }

    private void reloadList() {
        scoreViewModel.getScores().observe((ScoreActivity)context, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable List<Score> result) {
                if (result != null) {
                    scoreList.clear();
                    scoreList.addAll(result);
                    ScoreRecyclerAdapter.this.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (scoreList != null ? scoreList.size() : 0);
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private TextView position;
        private TextView name;
        private TextView score;
        private ImageView trophy;
        private CardView cv;
        private AppCompatImageButton deleteButton;

        ScoreViewHolder(View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.pos);
            name = itemView.findViewById(R.id.score_name);
            score = itemView.findViewById(R.id.final_score);
            trophy = itemView.findViewById(R.id.trophy);
            cv = itemView.findViewById(R.id.cv);
            deleteButton=itemView.findViewById(R.id.delete_button);
        }
    }
}
