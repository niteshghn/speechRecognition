/*
 * The application needs to have the permission to write to external storage
 * if the output file is written to the external storage, and also the
 * permission to record audio. These permissions must be set in the
 * application's AndroidManifest.xml file, with something like:
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *
 */
package com.nitesh.speechrecognition;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class RecordAudio extends AppCompatActivity implements OnClickListener {
    private ImageButton imageButton;
    private Boolean startrecording = true;
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    private static String mFileName = null;
    private static final String LOG_TAG = "AudioRecordTest";
    private int num = 0;
    private int aud_num = 4;
    private int user_no = 1123;
    private Button startAssBut;
    private Button nextbtn, prevbtn, newuserbtn;
    private TextView quesTv;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_aud);
        init();
        makeDirectory();
    }

    private void makeDirectory() {
        Log.e("src",Environment.getExternalStorageDirectory().getAbsolutePath()+"/appRecording/");
        File recordingDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/appRecording/");
        if (recordingDir.exists()){
            Toast.makeText(RecordAudio.this, "Ready to record", Toast.LENGTH_SHORT).show();
        }else {
            if (!recordingDir.mkdir()) {
                Toast.makeText(RecordAudio.this, "make folder recRecording", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        imageButton = (ImageButton) findViewById(R.id.imgbtn);
        assert imageButton != null;
        imageButton.setOnClickListener(this);
        startAssBut = (Button) findViewById(R.id.startassBut);
        nextbtn = (Button) findViewById(R.id.nextbtn);
        prevbtn = (Button) findViewById(R.id.lastBtn);
        quesTv = (TextView) findViewById(R.id.questonn_no);
        newuserbtn = (Button) findViewById(R.id.newuserbtn);
        assert startAssBut != null;
        startAssBut.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        prevbtn.setOnClickListener(this);
        newuserbtn.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_no = sharedPreferences.getInt("user_no", user_no);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn:
                startOrStopRecording();
                break;
            case R.id.startassBut:
                startAssessmentAudio();
                break;
            case R.id.newuserbtn:
                createNewUser();
                break;
            case R.id.lastBtn:
                showLastQuestion();
                break;
            case R.id.nextbtn:
                showNextQuestion();
                break;
        }
    }

    private void createNewUser() {
        quesTv.setText("Question No:=1");
        aud_num = 4;
        sharedPreferences.edit().putInt("user_no", ++user_no).commit();
        Toast.makeText(this, "New User Created", Toast.LENGTH_LONG).show();
    }

    private void showLastQuestion() {
        setScreen();
        if (aud_num > 4) {
            aud_num -= 1;
        }
        num = aud_num - 3;
        quesTv.setText("Question No :=" + num);
    }

    private void showNextQuestion() {
        setScreen();
        if (aud_num < 9) {
            aud_num += 1;
        }
        num = aud_num - 3;
        quesTv.setText("Question No :=" + num);
    }

    private void setScreen() {
        stopPlaying();
        stopRecording();
        startAssBut.setVisibility(View.VISIBLE);
        startrecording = true;
    }

    private void startAssessmentAudio() {
        try {
            mPlayer = MediaPlayer.create(this, getAudioFile(aud_num));
            Log.e("recoding_num", aud_num + "");
            Log.e("clicked", "startAssessmentAudio");
            startAssBut.setVisibility(View.GONE);
            mPlayer.start();
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
            stopPlaying();
        }
    }

    private int getAudioFile(int aud_num) {
        switch (aud_num) {
            case 1:
                return R.raw.rec1;
            case 2:
                return R.raw.rec2;
            case 3:
                return R.raw.rec3;
            case 4:
                return R.raw.rec4;
            case 5:
                return R.raw.rec5;
            case 6:
                return R.raw.rec6;
            case 7:
                return R.raw.rec7;
            case 8:
                return R.raw.rec8;
            case 9:
                return R.raw.rec9;
            default:
                return R.raw.rec1;
        }
    }

    private void startOrStopRecording() {
        Log.e("clicked", "stopAssessmentAudio");
        user_no = sharedPreferences.getInt("user_no", user_no);
        if (startrecording) { //start recording
            Log.e("rec_no", user_no + "_" + num);
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.stop));
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/appRecording/" + user_no + "_" + num + ".3gp";
            startRecording();
            startrecording = false;
        } else {//stop recording
            stopRecording();
            startrecording = true;
            stopPlaying();
        }
    }


    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncodingBitRate(64);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        num++;
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    //
    private void stopRecording() {
        try {
            startAssBut.setVisibility(View.VISIBLE);
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.circled_play));
            mRecorder.stop();
            mRecorder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
//    class RecordButton extends Button {
//        boolean mStartRecording = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                onRecord(mStartRecording);
//                if (mStartRecording) {
//                    setText("Stop recording");
//                } else {
//                    setText("Start recording");
//                }
//                mStartRecording = !mStartRecording;
//            }
//        };
//
//        public RecordButton(Context ctx) {
//            super(ctx);
//            setText("Start recording");
//            setOnClickListener(clicker);
//        }
//    }
//
//    class PlayButton extends Button {
//        boolean mStartPlaying = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                onPlay(mStartPlaying);
//                if (mStartPlaying) {
//                    setText("Stop playing");
//                } else {
//                    setText("Start playing");
//                }
//                mStartPlaying = !mStartPlaying;
//            }
//        };
//
//        public PlayButton(Context ctx) {
//            super(ctx);
//            setText("Start playing");
//            setOnClickListener(clicker);
//        }
//    }
//
//    public RecordAudio() {
//        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        mFileName += "/audiorecordtest.3gp";
//    }
//
//    @Override
//    public void onCreate(Bundle icicle) {
//        super.onCreate(icicle);
//
//        LinearLayout ll = new LinearLayout(this);
//        mRecordButton = new RecordButton(this);
//        ll.addView(mRecordButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        mPlayButton = new PlayButton(this);
//        ll.addView(mPlayButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        setContentView(ll);
//    }
//
    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayer = new MediaPlayer();
        mPlayer.setLooping(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sharedPreferences.edit().putInt("user_no", ++user_no);
    }
}