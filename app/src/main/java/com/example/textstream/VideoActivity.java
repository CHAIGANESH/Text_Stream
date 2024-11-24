package com.example.textstream;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {

    // Firebase Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<String> videoIds = new ArrayList<>(); // This will hold the video IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent i=getIntent();
        String sub=i.getStringExtra("subj");
        LinearLayout videoContainer = findViewById(R.id.video_container);

        // Retrieve video IDs from Firestore
        db.collection("Subjects") // Collection name in Firestore
                .document(sub) // Document name in Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Retrieve the document snapshot
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Retrieve the videos field from the Firestore document
                            List<String> videos = (List<String>) document.get("videos"); // Assuming 'videos' is the field name
                            if (videos != null) {
                                videoIds.addAll(videos);

                                // Now, iterate over the videoIds to add each video to the layout
                                for (String videoId : videoIds) {
                                    // Create a YouTube player view
                                    YouTubePlayerView youTubePlayerView = new YouTubePlayerView(this);
                                    videoContainer.addView(youTubePlayerView); // Add the player to the container
                                    getLifecycle().addObserver(youTubePlayerView); // Manage lifecycle

                                    // Set layout parameters with margin for space between videos
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    params.setMargins(0, 20, 0, 40); // Add 20dp margin on top and bottom for spacing
                                    youTubePlayerView.setLayoutParams(params);

                                    // Add a TextView to show the video ID below the video
                                    TextView videoIdTextView = new TextView(this);
                                    videoIdTextView.setText("Video ID: " + videoId);
                                    videoIdTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    ));
                                    videoIdTextView.setPadding(0, 10, 0, 20); // Add some padding to separate the text
                                    videoContainer.addView(videoIdTextView); // Add the TextView below the video

                                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                            youTubePlayer.cueVideo(videoId, 0); // Load the video
                                        }
                                    });
                                }
                            }
                        } else {
                            // Document does not exist
                            System.out.println("No such document");
                        }
                    } else {
                        // Handle failure
                        System.out.println("Error getting document: " + task.getException());
                    }
                });
    }
}