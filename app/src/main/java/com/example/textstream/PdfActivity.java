package com.example.textstream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

public class PdfActivity extends AppCompatActivity {

    private PDFView pdfView;
    private EditText pageNumberInput;
    private Button searchButton;
    private ImageButton fabSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfView = findViewById(R.id.pdfView);
        pageNumberInput = findViewById(R.id.pageNumberInput);
        searchButton = findViewById(R.id.searchButton);
        fabSearch = findViewById(R.id.fabSearch);

        // Initially hide the page number input and search button
        pageNumberInput.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);

        // Get the book name passed from the previous activity
        Intent intent = getIntent();
        String bookname = intent.getStringExtra("book");

        // Load the PDF from assets
        pdfView.fromAsset(bookname)
                .enableSwipe(true) // Enable vertical or horizontal swipe
                .swipeHorizontal(false) // Enable vertical scrolling
                .enableDoubletap(true) // Allow double-tap to zoom
                .enableAnnotationRendering(true) // Render annotations (e.g., comments, highlights)
                .defaultPage(0) // Open at the first page
                .load();

        // Toggle visibility of input and button when FAB is clicked
        fabSearch.setOnClickListener(v -> {
            if (pageNumberInput.getVisibility() == View.GONE) {
                pageNumberInput.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
            } else {
                pageNumberInput.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
            }
        });

        // Search for a specific page
        searchButton.setOnClickListener(v -> {
            String pageNumberStr = pageNumberInput.getText().toString();
            if (!pageNumberStr.isEmpty()) {
                int pageNumber = Integer.parseInt(pageNumberStr) - 1; // Convert to zero-based index
                if (pageNumber >= 0 && pageNumber < pdfView.getPageCount()) {
                    pdfView.jumpTo(pageNumber, true); // Jump to the specified page
                } else {
                    Toast.makeText(this, "Invalid page number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
