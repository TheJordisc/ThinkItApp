package net.xeill.elpuig.thinkitapp.view.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.model.Score;

import java.util.List;

public class ScoreRecyclerAdapter extends RecyclerView.Adapter<ScoreRecyclerAdapter.ScoreViewHolder>{

    List<Score> scoreList;

    public ScoreRecyclerAdapter(List<Score> scoreList){
        this.scoreList = scoreList;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemScore = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(itemScore);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        //AQUI
        holder.position.setText(position+1 +"");
        holder.name.setText(scoreList.get(position).getUser());
        holder.score.setText(scoreList.get(position).getScore() + "");

        switch (position) {
            case 0 :
                holder.trophy.setVisibility(View.VISIBLE);
                ViewCompat.setBackgroundTintList(holder.trophy, ColorStateList.valueOf(Color.parseColor("#FFD700")));
                break;
            case 1:
                holder.trophy.setVisibility(View.VISIBLE);
                ViewCompat.setBackgroundTintList(holder.trophy, ColorStateList.valueOf(Color.parseColor("#C0C0C0")));
                break;
            case 2:
                holder.trophy.setVisibility(View.VISIBLE);
                ViewCompat.setBackgroundTintList(holder.trophy, ColorStateList.valueOf(Color.parseColor("#CD7f32")));
                break;
        }
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

        ScoreViewHolder(View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.pos);
            name = itemView.findViewById(R.id.score_name);
            score = itemView.findViewById(R.id.final_score);
            trophy = itemView.findViewById(R.id.trophy);
        }
    }
}
