package com.android.sdk13.craft.independent_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.sdk13.craft.R;
import com.study.xuan.gifshow.widget.stlview.callback.OnReadCallBack;
import com.study.xuan.gifshow.widget.stlview.widget.STLView;
import com.study.xuan.gifshow.widget.stlview.widget.STLViewBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StlActivity extends AppCompatActivity {

    @BindView(R.id.stl)
    STLView stl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_stl );
        ButterKnife.bind( this );
        init3D();
    }

    /*
     * @Description 显示3D STL模型
     * @Date 14:43 2019/9/16
     **/
    private void init3D() {
        STLViewBuilder.init(stl).Assets(this, "abc.stl").build();
        stl.setTouch(true);//是否可以触摸
        stl.setScale(true);//是否可以缩放
        stl.setRotate(true);//是否可以拖拽
        stl.setSensor(false);//是否支持陀螺仪
        //stl文件读取过程中的回调
        stl.setOnReadCallBack(new OnReadCallBack() {
            @Override
            public void onStart() {}
            @Override
            public void onReading(int cur, int total) {}
            @Override
            public void onFinish() {}
        });
    }

}
