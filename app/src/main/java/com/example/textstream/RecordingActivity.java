package com.example.textstream;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordingActivity extends AppCompatActivity {
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private Button startRecordingButton, stopRecordingButton, playRecordingButton;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private EditText recordingNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
        startRecordingButton = findViewById(R.id.startRecordingButton);
        stopRecordingButton = findViewById(R.id.stopRecordingButton);
        playRecordingButton = findViewById(R.id.playRecordingButton);

        audioFilePath = getExternalFilesDir(null).getAbsolutePath() + "/voicenote.3gp";
        recordingNameEditText = findViewById(R.id.recordingNameEditText);

        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
                startRecordingButton.setEnabled(false);
                stopRecordingButton.setEnabled(true);
            }
        });

        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
                stopRecordingButton.setEnabled(false);
                playRecordingButton.setEnabled(true);
            }
        });

        playRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecording();
            }
        });
        loadRecordings();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, you can now start recording
            } else {
                // Permission was denied, show a message and disable recording functionality
                Toast.makeText(this, "Permission to record audio is required!", Toast.LENGTH_SHORT).show();
                startRecordingButton.setEnabled(false);
            }
        }
    }


    private void startRecording() {
        // Ensure permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            String recordingName = recordingNameEditText.getText().toString().trim();
            recordingName = recordingName.replaceAll("[^a-zA-Z0-9]", "_");
            if (recordingName.isEmpty()) {
                Toast.makeText(this, "Please enter a name for the recording", Toast.LENGTH_SHORT).show();
                return; // Exit if no name is provided
            }
            audioFilePath = getExternalFilesDir(null).getAbsolutePath() + "/" + recordingName +".3gp";

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFilePath);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Toast.makeText(this, "Recording saved", Toast.LENGTH_SHORT).show();
    }

    private void playRecording() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing recording", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadRecordings() {
        File directory = getExternalFilesDir(null);

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".3gp"));
        List<File> recordings = new ArrayList<>(Arrays.asList(files));

        RecyclerView recordingListView = findViewById(R.id.recordingListView);
        recordingListView.setLayoutManager(new LinearLayoutManager(this));
        recordingListView.setAdapter(new RecordingAdapter(recordings, this));
    }
}
