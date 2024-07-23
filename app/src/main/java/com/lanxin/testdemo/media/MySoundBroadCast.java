package com.lanxin.testdemo.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

import com.lanxin.testdemo.app.BContext;

/**
 * @author:张小兵
 * @e-mail:460116602@qq.com
 * @date:2024/01/20
 */
public class MySoundBroadCast {

    private static volatile MySoundBroadCast intance = null;
    private static String VOLUME_CHANGE_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    //    private static String VOLUME_CHANGE_ACTION = "android.media.EXTRA_VOLUME_STREAM_VALUE";
    private static String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    private VolumeBroadCastReceiver mVolumeBroadCastReceiver;
    private MyAudioManager.OnVolumeListener volumeChangeListener;
    private long lastTime;

    public static MySoundBroadCast getIntance() {
        if (intance == null) {
            intance = new MySoundBroadCast();
        }
        return intance;
    }

    public void registerVolumeChangeListener(MyAudioManager.OnVolumeListener onVolumeListener) {
        this.volumeChangeListener = onVolumeListener;
        mVolumeBroadCastReceiver = new VolumeBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
        intentFilter.addAction("android.media.RINGER_MODE_CHANGED");
        intentFilter.addAction("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION");
        intentFilter.addAction("android.media.EXTRA_RINGER_MODE");
        intentFilter.addAction("android.media.VIBRATE_SETTING_CHANGED");
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        intentFilter.addAction("android.media.STREAM_DEVICES_CHANGED_ACTION");
        intentFilter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
        intentFilter.addAction("android.media.EXTRA_VIBRATE_SETTING");
        intentFilter.addAction("android.media.EXTRA_VIBRATE_TYPE");
        intentFilter.addAction("android.media.EXTRA_VOLUME_STREAM_TYPE");
        intentFilter.addAction("android.media.EXTRA_VOLUME_STREAM_TYPE_ALIAS");
        intentFilter.addAction("android.media.EXTRA_VOLUME_STREAM_VALUE");
        intentFilter.addAction(VOLUME_CHANGE_ACTION);
        BContext.context().registerReceiver(mVolumeBroadCastReceiver, intentFilter);
    }

    public void unregisterVolumeChangeListener() {
        if (mVolumeBroadCastReceiver != null) {
            BContext.context().unregisterReceiver(mVolumeBroadCastReceiver);
        }
    }

    private class VolumeBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() == VOLUME_CHANGE_ACTION && intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_VOICE_CALL) {
                lastTime = System.currentTimeMillis();
                AudioManager audioManager = MyAudioManager.getInstance().getAudioManager();
                int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                double streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
                if (volumeChangeListener != null) {
                    volumeChangeListener.onBroadVolume(streamVolume, streamMaxVolume, AudioManager.STREAM_VOICE_CALL);
                }
                Log.e("zxb-test-boradt", "current=" + streamVolume);
            }
        }
    }

    public interface VolumeChangeListener {
        void onVolumeDownToMin();

        void onVolumeUp();
    }
}
