package com.example.textstream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class os extends AppCompatActivity {

    private EditText notesEditText;
    private Button saveNotesButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.os);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("NotesPref", MODE_PRIVATE);

        TextView sampleTextView = findViewById(R.id.objectives);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.myfont);
        sampleTextView.setTypeface(typeface);
        String cont = "Objectives\n1.Understand the fundamental concepts of operating systems, including process and thread management, scheduling mechanisms, and memory management strategies.\n2.Explore emerging trends and advancements in operating systems.";
        sampleTextView.setText(cont);

        View pdf = findViewById(R.id.blockLayout);
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(os.this, PdfActivity.class);
                i.putExtra("osbook", "Operating Systems.pdf");
                startActivity(i);
            }
        });

        // Initialize EditText and Button for notes
        notesEditText = findViewById(R.id.notesEditText);
        saveNotesButton = findViewById(R.id.saveNotesButton);

        // Load saved notes
        loadNotes();

        // Set onClickListener for the save button
        saveNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });
    }

    // Method to save notes to SharedPreferences
    private void saveNotes() {
        String notes = notesEditText.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userNotes", notes);
        editor.apply(); // Save the notes
    }

    // Method to load notes from SharedPreferences
    private void loadNotes() {
        String savedNotes = sharedPreferences.getString("userNotes", "");
        notesEditText.setText(savedNotes);
    }
}
