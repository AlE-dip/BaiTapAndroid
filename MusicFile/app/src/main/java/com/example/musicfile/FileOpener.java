package com.example.musicfile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class FileOpener extends AppCompatActivity {
    private FileAdapter fileAdapterMusic;

    private ListView recyclerViewMuic;
    public static ArrayList<File> findFiles(File file){
        ArrayList<File> arrayList =  new ArrayList<>();
        File[] files = file.listFiles();
        for (File singleFile : files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.add(singleFile);
            }
        }
        for (File singFile : files){
            if (singFile.getName().toLowerCase().endsWith(".jpeg")||singFile.getName().toLowerCase().endsWith(".jpg")||singFile.getName().toLowerCase().endsWith(".png")||singFile.getName().toLowerCase().endsWith(".mp3")
                    || singFile.getName().toLowerCase().endsWith(".mp4")||singFile.getName().toLowerCase().endsWith(".wav")||singFile.getName().toLowerCase().endsWith(".pdf")||singFile.getName().toLowerCase().endsWith(".apk")||singFile.getName().toLowerCase().endsWith(".doc")){
                arrayList.add(singFile);
            }
        }
        return  arrayList;
    }
    public void openFile(Context context, File file) throws IOException {
        final ArrayList<File> listView;
        int position = 0;
        listView = new ArrayList<>();
        listView.addAll(findFiles(new File(file.getParent())));
        for (int i=0;i<listView.size();i++){
                   if (listView.get(i).getName().equals(file.getName())) position=i;
               }
//        Log.d("position", String.valueOf(position));
//        Log.d("position1", file.getName());
//        Log.d("position2", file.getCanonicalPath());
        Uri uri = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() + ".provider",file);
//        fileAdapterMusic = new FileAdapter(context.getApplicationContext(),listView, (OnFileSelectedListener) this);
       // recyclerViewMuic.setAdapter(fileAdapterMusic);
        if (uri.toString().contains(".mp3")){
            String songName = listView.get(position).getName();
            Log.d("checkname",songName);
            Log.d("checkpos", String.valueOf(position));
            Intent intent = new Intent(context.getApplicationContext(),PlayMusic.class);
            intent.putExtra("songs", listView);
            intent.putExtra("songname",songName);
            intent.putExtra("pos",position);
            context.startActivity(intent);

        }




    }


//        File selectedfile = file;
//        Uri uri = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() + ".provider",file);
//        Intent intent = new Intent(FileOpener.this, PlayMusic.class);
//        if (uri.toString().contains(".mp3")){
//            intent.setDataAndType(uri,"audio");
//        }
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        context.startActivity(intent);


}
