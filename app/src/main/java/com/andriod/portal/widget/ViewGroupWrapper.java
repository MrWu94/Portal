package com.andriod.portal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class ViewGroupWrapper extends FrameLayout {

    public OnKeyEventListerner onKeyEventListerner;


    public ViewGroupWrapper(Context context) {
        super(context);
    }

    public ViewGroupWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnKeyEventListerner(OnKeyEventListerner onKeyEventListerner) {
        this.onKeyEventListerner = onKeyEventListerner;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (onKeyEventListerner != null) {
            onKeyEventListerner.onKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    public interface OnKeyEventListerner {
        void onKeyEvent(KeyEvent event);
    }


}
