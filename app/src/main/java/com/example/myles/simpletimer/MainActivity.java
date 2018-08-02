package com.example.myles.simpletimer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    SeekBar timeSet;
    TextView countdownTimer;
    TextView textView;
    Boolean activeCounter = false;
    Button stopStartButton;
    CountDownTimer countTimer;
    ImageView backgroundImage;
    long minutes;
    long seconds;
    long timeRemain;


    public void rotateAnimation () {
        if (activeCounter) {
            RotateAnimation rotateAnimation = new RotateAnimation(5f, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setDuration(2000);
            rotateAnimation.setRepeatCount(Animation.INFINITE);

            findViewById(R.id.backgroundImage).startAnimation(rotateAnimation);
        }
    }

    public void resetEnabler (View v) {
        resetTimer();
    }

    public void resetTimer(){
        countdownTimer.setText("00:00");
        textView.setText("");
        timeSet.setProgress(0);
        countTimer.cancel();
        stopStartButton.setText("Start");
        timeSet.setEnabled(true);
        activeCounter = false;
        backgroundImage.clearAnimation();
    }

    public void updateTimer (int secondsLeft) {
        minutes = secondsLeft / 60;
        seconds = secondsLeft - minutes * 60;

        countdownTimer.setText(String.format("%02d:%02d", minutes, seconds));
        // countdownTimer.setText(String.format(Integer.toString(minutes), "%02d")+ ":" + Integer.toString(seconds));


    }
    public void timerEnabler (View v) {
        if (!activeCounter) {
            activeCounter = true;
            textView.setText("");
            timeSet.setEnabled(false);
            stopStartButton.setText("Pause");
            rotateAnimation();
            countTimer = new CountDownTimer(timeSet.getProgress() * 1000 + 100, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                   updateTimer((int) millisUntilFinished / 1000);
                   timeRemain = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    resetTimer();
                    countdownTimer.setText("");
                    textView.setText("Done.");
                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);
                    mplayer.start();
                    backgroundImage.clearAnimation();
                }
            }.start();
        } else if (stopStartButton.getText().equals("Pause")) {
                countTimer.cancel();
                backgroundImage.clearAnimation();
                stopStartButton.setText("Resume");
        } else {
            countTimer = new MyCount(timeRemain, 1000);
            countTimer.start();
            rotateAnimation();
            stopStartButton.setText("Pause");
        }
    }

    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            resetTimer();
            countdownTimer.setText("");
            textView.setText("Done.");
            MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);
            mplayer.start();
            backgroundImage.clearAnimation();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            updateTimer((int) millisUntilFinished / 1000);
            timeRemain = millisUntilFinished;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timeSet = findViewById(R.id.timeSetSeekbar);
        countdownTimer = findViewById(R.id.countdownTimerTextView);
        textView = findViewById(R.id.textView);
        stopStartButton = findViewById(R.id.stopStartButton);
        backgroundImage = findViewById(R.id.backgroundImage);

        timeSet.setMax(9000);
        timeSet.setProgress(0);


        timeSet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
