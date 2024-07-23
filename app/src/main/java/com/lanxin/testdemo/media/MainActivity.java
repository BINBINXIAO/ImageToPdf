package com.lanxin.testdemo.media;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lanxin.testdemo.R;
import com.lanxin.testdemo.media.MyAudioManager;
import com.lanxin.testdemo.media.MyMediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyAudioManager.OnVolumeListener {

    private TextView player, tingtong, yangshengqi, yinliang, stop;
    private static final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = findViewById(R.id.player);
        stop = findViewById(R.id.stop);
        tingtong = findViewById(R.id.tingtong);
        yangshengqi = findViewById(R.id.yangshengqi);
        yinliang = findViewById(R.id.yinliang);

        player.setOnClickListener(this);
        stop.setOnClickListener(this);
        tingtong.setOnClickListener(this);
        yangshengqi.setOnClickListener(this);

        MyAudioManager.getInstance().setVolumeListener(this);
        MyAudioManager.getInstance().registerVolumeChangeListener();
        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                writeFile();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 110);
            }
        } else {
            writeFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110) {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                writeFile();
            } else {
                Toast.makeText(this, "2222", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void writeFile() {
        Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == player) {
            MyMediaPlayer.getInstance().playerMusic();
            MyAudioManager.getInstance().switchSpeakerOnModle(false);
            return;
        }
        if (v == stop) {
            if (MyMediaPlayer.getInstance().isPlaying()) {
                MyMediaPlayer.getInstance().stopMusic();
            }
            return;
        }
        if (v == tingtong) {
            if (MyMediaPlayer.getInstance().isPlaying()) {
                MyAudioManager.getInstance().switchSpeakerOnModle(false);
            }
            return;
        }
        if (v == yangshengqi) {
            if (MyMediaPlayer.getInstance().isPlaying()) {
                MyAudioManager.getInstance().switchSpeakerOnModle(true);
            }
            return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            MyAudioManager.getInstance().volumeDown();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            MyAudioManager.getInstance().volumeUp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onVolumeDown(int mVolume, double maxVolumt) {
        float mediaVolume = (float) (mVolume / maxVolumt);
        MyMediaPlayer.getInstance().setVolume(mediaVolume);
        yinliang.setText("当前音量：" + mVolume + "声道音量：" + mediaVolume + "最大音量：" + maxVolumt);
    }

    @Override
    public void onVolumeUp(int mVolume, double maxVolumt) {
        float mediaVolume = (float) (mVolume / maxVolumt);
        MyMediaPlayer.getInstance().setVolume(mediaVolume);
        yinliang.setText("当前音量：" + mVolume + "声道音量：" + mediaVolume + "最大音量：" + maxVolumt);
    }

    @Override
    public void onBroadVolume(int mVolume, double maxVolumt, int streamMaxVolume) {
        MyAudioManager.getInstance().volumeBoard(mVolume, maxVolumt, streamMaxVolume);
//        float mediaVolume = (float) (mVolume / maxVolumt);
//        MyMediaPlayer.getInstance().setVolume(mediaVolume);
//        yinliang.setText("当前音量：" + mVolume + "声道音量：" + mediaVolume + "最大音量：" + maxVolumt);


//        if (streamMaxVolume == AudioManager.STREAM_VOICE_CALL) {
//            MyAudioManager.getInstance().volumeBoard(mVolume, maxVolumt, streamMaxVolume);
//            float mediaVolume = (float) (mVolume / maxVolumt);
//            MyMediaPlayer.getInstance().setVolume(mediaVolume);
//            yinliang.setText("当前音量：" + mVolume + "声道音量：" + mediaVolume + "最大音量：" + maxVolumt);
//        }
    }


    public static void postOnMainThreadDelayed(Runnable runnable, long delayMillis) {
        if (null != runnable) {
            handler.postDelayed(runnable, delayMillis);
        }
    }
}