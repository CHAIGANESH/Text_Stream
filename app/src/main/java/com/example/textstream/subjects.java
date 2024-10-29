package com.example.textstream;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.Calendar;

public class subjects extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subjects);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String objectives = intent.getStringExtra("objectives");
        String notes = intent.getStringExtra("notes");
        String book=intent.getStringExtra("book");
        SharedPreferences sharedPreferences = getSharedPreferences("Notes", MODE_PRIVATE);
        TextView tit=findViewById(R.id.titleTextView);
        tit.setText(title);

        TextView sampleTextView = findViewById(R.id.objectives);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.myfont);
        sampleTextView.setTypeface(typeface);
        sampleTextView.setText(objectives);

        View pdf = findViewById(R.id.blockLayout);
        TextView pdfbutton=findViewById(R.id.osclick);
        pdfbutton.setText(title);
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(subjects.this, PdfActivity.class);
                i.putExtra("book", book);
                startActivity(i);
            }
        });
        EditText note=findViewById(R.id.notesEditText);
        String noteContent = sharedPreferences.getString(notes, "");
        note.setText(noteContent);
        Button saveNotesButton = findViewById(R.id.saveNotesButton);
        saveNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newValue = note.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(notes, newValue);
                editor.apply();
            }
        });




    }
}