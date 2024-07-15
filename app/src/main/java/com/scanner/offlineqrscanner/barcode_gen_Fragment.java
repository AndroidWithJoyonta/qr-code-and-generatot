package com.scanner.offlineqrscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class barcode_gen_Fragment extends Fragment {
    EditText etInput;
    Button btnGenerate;
     ImageView ivBarcode;

    FloatingActionButton downloadButton;

    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View myView = inflater.inflate(R.layout.fragment_barcode_gen_, container, false);


        etInput = myView.findViewById(R.id.etInput);
        btnGenerate = myView.findViewById(R.id.btnGenerate);
        ivBarcode = myView.findViewById(R.id.ivBarcode);
        downloadButton = myView.findViewById(R.id.downloadButton);

        btnGenerate.setOnClickListener(v -> {
            String text = etInput.getText().toString().trim();
            if (!text.isEmpty()) {
                generateBarcode(text);
            }
        });

        downloadButton.setOnClickListener(view -> {

            imageViewImageDownload();

        });
        return myView;
    }
    private void generateBarcode(String text) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.CODE_128, 800, 800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            ivBarcode.setImageBitmap(bitmap);
            downloadButton.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    //----------------------------------------------------------------

    //    image download method here
    public void imageViewImageDownload(){
        bitmapDrawable=(BitmapDrawable) ivBarcode.getDrawable();
        bitmap=bitmapDrawable.getBitmap();

        FileOutputStream fileOutputStream=null;

        File sdCard = Environment.getExternalStorageDirectory();
        File Directory=new File(sdCard.getAbsolutePath()+ "/Download");
        Directory.mkdir();

        String filename=String.format("%d.jpg",System.currentTimeMillis());
        File outfile=new File(Directory,filename);

        Toast.makeText(getContext(),"Image Saved Successfully",Toast.LENGTH_SHORT).show();



        try {
            fileOutputStream=new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outfile));
            getContext().sendBroadcast(intent);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}