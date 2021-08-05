package com.ritikgarg1057.camera2;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        im=findViewById(R.id.imageView);

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                ImageOpen();

            }
        });

    }

    private void ImageOpen()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflator=getLayoutInflater();
        View dialogView=inflator.inflate(R.layout.image_gallery,null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        ImageView im2=dialogView.findViewById(R.id.imageView2);
        ImageView im3=dialogView.findViewById(R.id.imageView3);

        final AlertDialog dialogimage=builder.create();
        dialogimage.show();

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermission())
                {takeFromCamera();
                dialogimage.cancel();}

            }
        });

        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               takeFromGalley();
               dialogimage.cancel();
            }

        });
    }
    private void takeFromGalley()
    {
        Intent picImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(picImage,1);
    }

    private void takeFromCamera()
    {
     Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     if(captureImage.resolveActivity(getPackageManager())!=null)
     {
         startActivityForResult(captureImage,2);
     }
             
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 1:
                if(resultCode==RESULT_OK)
                {
                 Uri selectedImageUri = data.getData();
                 im.setImageURI(selectedImageUri);

                    Toast.makeText(this, "Thanks for using this app", Toast.LENGTH_SHORT).show();

                }
                break;
            case 2 :
                if(resultCode==RESULT_OK)
                {
                  Bundle bundle= data.getExtras();
                    Bitmap bitmapImage= (Bitmap) bundle.get("data");
                    im.setImageBitmap(bitmapImage);
                    Toast.makeText(this, "Thanks for using this app", Toast.LENGTH_SHORT).show();


                }
        }
    }

    private boolean checkAndRequestPermission()
    {
        if(Build.VERSION.SDK_INT>=23)
        {
         int CameraPermission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
         if(CameraPermission== PackageManager.PERMISSION_DENIED)
         {
             ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.CAMERA},20);
             return false;
         }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==20 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            takeFromCamera();
        }
        else
        {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }
}