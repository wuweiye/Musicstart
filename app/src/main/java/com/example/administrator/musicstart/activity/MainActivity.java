package com.example.administrator.musicstart.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.musicstart.bean.Music;
import com.example.administrator.musicstart.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    public static boolean prepared;
    public static Boolean tag;
    public static int position;


    private  Cursor cursor;
    private ListView lv_musicname;
    private List<Music> musicsList =new ArrayList<Music>();;
    private long mFistBackKeyPressTime = -1;
    private long mLastBackKeyPressTime = -1;
    private static long INTERVAL = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        lv_musicname = (ListView)this.findViewById(R.id.Lv_musicname);
        //musicsList = new ArrayList<Music>();

        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);

        if(cursor!=null&&cursor.getCount()>0)
        {
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                Music music = new Music();
                music.setId(id);
                music.setName(tilte);
                music.setAlbum(album);
                music.setArtist(artist);
                music.setUrl(url);
                music.setDuration(duration);
                music.setSize(size);
                musicsList.add(music);
            }
            lv_musicname.setAdapter(new myListAdapter());
        }

         lv_musicname.setOnItemClickListener(new ListListener());
        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                tag = false;
            }
        });
    }
    private class ListListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
            position = position1;
            tag = true;
            intent(position);
        }
    }
    private void intent(int position){
        Intent it =new Intent(MainActivity.this,musicPlayActivity.class);
        Music music = (Music) musicsList.get(position);


        it.putExtra("position",position);
        //it.putExtra("name",music.getName());
       // it.putExtra("artist",music.getArtist());
        //it.putExtra("path",music.getUrl());
        //it.putExtra("count",musicsList.size());
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("list", (ArrayList)musicsList);
        it.putExtras(bundle);

        startActivity(it);

        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
    public  class myListAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            return musicsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           View view;
            if(convertView == null)
            {
               view = View.inflate(getApplicationContext(),R.layout.usic_item,null);

            }else
            {
                view = convertView;
            }
            TextView name = (TextView)view.findViewById(R.id.tv_musicname);
            TextView artist = (TextView)view.findViewById(R.id.tv_musicartist);
            Music music = (Music) musicsList.get(position);
            name.setText(music.getName());
            artist.setText(music.getArtist());

            return view;
        }
    }

    //按“返回”两次退出程序
    @Override
    public void onBackPressed() {
        if(mFistBackKeyPressTime == -1){
            mFistBackKeyPressTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_LONG).show();

        }else{
            mLastBackKeyPressTime = System.currentTimeMillis();
            if((mLastBackKeyPressTime-mFistBackKeyPressTime) <= INTERVAL){
                prepared = false;
                if(musicPlayActivity.mPlayer!=null){
                    musicPlayActivity.mPlayer.reset();

                }

                AppManager.getAppManager().finishActivity();
            }else{
                mFistBackKeyPressTime = mLastBackKeyPressTime;
                Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_LONG).show();
            }
        }
    }
}
