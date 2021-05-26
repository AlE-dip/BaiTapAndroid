package com.example.selfie_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int REQUEST_CAMERA_CODE = 111;
    public static final int REQUEST_FILE_CODE = 112;
    ImageView selectedImage, imCamera, imGallery, imSave, imClose;
    ConstraintLayout cnShowImg;
    GridView grImage;
    Bitmap bitmap;
    ArrayList<String> paths;
    ImageAdapter imageAdapter;
    int indexFile = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getImageFromSdcard();

        selectedImage = findViewById(R.id.displayImageView);
        cnShowImg = findViewById(R.id.cnShowImage);
        grImage = findViewById(R.id.grImgage);
        imCamera = findViewById(R.id.imCamera);
        imGallery = findViewById(R.id.imGallery);
        imSave = findViewById(R.id.imSave);
        imSave.setVisibility(View.INVISIBLE);
        imClose = findViewById(R.id.imClose);

        imageAdapter = new ImageAdapter(MainActivity.this,
                R.layout.image,
                paths,
                paths.size());

        grImage.setAdapter(imageAdapter);

        imCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(askCameraPermissions()){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }
            }
        });

        imSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(indexFile == -1){
                    if(askFilePermission()){
                        if(saveImageFile()){
                            cnShowImg.setVisibility(View.INVISIBLE);

                            getImageFromSdcard();
                            imageAdapter = new ImageAdapter(MainActivity.this,
                                    R.layout.image,
                                    paths,
                                    paths.size());

                            grImage.setAdapter(imageAdapter);
                        }
                    }
                }else{
                    File image = new File(paths.get(indexFile));
                    if(!image.delete()){
                        Toast.makeText(MainActivity.this, "Can not delete file", LENGTH_SHORT).show();
                    }
                    paths.remove(indexFile);
                    indexFile = -1;
                    imSave.setImageResource(R.drawable.download);
                    imSave.setVisibility(View.INVISIBLE);
                    cnShowImg.setVisibility(View.INVISIBLE);
                    imageAdapter = new ImageAdapter(MainActivity.this,
                            R.layout.image,
                            paths,
                            paths.size());

                    grImage.setAdapter(imageAdapter);
                    Toast.makeText(MainActivity.this, "deleted!", LENGTH_SHORT).show();
                }
            }
        });

        imClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexFile = -1;
                imSave.setImageResource(R.drawable.download);
                imSave.setVisibility(View.INVISIBLE);
                cnShowImg.setVisibility(View.INVISIBLE);
            }
        });

        imGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        grImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pathFile = paths.get(i);
                Bitmap bm = BitmapFactory.decodeFile(pathFile);
                selectedImage.setImageBitmap(bm);
                cnShowImg.setVisibility(View.VISIBLE);
                imSave.setImageResource(R.drawable.trash);
                indexFile = i;
                imSave.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getImageFromSdcard() {
        paths = new ArrayList<>();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(), "Selfie");

        if (file.isDirectory())
        {
            File[] listFile = file.listFiles();
            for(int i = 0; i < listFile.length; i++){
                String filePath = listFile[i].getPath();
                paths.add(filePath);
            }
        }
    }

    private boolean saveImageFile() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        File mdir = new File(path, "/Selfie");
        if (!mdir.exists()) {
            mdir.mkdir();
        }

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        File file = new File(mdir, imageFileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(MainActivity.this, "saved!", Toast.LENGTH_LONG).show();

            galleryAddPic(file.toString());
            return true;
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Can not save image", Toast.LENGTH_LONG).show();
            Log.d("TAG", e.toString());
            return false;
        }
    }

    private void galleryAddPic(String currentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            selectedImage.setImageBitmap(bitmap);
            cnShowImg.setVisibility(View.VISIBLE);
            imSave.setVisibility(View.VISIBLE);
        }
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri contentUri = data.getData();
            selectedImage.setImageURI(contentUri);
            cnShowImg.setVisibility(View.VISIBLE);
            imSave.setVisibility(View.INVISIBLE);
        }
    }

    //Kiem tra quyen
    private boolean askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
        }else {
            return true;
        }
        return false;
    }

    public boolean askFilePermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_FILE_CODE);
        }else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CAMERA_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }else {
                    Toast.makeText(this, "Không được cấp quyền truy cập camera", LENGTH_SHORT).show();
                }
                break;
            case REQUEST_FILE_CODE:
                if(Build.VERSION.SDK_INT >= 23 && grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    if(saveImageFile()){
                        cnShowImg.setVisibility(View.GONE);
                        imSave.setVisibility(View.INVISIBLE);
                    }
                }else {
                    Toast.makeText(this, "Không được cấp quyền truy cập file", LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        startAlarm();
        Log.d("TAG", "Pause");
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        long timeBtnClick = System.currentTimeMillis();
        long times = 1000 * 5;
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeBtnClick + times, pendingIntent);
    }
}
