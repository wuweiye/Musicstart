package com.example.administrator.musicstart.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.musicstart.R;
import com.example.administrator.musicstart.adapter.MusicAdapter;
import com.example.administrator.musicstart.application.AppCache;
import com.example.administrator.musicstart.service.PlayService;
import com.example.administrator.musicstart.utils.ToastUtils;
import com.example.administrator.musicstart.utils.binding.Bind;

/**
 * Created by Administrator on 2017/8/29.
 */

public class LocalMusicFragment extends BaseFragment {


    private MusicAdapter mAdapter;
    @Bind(R.id.lv_local_music)
    private ListView lvLocalMusic;
    @Bind(R.id.tv_empty)
    private TextView tvEmpty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_music,container,false);
    }

    @Override
    protected void init() {

        mAdapter = new MusicAdapter();
        lvLocalMusic.setAdapter(mAdapter);

        updateView();
    }

    private void updateView() {
        if (AppCache.getMusicList().isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvLocalMusic.setVisibility(View.GONE);
        } else {
            lvLocalMusic.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setListener() {

        lvLocalMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //播放music
                getPlayService().play(position);

            }
        });

    }

    protected PlayService getPlayService() {

        PlayService playService = AppCache.getPlayService();
        if(playService == null){
            ToastUtils.show("null");
        }

        return playService;
    }
}
