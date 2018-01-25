package net.xeill.elpuig.thinkitapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Score implements Comparable<Score>, Serializable {
    @PrimaryKey (autoGenerate = true)
    long id;

    private String user;
    private int score;
    private int level;
    private int correctAnswers;
    private int mistakes;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(@NonNull Score score) {
        if (this.score < score.score) {
            return 1;
        } else if (this.score > score.score) {
            return -1;
        } else {
            return 0;
        }
    }

    //UUID idOne = UUID.randomUUID();

}
