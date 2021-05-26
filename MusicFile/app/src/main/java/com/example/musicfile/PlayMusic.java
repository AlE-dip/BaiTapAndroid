package com.example.musicfile;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PlayMusic extends AppCompatActivity  {
    ImageButton btnplay,btnnext,btnpev, btnshuffile, btnrepeat;
    TextView txtsname, txtsstart, txtsstop;
    SeekBar seekmusic;
    String sname;
    Thread updateseekbar;
    public static final String EXTRA_NAME = "song name";
    private  static  final  int LEVEL_PLAY=0;
    private  static  final  int LEVEL_PAUSE=1;
    private  int level = LEVEL_PLAY;
    private  boolean isShuffle=true;
    private  int dem=0;
    private  boolean isRepeat=true;
    private  static  final  int LEVEL_REPEAT_OFF=0;
    private  static  final  int LEVEL_REPEAT_ONE=1;
    private  static  final  int LEVEL_REPEAT_ALL=2;
    private  int levelRepeat = LEVEL_REPEAT_OFF;
    private  static  final  int REQUEST_CODE =1;
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        btnnext = findViewById(R.id.imagebuttonnext);
        btnpev = findViewById(R.id.imagebuttonprevious);
        btnrepeat = findViewById(R.id.imagebuttonrepaeat);
        btnshuffile = findViewById(R.id.imagebuttonshuffle);
        btnplay = findViewById(R.id.imagebutonplay);
        txtsname = findViewById(R.id.txtsn);
        txtsstart = findViewById(R.id.textviewtimesong);
        txtsstop = findViewById(R.id.textviewtotaltimesong);
        seekmusic = findViewById(R.id.seekbar);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        txtsname.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        sname = mySongs.get(position).getName();
       // System.out.println("sname = " + sname);;
        txtsname.setText(sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        updateseekbar  = new Thread()
        {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currenposition = 0;
                while (currenposition<totalDuration){
                    try{
                        sleep(500);
                        currenposition =mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currenposition);

                    }
                    catch(InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.design_default_color_primary), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.design_default_color_primary),PorterDuff.Mode.SRC_IN);
        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        String endTime = createtime(mediaPlayer.getDuration());
        txtsstop.setText(endTime);
        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currenTime=createtime(mediaPlayer.getCurrentPosition()) ;
                txtsstart.setText(currenTime);
                handler.postDelayed(this,delay);
            }
        }, delay);
        run();
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    btnplay.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else {
                    btnplay.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }

            }
        });

        btnshuffile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShuffle==true) {
                    btnshuffile.setBackgroundResource(R.drawable.shuffle_1);
                    isShuffle = false;
                }
                else {
                    btnshuffile.setBackgroundResource(R.drawable.shuffle);
                    isShuffle = true;
                }

            }
        });
        btnrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( levelRepeat == LEVEL_REPEAT_OFF ){
                    levelRepeat = LEVEL_REPEAT_ONE;
                    btnrepeat.setBackgroundResource(R.drawable.repeat_one);
                }
                else if (levelRepeat==LEVEL_REPEAT_ONE){
                    levelRepeat = LEVEL_REPEAT_ALL;
                    btnrepeat.setBackgroundResource(R.drawable.repeat_all);
                }
                else {
                    levelRepeat = LEVEL_REPEAT_OFF;
                    btnrepeat.setBackgroundResource(R.drawable.repeat);
                }

            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
//                position = ((position+1)%mySongs.size());
//                Uri u = Uri.parse(mySongs.get(position).toString());
//                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
//                sname = mySongs.get(position).getName();
//                txtsname.setSelected(true);
//                String endTime = createtime(mediaPlayer.getDuration());
//                seekmusic.setMax(mediaPlayer.getDuration());
//                txtsstop.setText(endTime);
//                seekmusic.requestFocus();
//                sname = mySongs.get(position).getName();
//                txtsname.setText(sname);
                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.pause);
            }
        });
        btnpev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)? (mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName();
                txtsname.setSelected(true);
                sname = mySongs.get(position).getName();
                txtsname.setText(sname);
                String endTime = createtime(mediaPlayer.getDuration());
                seekmusic.setMax(mediaPlayer.getDuration());
//                seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.design_default_color_primary), PorterDuff.Mode.MULTIPLY);
//                seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.design_default_color_primary),PorterDuff.Mode.SRC_IN);
                txtsstop.setText(endTime);
                run();
                mediaPlayer.start();
                //btnplay.setBackgroundResource(R.drawable.pause);
            }
        });

    }
    public void run() {
        seekmusic.setProgress(mediaPlayer.getCurrentPosition());
        //Kiểm tra thời gian bài hát nếu kết thúc -> next
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                dem++;
                if (levelRepeat == LEVEL_REPEAT_ONE) position = ((position-1)<0)? (mySongs.size()-1):(position-1);
                //Kiểm tra nếu vị trí lớn hơn size thì quay về bài hát đầu tiên
                //nếu bài hát đang được hát thì dừng bài hát lại
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                KhoiTaoMediaPlayer();
//                if (position==mySongs.size()){
//                    if (levelRepeat == LEVEL_REPEAT_OFF) mediaPlayer.stop();
//                        else mediaPlayer.start();
//                }
//                else
                if (dem==mySongs.size()&&levelRepeat == LEVEL_REPEAT_OFF ) mediaPlayer.stop();
                else mediaPlayer.start();
                //Trong trường hợp khi bài hát đang dừng mà bấm next thì đổi icon
              //  btnplay.setImageResource(R.drawable.pause);
            }
        });
    }

    private void KhoiTaoMediaPlayer(){
        if (isShuffle == false) position = new Random().nextInt(mySongs.size()); else position = ((position + 1) % mySongs.size());
        Uri u = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        txtsname.setSelected(true);
        String endTime = createtime(mediaPlayer.getDuration());
        seekmusic.setMax(mediaPlayer.getDuration());
        txtsstop.setText(endTime);
        seekmusic.requestFocus();
        sname = mySongs.get(position).getName();
        txtsname.setText(sname);
        run();
    }
    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }
    public String createtime(int duration){
        String time ="";
        int min = duration/1000/60;
        int sec = duration/1000%60;
        time +=min+":";
        if (sec<10){
            time+="0";

        }
        time+=sec;
        return time;
    }


}