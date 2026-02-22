package com.tinywo.tinytv.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tinywo.tinytv.R
import com.tinywo.tinytv.base.BaseVbActivity
import com.tinywo.tinytv.bean.VideoInfo
import com.tinywo.tinytv.constant.CacheConst
import com.tinywo.tinytv.databinding.ActivityMovieFoldersBinding
import com.tinywo.tinytv.event.RefreshEvent
import com.tinywo.tinytv.adapter.LocalVideoAdapter
import com.tinywo.tinytv.util.FastClickCheckUtil
import com.tinywo.tinytv.util.Utils
import com.lxj.xpopup.XPopup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

class VideoListActivity : BaseVbActivity<ActivityMovieFoldersBinding>() {
    private var mBucketDisplayName = ""
    private var mLocalVideoAdapter = LocalVideoAdapter()
    private var mSelectedCount = 0
    override fun init() {

        mBucketDisplayName = intent.extras?.getString("bucketDisplayName")?:""

        mBinding.titleBar.setTitle(mBucketDisplayName)
        mBinding.rv.setAdapter(mLocalVideoAdapter)
        mLocalVideoAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>, _: View?, position: Int ->
                val videoInfo = adapter.getItem(position) as VideoInfo?
                if (mLocalVideoAdapter.isSelectMode) {
                    videoInfo!!.isChecked = !videoInfo.isChecked
                    mLocalVideoAdapter.notifyDataSetChanged()
                } else {
                    val bundle = Bundle()
                    bundle.putString("videoList", GsonUtils.toJson(mLocalVideoAdapter.data))
                    bundle.putInt("position", position)
                    jumpActivity(LocalPlayActivity::class.java, bundle)
                }
            }
        mLocalVideoAdapter.onItemLongClickListener =
            BaseQuickAdapter.OnItemLongClickListener { adapter: BaseQuickAdapter<*, *>, _: View?, position: Int ->
                toggleListSelectMode(true)
                val videoInfo = adapter.getItem(position) as VideoInfo?
                videoInfo!!.isChecked = true
                mLocalVideoAdapter.notifyDataSetChanged()
                true
            }

        mBinding.tvAllCheck.setOnClickListener { view: View? ->
            FastClickCheckUtil.check(view)
            for (item in mLocalVideoAdapter.data) {
                item.isChecked = true
            }
            mLocalVideoAdapter.notifyDataSetChanged()
        }

        mBinding.tvCancelAllChecked.setOnClickListener { view: View? ->
            FastClickCheckUtil.check(view)
            cancelAll()
        }

        mLocalVideoAdapter.setOnSelectCountListener { count: Int ->
            mSelectedCount = count
            if (mSelectedCount > 0) {
                mBinding.tvDelete.isEnabled = true
                mBinding.tvDelete.setTextColor(ColorUtils.getColor(R.color.colorPrimary))
            } else {
                mBinding.tvDelete.isEnabled = false
                mBinding.tvDelete.setTextColor(ColorUtils.getColor(R.color.disable_text))
            }
        }

        mBinding.tvDelete.setOnClickListener { view: View? ->
            FastClickCheckUtil.check(view)
            XPopup.Builder(this)
                .isDarkTheme(Utils.isDarkTheme())
                .asConfirm("提示", "确定删除所选视频吗？") {
                    showLoadingDialog()
                    lifecycleScope.launch(Dispatchers.IO) {
                        val data = mLocalVideoAdapter.data
                        val deleteList: MutableList<VideoInfo> = ArrayList()
                        for (item in data) {
                            if (item.isChecked) {
                                deleteList.add(item)
                                if (FileUtils.delete(item.path)) {
                                    SPUtils.getInstance(CacheConst.VIDEO_DURATION_SP).remove(item.path)
                                    SPUtils.getInstance(CacheConst.VIDEO_PROGRESS_SP).remove(item.path)
                                    FileUtils.notifySystemToScan(FileUtils.getDirName(item.path))
                                }
                            }
                        }
                        data.removeAll(deleteList)

                        withContext(Dispatchers.Main){
                            dismissLoadingDialog()
                            mLocalVideoAdapter.notifyDataSetChanged()
                            toggleListSelectMode(false)
                        }
                    }
                }.show()
        }
    }

    private fun toggleListSelectMode(open: Boolean) {
        mLocalVideoAdapter.setSelectMode(open)
        mBinding.llMenu.visibility = if (open) View.VISIBLE else View.GONE
        if (!open) {
            mLocalVideoAdapter.notifyDataSetChanged()
        }
    }

    private fun cancelAll() {
        for (item in mLocalVideoAdapter.data) {
            item.isChecked = false
        }
        mLocalVideoAdapter.notifyDataSetChanged()
    }

    override fun refresh(event: RefreshEvent) {
        Handler(Looper.getMainLooper()).postDelayed({ groupVideos() }, 1000)
    }

    override fun onResume() {
        super.onResume()
        groupVideos()
    }

    private fun groupVideos() {
        val videoList = Utils.getVideoList()
        val collect = videoList.stream()
            .filter { videoInfo: VideoInfo -> videoInfo.bucketDisplayName == mBucketDisplayName }
            .collect(Collectors.toList())
        mLocalVideoAdapter.setNewData(collect)
    }

    override fun onBackPressed() {
        if (mLocalVideoAdapter.isSelectMode) {
            if (mSelectedCount > 0) {
                cancelAll()
            } else {
                toggleListSelectMode(false)
            }
        } else {
            super.onBackPressed()
        }
    }
}
