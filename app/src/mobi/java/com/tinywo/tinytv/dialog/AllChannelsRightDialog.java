package com.tinywo.tinytv.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tinywo.tinytv.R;
import com.tinywo.tinytv.databinding.DialogAllChannelBinding;
import com.tinywo.tinytv.activity.LiveActivity;
import com.lxj.xpopup.core.DrawerPopupView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

public class AllChannelsRightDialog extends DrawerPopupView {

    private final LiveActivity mActivity;
    private com.tinywo.tinytv.databinding.DialogAllChannelBinding mBinding;

    public AllChannelsRightDialog(@NonNull @NotNull Context context) {
        super(context);
        mActivity = (LiveActivity) context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_all_channel;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mBinding = DialogAllChannelBinding.bind(getPopupImplView());
        initChannelGroupView();
        initLiveChannelView();
    }

    private void initChannelGroupView() {
        mBinding.mGroupGridView.setHasFixedSize(true);
        mBinding.mGroupGridView.setLayoutManager(new V7LinearLayoutManager(getContext(), 1, false));

        if (mActivity.liveChannelGroupAdapter!=null){
            mBinding.mGroupGridView.setAdapter(mActivity.liveChannelGroupAdapter);
        }

    }
    private void initLiveChannelView() {
        mBinding.mChannelGridView.setHasFixedSize(true);
        mBinding.mChannelGridView.setLayoutManager(new V7LinearLayoutManager(getContext(), 1, false));

        if (mActivity.liveChannelItemAdapter!=null){
            mBinding.mChannelGridView.setAdapter(mActivity.liveChannelItemAdapter);
        }
    }

}