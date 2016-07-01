package com.nitesh.speechrecognition;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView mText;
    private SpeechRecognizer sr;
    private static final String TAG = "MainActivity";
    private Button startspeak,stopspeak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startspeak = (Button) findViewById(R.id.btn_start_speak);
        stopspeak = (Button)findViewById(R.id.btn_stop_speak);
        mText = (TextView) findViewById(R.id.textView1);
        startspeak.setOnClickListener(this);
        stopspeak.setOnClickListener(this);
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "onReadyForSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "beginingofSpeech");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                Log.d(TAG, "onRmsChanged");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.d(TAG, "onbufferRecieved");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "endSpeech");
            }

            @Override
            public void onError(int error) {
                switch (error){
                    case 1:
                        Log.e("error","NETWORK TIMEOUT");
                        break;
                    case 2:
                        Log.e("error","NETWORK ERROR");
                        break;
                    case 3:
                        Log.e("error","ERROR_AUDIO");
                        break;
                    case 4:
                        Log.e("error","SERVER ERROR");
                        break;
                    case 5:
                        Log.e("error","ERROR_CLIENT");
                        break;
                    case 6:
                        Log.e("error","SPEECH TIMEOUT");
                        break;
                    case 7:
                        Log.e("error","ERROR_NO_MATCH");
                        break;
                    case 8:
                        Log.e("error","ERROR_RECOGNIZER_BUSY");
                        break;
                    case 9:
                        Log.e("error","ERROR_INSUFFICIENT_PERMISSIONS");
                        break;
                    default:
                        Log.e("error","ERROR");
                        break;
                }
            }

            @Override
            public void onResults(Bundle results) {
                String result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                Log.d(TAG, "onReadyForSpeech"+result);
                mText.setText(result);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                Log.d(TAG, "onPartialResult");
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.d(TAG, "onEvent");
            }
        });
        }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start_speak)
        {
            startspeak.setVisibility(View.GONE);
            stopspeak.setVisibility(View.VISIBLE);
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplication().getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"say something");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,2);
            sr.startListening(intent);
            Log.i("111111","11111111");
        }
        if (v.getId()==R.id.btn_stop_speak){
            sr.stopListening();
            startspeak.setVisibility(View.VISIBLE);
            stopspeak.setVisibility(View.GONE);
        }

    }
}
