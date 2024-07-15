package com.scanner.offlineqrscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

public class qrScanActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Button scanButton, galleryButton;

    TextView textView;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        imageView = findViewById(R.id.imageView);
        scanButton = findViewById(R.id.scanButton);
        galleryButton = findViewById(R.id.galleryButton);
        textView = findViewById(R.id.textView);

        scanQRCode();



        scanButton.setOnClickListener(v -> {

            scanQRCode();
//            BarCodeScan();
        });


//        galleryButton.setOnClickListener(v ->
//                openGallery()
//        );



    }

    private void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR Code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.getCaptureActivity();
        integrator.initiateScan();


//        ScanOptions options = new ScanOptions();
//        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
//        options.setPrompt("Scan a barcode");
//        options.setCameraId(0);  // Use a specific camera of the device
//        options.setBeepEnabled(false);
//        options.setBarcodeImageEnabled(true);
//        ;

//        new IntentIntegrator(qrScanActivity.this)
//                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
//                .setPrompt("Scan a barcode")
//                .setCameraId(0)
//                .setBeepEnabled(true)
//                .setBarcodeImageEnabled(true)
//                .initiateScan();
    }



    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                String qrCode = decodeQRCode(bitmap);
                if (qrCode != null) {
//                    textView.setText("Qr code: "  +qrCode);
                    Intent intent = new Intent(qrScanActivity.this,resultActivity.class);
                    intent.putExtra("EXTRA_TEXT",qrCode);
                    startActivity(intent);
//                    Toast.makeText(this, "QR Code: " + qrCode, Toast.LENGTH_LONG).show();

                } else {
//                    textView.setText(" No QR Code found ");
                    Intent intent = new Intent(qrScanActivity.this,resultActivity.class);
                    intent.putExtra("EXTRA_TEXT",qrCode);
                    startActivity(intent);
                    Toast.makeText(this, "No QR Code found", Toast.LENGTH_LONG).show();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
//                    textView.setText("Scanned: "  +result.getContents());
                    Intent intent = new Intent(qrScanActivity.this,resultActivity.class);
                    intent.putExtra("EXTRA_TEXT",result.getContents());
                    startActivity(intent);
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(qrScanActivity.this,resultActivity.class);
                    intent.putExtra("EXTRA_TEXT",result.getContents());
                    startActivity(intent);
                    onBackPressed();
//                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private String decodeQRCode(Bitmap bitmap) {
        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new com.google.zxing.qrcode.QRCodeReader().decode(binaryBitmap);
            return result.getText();
        } catch (NotFoundException | ChecksumException | FormatException e) {
            e.printStackTrace();
        }
        return null;
    }





}