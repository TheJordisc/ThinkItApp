package net.xeill.elpuig.thinkitapp.view.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;


/**
 * Created by gerard on 11/03/2018.
 */

public class SoundManager {

    public interface OnLoadCompleteListener {
        void onLoadComplete(SoundManager soundManager);
    }

    public static class Builder {
        Context context;
        ArrayList<Integer> sounds = new ArrayList<>();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder addSound(int resId) {
            sounds.add(resId);
            return this;
        }

        public SoundManager build() {
            return new SoundManager(context, sounds);
        }
    }

    private OnLoadCompleteListener onLoadCompleteListener;
    private Context context;

    ArrayList<Integer> sounds = new ArrayList<>();

    private SoundPool mSoundPool;
    private SparseIntArray mSoundPoolMap = new SparseIntArray();
    SparseArray<Boolean> mSoundPoolLoadedMap = new SparseArray<>();
    private Handler mHandler = new Handler();

    SparseArray<MediaPlayer> mMediaPlayerMap = new SparseArray<>();
    SparseArray<Boolean> mMediaPlayerPausedMap = new SparseArray<>();

    private boolean mMuted = false;
    private float mVolume = 1;

    private static final int MAX_STREAMS = 2;
    private static final int STOP_DELAY_MILLIS = 3000;

    public SoundManager(Context context, final ArrayList<Integer> sounds) {
        this.context = context;
        this.sounds = sounds;
    }

    public void load(){
        if(sounds.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSoundPool = new SoundPool.Builder().setMaxStreams(15)
                        .build();
            } else {
                mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
            }

            for (Integer sound : sounds) {
                int soundId = mSoundPool.load(context, sound, 1);
                mSoundPoolMap.put(sound, soundId);
                mSoundPoolLoadedMap.put(soundId, false);
            }

            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int soundId, int status) {
                    if(status == 0) {
                        mSoundPoolLoadedMap.put(soundId, true);
                        dispatchOnLoadComplete();
                    }
                }
            });
        }
    }

    public void setOnLoadCompleteListener(OnLoadCompleteListener onLoadCompleteListener) {
        this.onLoadCompleteListener = onLoadCompleteListener;
    }

    private void dispatchOnLoadComplete(){
        if (onLoadCompleteListener == null) {
            return;
        }

        Boolean loaded = true;
        for (int i = 0; i < mSoundPoolLoadedMap.size(); i++) {
            loaded = loaded && mSoundPoolLoadedMap.get(mSoundPoolLoadedMap.keyAt(i));
        }

        if(loaded) {
            onLoadCompleteListener.onLoadComplete(SoundManager.this);
        }
    }

    public void playSound(int soundID) {
        if(mMuted){
            return;
        }

        final int soundId = mSoundPool.play(mSoundPoolMap.get(soundID), mVolume, mVolume, 1, 0, 1f);
        scheduleSoundStop(soundId);
    }

    public void playAudio(final int audioId) {
        try {
            mMediaPlayerMap.get(audioId).start();
        } catch (Exception e) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, audioId);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    setMuted(mMuted);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            });
            mMediaPlayerMap.put(audioId, mediaPlayer);
        }
    }

    public void stopAudios() {
        for (int i = 0; i < mMediaPlayerMap.size(); i++) {
            mMediaPlayerMap.get(mMediaPlayerMap.keyAt(i)).release();
        }
    }

    public void pauseAudios() {
        for (int i = 0; i < mMediaPlayerMap.size(); i++) {
            try {
                MediaPlayer mediaPlayer = mMediaPlayerMap.get(mMediaPlayerMap.keyAt(i));
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mMediaPlayerPausedMap.put(mMediaPlayerMap.keyAt(i), true);
                }
            } catch (Exception e) {

            }
        }
    }

    public void resumeAudios() {
        for (int i = 0; i < mMediaPlayerPausedMap.size(); i++) {
            if(mMediaPlayerPausedMap.get(mMediaPlayerPausedMap.keyAt(i))){
                try {
                    setMuted(mMuted);
                    mMediaPlayerMap.get(mMediaPlayerMap.keyAt(i)).start();
                } catch (Exception e){

                }
            }
        }
    }

    public void release(){
        for (int i = 0; i < mMediaPlayerMap.size(); i++) {
            mMediaPlayerMap.get(mMediaPlayerMap.keyAt(i)).release();
        }
    }

    private void scheduleSoundStop(final int soundId){
        mHandler.postDelayed(new Runnable() {
            public void run() {
                mSoundPool.stop(soundId);
            }
        }, STOP_DELAY_MILLIS);
    }

    public void setMuted(boolean muted) {
        this.mMuted = muted;

        if(muted) {
            for (int i = 0; i < mMediaPlayerMap.size(); i++) {
                try {
                    mMediaPlayerMap.get(mMediaPlayerMap.keyAt(i)).setVolume(0, 0);
                } catch (Exception e) {

                }
            }
        } else {
            for (int i = 0; i < mMediaPlayerMap.size(); i++) {
                try {
                    mMediaPlayerMap.get(mMediaPlayerMap.keyAt(i)).setVolume(mVolume, mVolume);
                } catch (Exception e){

                }
            }
        }
    }
}