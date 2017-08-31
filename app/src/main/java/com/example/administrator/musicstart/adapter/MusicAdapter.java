package com.example.administrator.musicstart.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.musicstart.R;
import com.example.administrator.musicstart.application.AppCache;
import com.example.administrator.musicstart.bean.Music;
import com.example.administrator.musicstart.service.PlayService;
import com.example.administrator.musicstart.utils.FileUtils;
import com.example.administrator.musicstart.utils.ToastUtils;
import com.example.administrator.musicstart.utils.binding.Bind;
import com.example.administrator.musicstart.utils.binding.ViewBinder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public class MusicAdapter extends BaseAdapter {

    private Context mContent;
    private int mPlayingPosition = -1;
    /*private List<Music> musicList;*/

    /*public MusicAdapter(Context mContent,List<Music> musicList){
        this.mContent =mContent;
        this.musicList = musicList;
    }
*/
    @Override
    public int getCount() {
        return AppCache.getMusicList().size();
    }

    @Override
    public Object getItem(int i) {
        return AppCache.getMusicList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_music, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == mPlayingPosition) {
            holder.vPlaying.setVisibility(View.VISIBLE);
        } else {
            holder.vPlaying.setVisibility(View.INVISIBLE);
        }

        Music music = AppCache.getMusicList().get(position);
       /* Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);*/
       /* holder.ivCover.setImageBitmap(cover);*/
        holder.tvTitle.setText(music.getTitle());
        String artist = FileUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum());
        holder.tvArtist.setText(artist);
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("onclick");
            }
        });
        holder.vDivider.setVisibility(isShowDivider(position) ? View.VISIBLE : View.GONE);


        return convertView;
    }

    public void updatePlayingPosition(PlayService playService) {
        /*if (playService.getPlayingMusic() != null && playService.getPlayingMusic().getType() == Music.Type.LOCAL) {
            mPlayingPosition = playService.getPlayingPosition();
        } else {
            mPlayingPosition = -1;
        }*/
    }


    private boolean isShowDivider(int position) {
        return position != AppCache.getMusicList().size() - 1;
    }
    static class ViewHolder {
        @Bind(R.id.v_playing)
        private View vPlaying;
        @Bind(R.id.iv_cover)
        private ImageView ivCover;
        @Bind(R.id.tv_title)
        private TextView tvTitle;
        @Bind(R.id.tv_artist)
        private TextView tvArtist;
        @Bind(R.id.iv_more)
        private ImageView ivMore;
        @Bind(R.id.v_divider)
        private View vDivider;

        public ViewHolder(View view) {
            ViewBinder.bind(this, view);
        }
    }
}
