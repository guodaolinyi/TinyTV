package com.tinywo.tinytv.activity

import android.os.Build
import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tinywo.tinytv.base.BaseVbActivity
import com.tinywo.tinytv.bean.VideoFolder
import com.tinywo.tinytv.bean.VideoInfo
import com.tinywo.tinytv.databinding.ActivityMovieFoldersBinding
import com.tinywo.tinytv.adapter.FolderAdapter
import com.tinywo.tinytv.util.Utils
import java.util.function.Function
import java.util.stream.Collectors

class MovieFoldersActivity : BaseVbActivity<ActivityMovieFoldersBinding>() {

    private var mFolderAdapter = FolderAdapter()
    override fun init() {
        mBinding.rv.setAdapter(mFolderAdapter)
        mFolderAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, _, position ->
                val videoFolder = adapter.getItem(position) as VideoFolder?
                if (videoFolder != null) {
                    val bundle = Bundle()
                    bundle.putString("bucketDisplayName", videoFolder.name)
                    jumpActivity(VideoListActivity::class.java, bundle)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        groupVideos()
    }

    /**
     * 按文件夹名字分组视频
     */
    private fun groupVideos() {
        val videoList = Utils.getVideoList()
        val videoMap = videoList.stream()
            .collect(
                Collectors.groupingBy { obj: VideoInfo -> obj.bucketDisplayName }
            )
        val videoFolders: MutableList<VideoFolder> = ArrayList()
        videoMap.forEach { (key: String?, value: List<VideoInfo>?) ->
            val videoFolder = VideoFolder(key, value)
            videoFolders.add(videoFolder)
        }
        mFolderAdapter.setNewData(videoFolders)
    }
}