package net.xeill.elpuig.thinkitapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import net.xeill.elpuig.thinkitapp.model.Score;

import java.util.List;

/**
 * Created by dam2a on 24/01/18.
 */

@Dao
public interface ScoreDao {

    @Query("select * from score")
    LiveData<List<Score>> getScores();

    @Insert(onConflict = 1)
    long insertScore(Score score);

    @Delete
    void deleteScore(Score score);

    @Delete
    void deleteAllScores(List<Score> scoreList);
}