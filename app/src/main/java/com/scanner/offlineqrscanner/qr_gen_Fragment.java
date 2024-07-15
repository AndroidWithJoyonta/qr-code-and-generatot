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
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class qr_gen_Fragment extends Fragment {

    private EditText editText;
    private Button button ;

    FloatingActionButton downloadButton;
    private ImageView imageView;
    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_qr_gen_, container, false);


        editText = myview.findViewById(R.id.editText);
        button = myview.findViewById(R.id.button);
        downloadButton = myview.findViewById(R.id.downloadButton);
        imageView = myview.findViewById(R.id.imageView);

        button.setOnClickListener(view -> {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                generateQRCode(text);
                downloadButton.setVisibility(View.VISIBLE);

            }
        });

        downloadButton.setOnClickListener(view -> {

            imageViewImageDownload();

        });

        return myview;



    }
    private void generateQRCode(String text) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 800, 800);
            imageView.setImageBitmap(bitmap);
            downloadButton.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------

    //    image download method here
    public void imageViewImageDownload(){
        bitmapDrawable=(BitmapDrawable) imageView.getDrawable();
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