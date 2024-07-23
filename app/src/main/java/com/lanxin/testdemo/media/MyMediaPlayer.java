package com.lanxin.testdemo.media;

import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.lanxin.testdemo.app.BContext;

import java.io.IOException;

/**
 * @author:张小兵
 * @e-mail:460116602@qq.com
 * @date:2024/01/19
 */
public class MyMediaPlayer {
    private static volatile MyMediaPlayer instance = null;
    private MediaPlayer mp = null;
    private boolean isPlayer = false;


    public static MyMediaPlayer getInstance() {
        if (instance == null) {
            synchronized (MyMediaPlayer.class) {
                if (instance == null) {
                    instance = new MyMediaPlayer();
                }
            }
        }
        return instance;
    }

    public void playerMusic() {
        isPlayer = true;
        MediaPlayer mediaPlayer = getMediaPlayer();
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void stopMusic() {
        isPlayer = false;
        MediaPlayer mediaPlayer = getMediaPlayer();
        mediaPlayer.stop();
        mediaRelease();
    }

    private MediaPlayer getMediaPlayer() {
        if (mp == null) {
            try {
                mp = new MediaPlayer();
                AssetFileDescriptor afd = BContext.context().getAssets().openFd("phonering.mp3");
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            mp = MediaPlayer.create(BContext.context(), R.raw.phonering);
//            AudioAttributes audioAttributes = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                audioAttributes = new AudioAttributes.Builder()
//                        .setLegacyStreamType(AudioManager.STREAM_VOICE_CALL)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .setAllowedCapturePolicy(AudioAttributes.ALLOW_CAPTURE_BY_ALL)
//                        .build();
//            }

            mp.setLooping(true);
            mp.setAudioAttributes(new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_VOICE_CALL)
                    .build());
        }
        return mp;
    }

    private void mediaRelease() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    public boolean isPlaying() {
        return isPlayer;
    }

    public void setVolume(float mVolume) {
        MediaPlayer mediaPlayer = getMediaPlayer();
        mediaPlayer.setVolume(mVolume, mVolume);
    }
}
