package com.scanner.offlineqrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

public class resultActivity extends AppCompatActivity {

    AppBarLayout appbar;
    MaterialToolbar toolbar;
    TextView resultTv;

    Button copyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        appbar=findViewById(R.id.appbar);
        toolbar=findViewById(R.id.toolbar);
        resultTv=findViewById(R.id.resultTv);
        copyBtn=findViewById(R.id.copyBtn);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToCopy = resultTv.getText().toString();

                if (!textToCopy.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(resultActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(resultActivity.this, "Please enter some text to copy", Toast.LENGTH_SHORT).show();
                }
            }
        });



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
                startActivity(new Intent(resultActivity.this,MainActivity.class));
            }
        });

        Intent intent = getIntent();
        String receivedText = intent.getStringExtra("EXTRA_TEXT");

        resultTv.setText(receivedText);
    }
}