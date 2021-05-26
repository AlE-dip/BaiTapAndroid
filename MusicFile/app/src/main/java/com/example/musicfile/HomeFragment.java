package com.example.musicfile;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnFileSelectedListener{
    private RecyclerView recyclerView;
    private List<File> fileList;
    private ImageView img_back;
    private FileAdapter fileAdapter;
    private TextView tv_pathHolder;
    private List<File> fileListMusic;



    File storge;
    View view;
    String data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);
        tv_pathHolder =  view.findViewById(R.id.tv_pathHolder);
        img_back = view.findViewById(R.id.img_back);
        String internalStorage = System.getenv("EXTERNAL_STORAGE");
        storge = new File(internalStorage);
        try {
            data = getArguments().getString("path");
            File file = new File(data);
            storge = file ;

        } catch (Exception e){
            e.printStackTrace();
        }
        tv_pathHolder.setText(storge.getAbsolutePath());
        runtimePermission();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storge = storge.getParentFile();
                tv_pathHolder.setText(storge.getAbsolutePath());
                displayFiles();
            }
        });
        return view;
    }
    public void setStorge(File storge) {
        this.storge = storge;
    }
    private void runtimePermission(){
            Dexter.withContext(getContext()).withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    displayFiles();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
    }
    public ArrayList<File> findFiles(File file){
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
    private void displayFiles() {
        recyclerView = view.findViewById(R.id.recyler_internal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        fileList = new ArrayList<>();
        fileList.addAll(findFiles(storge));
        fileAdapter = new FileAdapter(getContext(),fileList,this);
        recyclerView.setAdapter(fileAdapter);
    }

    @Override
    public void onFileClicked(File file) {
        if (file.isDirectory()){
            Bundle bundle = new Bundle();
            bundle.putString("path",file.getAbsolutePath());
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).addToBackStack(null).commit();

        }
        else {
            try {
                FileOpener s= new FileOpener();
                s.openFile(getContext(),file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFileLongClicked(File file) {

    }
}