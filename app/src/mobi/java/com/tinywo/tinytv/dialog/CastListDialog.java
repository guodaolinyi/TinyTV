package com.tinywo.tinytv.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

//import com.android.cast.dlna.dmc.DLNACastManager;
import com.tinywo.tinytv.R;
import com.tinywo.tinytv.bean.CastVideo;
import com.tinywo.tinytv.adapter.CastDevicesAdapter;
import com.lxj.xpopup.core.CenterPopupView;

//import org.fourthline.cling.model.meta.Device;
import org.jetbrains.annotations.NotNull;

public class CastListDialog extends CenterPopupView {

    private final CastVideo castVideo;
    private CastDevicesAdapter adapter;

    public CastListDialog(@NonNull @NotNull Context context, CastVideo castVideo) {
        super(context);
        this.castVideo = castVideo;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_cast;
    }

    /*
    @Override
    protected void onCreate() {
        super.onCreate();
        DLNACastManager.getInstance().bindCastService(App.getInstance());
        findViewById(R.id.btn_cancel).setOnClickListener(view -> {
            dismiss();
        });
        findViewById(R.id.btn_confirm).setOnClickListener(view ->{
            adapter.setNewData(new ArrayList<>());
            DLNACastManager.getInstance().search(null, 1);
        });
        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CastDevicesAdapter();
        rv.setAdapter(adapter);
        DLNACastManager.getInstance().registerDeviceListener(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Device item = (Device)adapter.getItem(position);
                if (item!=null){
                    DLNACastManager.getInstance().cast(item,castVideo);
                }
            }
        });
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        DLNACastManager.getInstance().unregisterListener(adapter);
        DLNACastManager.getInstance().unbindCastService(App.getInstance());
    }
    */
}
