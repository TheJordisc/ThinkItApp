package net.xeill.elpuig.thinkitapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable {
    private String user;
    private int score;
    private int level;
    private int correctAnswers;
    private int mistakes;
    private int UUID;

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
