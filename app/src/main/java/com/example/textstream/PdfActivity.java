package com.example.textstream;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
public class PdfActivity extends AppCompatActivity {
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;
    private ImageView pdfImageView;
    private int currentPageIndex = 0;
    private EditText pageNumberInput;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfImageView = findViewById(R.id.pdfImageView);
        Button nextPageBtn = findViewById(R.id.nextPageBtn);
        Button prevPageBtn = findViewById(R.id.prevPageBtn);
        pageNumberInput = findViewById(R.id.pageNumberInput);
        searchButton = findViewById(R.id.searchButton);
        Intent intent = getIntent();
        String bookname = intent.getStringExtra("book");
        try {
            openRenderer(bookname);  // Load PDF from assets
            showPage(currentPageIndex);  // Show the first page
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchButton.setOnClickListener(v -> {
            String pageNumberStr = pageNumberInput.getText().toString();
            if (!pageNumberStr.isEmpty()) {
                int pageNumber = Integer.parseInt(pageNumberStr) - 1; // Convert to zero-based index
                if (pageNumber >= 0 && pageNumber < pdfRenderer.getPageCount()) {
                    try {
                        currentPageIndex=pageNumber;
                        showPage(pageNumber);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle invalid page number
                    Toast.makeText(this, "Invalid page number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Next page button listener
        nextPageBtn.setOnClickListener(v -> {
            if (currentPageIndex < pdfRenderer.getPageCount() - 1) {
                currentPageIndex++;
                try {
                    showPage(currentPageIndex);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        prevPageBtn.setOnClickListener(v -> {
            if (currentPageIndex > 0) {
                currentPageIndex--;
                try {
                    showPage(currentPageIndex);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start rendering the PDF in onStart or onResume
        try {
            showPage(0);  // Display the first page
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Release the renderer resources when the activity is no longer visible
        closeRenderer();
    }

    private void openRenderer(String fileName) throws IOException {
        File file = new File(getCacheDir(), fileName);
        if (!file.exists()) {
            // Copy the file from assets if not already cached
            try (InputStream asset = getAssets().open(fileName);
                 FileOutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = asset.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(parcelFileDescriptor);
    }

    private void showPage(int index) throws IOException {
        if (pdfRenderer.getPageCount() <= index) return;

        // Close the current page before opening another
        if (currentPage != null) {
            currentPage.close();
        }

        // Open a specific page
        currentPage = pdfRenderer.openPage(index);

        // Create a bitmap with the same size as the PDF page
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Ensure the entire page is rendered onto the bitmap
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);  // Set a white background to avoid transparency issues
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Apply brightness adjustment using ColorMatrix
        Bitmap brightBitmap = adjustBrightness(bitmap, 1.2f); // Adjust brightness factor

        // Show the rendered page in the ImageView
        pdfImageView.setImageBitmap(brightBitmap);
    }

    private Bitmap adjustBrightness(Bitmap bitmap, float brightnessFactor) {
        Bitmap brightBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        Canvas canvas = new Canvas(brightBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        // Set the brightness (increase value for higher brightness)
        colorMatrix.set(new float[] {
                brightnessFactor, 0, 0, 0, 0,
                0, brightnessFactor, 0, 0, 0,
                0, 0, brightnessFactor, 0, 0,
                0, 0, 0, 1, 0
        });

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return brightBitmap;
    }

    private void closeRenderer() {
        if (currentPage != null) {
            currentPage.close();
        }
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}