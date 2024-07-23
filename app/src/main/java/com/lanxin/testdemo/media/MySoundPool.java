package com.lanxin.testdemo.media;

import android.media.AudioAttributes;
import android.media.SoundPool;

import com.lanxin.testdemo.R;
import com.lanxin.testdemo.app.BContext;

/**
 * @author:张小兵
 * @e-mail:460116602@qq.com
 * @date:2024/01/20
 */
public class MySoundPool {
    private static volatile MySoundPool instance = null;
    private SoundPool soundPool;

    public static MySoundPool getInstance() {
        if (instance == null) {
            instance = new MySoundPool();
        }
        return instance;
    }

    public void start() {
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(15);
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION);
        spb.setAudioAttributes(attrBuilder.build());

        soundPool = spb.build();
        int soundId = soundPool.load(BContext.context(), R.raw.phonering, 1);
        soundPool.play(soundId, 1, 1, 5, 30, 1f);
    }

    public void setVolume() {
//        soundPool.setVolume();
    }
}
