package com.example.voiceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecogniser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton mic = findViewById(R.id.imageButton);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecogniser.startListening(intent);
            }
        });
        initializeTextToSpeech();
        initializeSpeechRecogniser();
    }

    private void initializeSpeechRecogniser() {
        if(SpeechRecognizer.isRecognitionAvailable(this))
        {
            mySpeechRecogniser = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecogniser.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> r = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResults(r.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResults(String s) {
        s = s.toLowerCase();
        if(s.indexOf("what")!=-1){
            if(s.indexOf("your name")!=-1)
            {
                speak("hi, I'm Peter Pan.");
            }
            if(s.indexOf("time")!=-1){
                Date now = new Date();
                String time = DateUtils.formatDateTime(this,now.getTime(),DateUtils.FORMAT_SHOW_TIME);
                speak("the time is"+time);
            }
        }
        else if(s.indexOf("open")!=-1){
            if(s.indexOf("browser")!=-1){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                startActivity(intent);
            }
        }
    }

    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0)
                {
                    Toast.makeText(MainActivity.this,"Sorry, No TextToSpeech Engine exists on this device.",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    myTTS.setLanguage(Locale.ENGLISH);
                    speak("Hello! I am ready.");
                }
            }
        });
    }

    private void speak(String s) {
        if(Build.VERSION.SDK_INT>=21)
        {
            myTTS.speak(s,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            myTTS.speak(s,TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        myTTS.shutdown();
    }

}
