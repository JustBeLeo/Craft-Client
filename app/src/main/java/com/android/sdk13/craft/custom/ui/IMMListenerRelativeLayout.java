package com.android.sdk13.craft.custom.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.android.sdk13.craft.custom.InputWindowListener;

/*
 * @ ClassName IMMListenerRelativeLayout
 * @ Description 用OnSizeChange来模拟实现监听软键盘的弹出收起事件
 * @ Author sdk13
 * @ Date 2020/6/24 9:33
 * @ Version 1.0
 */
public class IMMListenerRelativeLayout extends RelativeLayout {
    private InputWindowListener listener;

    public IMMListenerRelativeLayout(Context context) {
        super(context);
    }

    public IMMListenerRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IMMListenerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > h) {
            Log.e( "TAG", "onSizeChanged: " + "input window show" );
            listener.show();
        } else{
            Log.e( "TAG", "onSizeChanged: " + "input window hide");
            listener.hidden();
        }
    }

    public void setListener(InputWindowListener listener) {
        this.listener = listener;
    }
}
