package net.xeill.elpuig.thinkitapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import net.xeill.elpuig.thinkitapp.model.Score;

/**
 * Created by dam2a on 24/01/18.
 */

@Database(entities = {Score.class}, version = 1)
public abstract class ScoreDatabase extends RoomDatabase {

    private static ScoreDatabase INSTANCE;

    public static ScoreDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), ScoreDatabase.class, "db")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public abstract ScoreDao getScoreDao();
}