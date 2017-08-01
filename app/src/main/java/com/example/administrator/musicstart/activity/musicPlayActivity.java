package com.example.administrator.musicstart.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.musicstart.bean.Music;
import com.example.administrator.musicstart.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class musicPlayActivity extends Activity implements View.OnClickListener,OnCompletionListener {

    public static MediaPlayer mPlayer;
    private ImageButton btn_back,btn_play,btn_pause,btn_next,btn_prev;
    private TextView current_time,finish_time,music_title,music_artist;
    private SeekBar timeBar;
    private String path = null;
    private  int position;
    private Cursor cursor;
    private Music music;
    private ArrayList musicList;
    private Timer timer = new Timer();
    private final int MSG = 0x123;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(MSG);

        }
    };
   private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
          if(msg.what == MSG){
              setTime();
          }

        }

    };
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music_play);
        if(mPlayer == null)
            mPlayer = new MediaPlayer();
        position = getIntent().getIntExtra("position",0);

        initWidget();
        stateJudge();
        timer.schedule(task,0,1000);
    }
public void initWidget(){
    btn_back = (ImageButton)findViewById(R.id.btn_back);
    btn_play = (ImageButton)findViewById(R.id.play);
    btn_pause = (ImageButton)findViewById(R.id.pause);
    btn_next = (ImageButton)findViewById(R.id.next);
    btn_prev = (ImageButton)findViewById(R.id.prev);
    current_time = (TextView)findViewById(R.id.time_current);
    finish_time = (TextView)findViewById(R.id.time);
    music_title =  (TextView)findViewById(R.id.music_title);
    music_artist = (TextView)findViewById(R.id.music_artist);
    timeBar = (SeekBar)findViewById(R.id.mediacontroller_progress);
    btn_back.setOnClickListener(this);
    btn_play.setOnClickListener(this);
    btn_pause.setOnClickListener(this);
    btn_prev.setOnClickListener(this);
    btn_next.setOnClickListener(this);
    timeBar.setOnSeekBarChangeListener(new SeekBarListener());
    mPlayer.setOnCompletionListener(this);
    musicInfo();
}
    public void musicInfo(){
        //获取MainActivity传递的值
        intent =getIntent();//开启此Activity意图对象

        //接受musicList
        Bundle bundle=this.getIntent().getExtras();
        musicList = bundle.getParcelableArrayList("list");
        music = (Music) musicList.get(position);


       // int count = getIntent().getIntExtra("count",0);
      //  String name = intent.getStringExtra("name");
       // String artist = intent.getStringExtra("artist");
        music_title.setText(music.getName());
        music_artist.setText(music.getArtist());
    }
    public void stateJudge(){
        if(MainActivity.tag) {
            musicPrepare();
            play();
        }else{
            if(!mPlayer.isPlaying()){
                if(isPrepared())
                {
                    changPlayStatus(false);
                }else{
                    musicPrepare();
                    changPlayStatus(false);
                }
            }
        }

    }
    public  void musicPrepare()
    {

        //path = intent.getStringExtra("path");
        mPlayer.reset();
        try {
            mPlayer.setDataSource(music.getUrl());
           // mPlayer.setDataSource(path);
            mPlayer.prepare();
            MainActivity.prepared = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void play(){
        changPlayStatus(true);
        mPlayer.start();
    }
    public Boolean isPrepared(){
        if(MainActivity.prepared)
        {
            return true;
        }else{
            return false;
        }
    }
    public void changPlayStatus(Boolean b){
        if(b){
            btn_play.setVisibility(View.GONE);
            btn_pause.setVisibility(View.VISIBLE);
        }else{
            btn_play.setVisibility(View.VISIBLE);
            btn_pause.setVisibility(View.GONE);
        }
    }
    public void pause(){
        changPlayStatus(false);
        mPlayer.pause();
    }
    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.btn_back:
               backListener();
                break;
            case R.id.play:
                playListener();
                break;
            case R.id.pause:
                pauseListener();
                break;
            case R.id.prev:
                prevListener();
                break;
            case R.id.next:
                nextListener();
                break;

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        seriation(0);
        musicPrepare();
        play();

    }
    private void playListener()
    {
        if(!mPlayer.isPlaying())
        {
            if(isPrepared()){
                play();
            }
        }
    }
    private void pauseListener(){
        if(mPlayer.isPlaying()){
            pause();
        }
    }
    private void prevListener(){

        seriation(1);
        musicPrepare();
        setTime();
        play();

    }
    private void nextListener(){

        seriation(0);
        musicPrepare();
        setTime();
        play();
    }

    public void seriation(int i)
    {
        int count = musicList.size();
        if(i==1){
            position = position - 1>= 0 ? position - 1:count - 1;
        }else{
            position = position + 1<= count - 1 ? position + 1:0;
        }
        MainActivity.position = position;
        //MainActivity.tag = true;
        musicInfo();
    }
    public void setTime(){
        timeBar.setMax(mPlayer.getDuration());
        finish_time.setText(toTime(mPlayer.getDuration()));
        timeBar.setProgress(mPlayer.getCurrentPosition());
        current_time.setText(toTime(mPlayer.getCurrentPosition()));
    }
    public String toTime(int time){
        time/=1000;
        int minute = time/60;
        int second = time % 60;
        return String.format("%01d:%02d",minute,second);
    }
    class SeekBarListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                mPlayer.seekTo(progress);  //设定歌曲播放进度
                setTime();                 // 显示播放时间信息
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    private void backListener(){

        timer.cancel();
        startActivity(new Intent(musicPlayActivity.this,MainActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
