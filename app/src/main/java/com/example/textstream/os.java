package com.example.textstream;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class os extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.os);

        TextView sampleTextView = findViewById(R.id.objectives);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.myfont);
        sampleTextView.setTypeface(typeface);
        String cont="Objectives\n1.Understand the fundamental concepts of operating systems, including process and thread management, scheduling mechanisms, and memory management strategies.\n2.Explore emerging trends and advancements in operating systems.";
        sampleTextView.setText(cont);

        View pdf=findViewById(R.id.blockLayout);
        pdf.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(os.this,PdfActivity.class);
                i.putExtra("osbook", "Operating Systems.pdf");
                startActivity(i);
            }

        });



    }
}