package com.lanxin.testdemo.media;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import com.lanxin.testdemo.app.BContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author:张小兵
 * @e-mail:460116602@qq.com
 * @date:2024/01/15
 */
public class MyAudioManager {
    private static final String TAG = "zxb-test";
    private static volatile MyAudioManager myAudioManager = null;
    private static AudioManager audioManager = null;
    private AudioManager.OnModeChangedListener onModeChangedListener;
    private int mVolume;
    private double maxVolumt = 16;
    private int mMusicVolume;
    private double maxMusicVolume = 16;
    private int mStreamType = AudioManager.STREAM_VOICE_CALL;
    private int mStreamFlag = AudioManager.FLAG_SHOW_UI;
    private OnVolumeListener onVolumeListener;
    private AtomicBoolean fisrt = new AtomicBoolean(false);
    private AtomicBoolean isDown = new AtomicBoolean(false);
    private String currentText = "听筒";

    public static MyAudioManager getInstance() {
        if (null == myAudioManager) {
            synchronized (MyAudioManager.class) {
                if (null == myAudioManager) {
                    myAudioManager = new MyAudioManager();
                }
            }
        }
        return myAudioManager;
    }

    public void switchSpeakerOnModle(boolean isSpeaker) {
        Log.e(TAG, "switchSpeakerOnModle" + isSpeaker);
        AudioManager audioManager = getAudioManager();
        if (!fisrt.get()) {
            fisrt.set(true);
            maxVolumt = audioManager.getStreamMaxVolume(mStreamType);
            mVolume = audioManager.getStreamVolume(mStreamType);
            maxMusicVolume = maxVolumt;
            mMusicVolume = mVolume;

            Log.e(TAG, "first mVolume=" + mVolume + "maxVolumt=" + maxVolumt);
            Log.e(TAG, "first mMusicVolume=" + mMusicVolume + "maxMusicVolume=" + maxMusicVolume);
        }


        if (!isSpeaker) {
            currentText = "听筒";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                audioManager.setSpeakerphoneOn(false);
                audioManager.setStreamVolume(mStreamType, mVolume, AudioManager.FLAG_PLAY_SOUND);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioManager.setSpeakerphoneOn(false);
                //只能3 ，设置0无效
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                audioManager.setStreamVolume(mStreamType, mVolume, AudioManager.FLAG_PLAY_SOUND);
            } else {
                audioManager.setSpeakerphoneOn(false);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setStreamVolume(mStreamType, mVolume, AudioManager.FLAG_PLAY_SOUND);
            }

            Log.e(TAG, "mVolume=" + mVolume + "maxVolumt=" + maxVolumt);
        } else {
            currentText = "扬声器";
            audioManager.setSpeakerphoneOn(true);
            //0或者3  没什么影响
            audioManager.setMode(AudioManager.MODE_NORMAL);
//            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
//            audioManager.setStreamVolume(mStreamType, mMusicVolume, AudioManager.FLAG_PLAY_SOUND);
            audioManager.setStreamVolume(mStreamType, mMusicVolume, AudioManager.FLAG_PLAY_SOUND);

            Log.e(TAG, "mMusicVolume=" + mMusicVolume + "maxMusicVolume=" + maxMusicVolume);
        }
    }


    public AudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager = (AudioManager) BContext.context().getSystemService(Context.AUDIO_SERVICE);
        }
        return audioManager;
    }

    public void volumeDown() {
        AudioManager audioManager = getAudioManager();
        mVolume--;
        if (mVolume < 0) {
            mVolume = 0;
        }

        maxMusicVolume = maxVolumt;
        mMusicVolume = mVolume;
        audioManager.setStreamVolume(mStreamType, mVolume, mStreamFlag);
        if (onVolumeListener != null) {
            onVolumeListener.onVolumeDown(mVolume, maxVolumt);
        }
        Log.e(TAG, "volumeDown mVolume=" + mVolume + "maxVolumt=" + maxVolumt);
        Log.e(TAG, "volumeDown mMusicVolume=" + mMusicVolume + "maxMusicVolume=" + maxMusicVolume);
    }

    public void volumeUp() {
        AudioManager audioManager = getAudioManager();
        mVolume++;
        if (mVolume < 0) {
            mVolume = 0;
        }

        if (mVolume > maxVolumt) {
            mVolume = (int) maxVolumt;
        }

        maxMusicVolume = maxVolumt;
        mMusicVolume = mVolume;
        audioManager.setStreamVolume(mStreamType, mVolume, mStreamFlag);
        if (onVolumeListener != null) {
            onVolumeListener.onVolumeUp(mVolume, maxVolumt);
        }
        Log.e(TAG, "volumeUp mVolume=" + mVolume + "maxVolumt=" + maxVolumt);
        Log.e(TAG, "volumeUp mMusicVolume=" + mMusicVolume + "maxMusicVolume=" + maxMusicVolume);
    }

    public void setVolumeListener(OnVolumeListener onVolumeListener) {
        this.onVolumeListener = onVolumeListener;
    }

    public void volumeBoard(int mVolume, double maxVolumt, int type) {
        this.mVolume = mVolume;
        this.maxVolumt = maxVolumt;
        this.maxMusicVolume = maxVolumt;
        this.mMusicVolume = mVolume;

        if (onVolumeListener != null) {
            onVolumeListener.onVolumeUp(mVolume, maxVolumt);
        }//
    }

    public interface OnVolumeListener {
        void onVolumeDown(int mVolume, double maxVolumt);

        void onVolumeUp(int mVolume, double maxVolumt);

        void onBroadVolume(int mVolume, double maxVolumt, int streamMaxVolume);
    }

    public void registerVolumeChangeListener() {
        if (onVolumeListener != null) {
            MySoundBroadCast.getIntance().registerVolumeChangeListener(onVolumeListener);
        }
    }

    public void unregisterVolumeChangeListener() {
        if (onVolumeListener != null) {
            MySoundBroadCast.getIntance().unregisterVolumeChangeListener();
        }
    }
}


