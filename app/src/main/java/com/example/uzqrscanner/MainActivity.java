package com.example.uzqrscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    Button btnScan, btnOpen;
    TextView txtResult;
    String scannedContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        btnOpen = findViewById(R.id.btnOpen);
        txtResult = findViewById(R.id.txtResult);

        btnScan.setOnClickListener(v -> scanCode());

        btnOpen.setOnClickListener(v -> {
            if (scannedContent.startsWith("http://") || scannedContent.startsWith("https://")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scannedContent));
                startActivity(browserIntent);
            }
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a QR code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class); // custom activity for camera

        barcodeLauncher.launch(options);
    }

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if(result.getContents() != null) {
                    scannedContent = result.getContents();
                    txtResult.setText(scannedContent);

                    if (scannedContent.startsWith("http://") || scannedContent.startsWith("https://")) {
                        btnOpen.setVisibility(View.VISIBLE);
                    } else {
                        btnOpen.setVisibility(View.GONE);
                    }
                }
            });
}
