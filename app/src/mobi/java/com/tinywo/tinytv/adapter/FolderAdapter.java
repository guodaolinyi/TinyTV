package com.tinywo.tinytv.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tinywo.tinytv.R;
import com.tinywo.tinytv.bean.VideoFolder;
import com.tinywo.tinytv.bean.VideoInfo;

import java.util.List;

public class FolderAdapter extends BaseQuickAdapter<VideoFolder, BaseViewHolder> {
    public FolderAdapter() {
        super(R.layout.item_folder);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoFolder item) {
        List<VideoInfo> videoList = item.getVideoList();
        helper.setText(R.id.tv_name,item.getName());
        helper.setText(R.id.tv_count,videoList.size()+"个视频");

        Glide.with(mContext)
                .load(videoList.get(0).getPath()) // 第一个视频做封面
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.iv_video)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.iv));
    }
}