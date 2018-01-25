package net.xeill.elpuig.thinkitapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import net.xeill.elpuig.thinkitapp.ScoreDao;
import net.xeill.elpuig.thinkitapp.ScoreDatabase;
import net.xeill.elpuig.thinkitapp.model.Score;

import java.util.List;

/**
 * Created by dam2a on 24/01/18.
 */

public class ScoreViewModel extends AndroidViewModel {
    private final MutableLiveData<Score> repository = new MutableLiveData<>();
    private final ScoreDao scoreDao;

    public ScoreViewModel(Application application) {
        super(application);

        scoreDao = ScoreDatabase.getInstance(this.getApplication()).getScoreDao();
    }

    public LiveData<List<Score>> getScores() {
        return scoreDao.getScores();
    }

    public MutableLiveData<Long> insertScore(final Score score){
        final MutableLiveData<Long> id = new MutableLiveData<>();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                id.postValue(scoreDao.insertScore(score));
                return null;
            }
        }.execute();

        return id;
    }

}