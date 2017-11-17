package com.andriod.portal.widget;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.andriod.portal.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public final class TookitViewController implements View.OnClickListener, ViewGroupWrapper.OnKeyEventListerner {
    public final String TAG = this.getClass().getSimpleName();
    private WindowManager mWindowManager;
    private Context mContext;
    private ViewGroupWrapper mWholeView;
    private ViewExitListerner mViewExitListener;
    private CharSequence mContent;

    private WebView webView;

    public TookitViewController(Context application, CharSequence content) {
        mContext = application;
        mContent = content;
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setmViewExitListener(ViewExitListerner viewExitListener) {
        mViewExitListener = viewExitListener;
    }

    public interface ViewExitListerner {
        void onViewExit();
    }

    public void updateContent(CharSequence content) {
        mContent = content;
//        mTextView.setText(mContent);
    }

    public void show() {

        ViewGroupWrapper view = (ViewGroupWrapper) View.inflate(mContext, R.layout.pop_view, null);

        // display content

        mWholeView = view;
        webView = (WebView) view.findViewById(R.id.pop_view_webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


        webView.loadUrl(fetchHttpUrl("" + mContent));


        mWholeView.setOnKeyEventListerner(this);

        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.MATCH_PARENT;

        int flags = 0;
        int type = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;

        mWindowManager.addView(mWholeView, layoutParams);
    }

    public String fetchHttpUrl(String content) {
        Pattern pb = Pattern.compile("(?<!\\d)(?:(?:[\\w[.-://]]*\\.[com|cn|net|tv|gov|org|biz|cc|uk|jp|edu]+[^\\s|^\\u4e00-\\u9fa5]*))");
        Matcher mb = pb.matcher(content);
        String http = null;
        while (mb.find()) {
            //mb.replaceAll("");
            http = mb.group();
        }
        return http;
    }

    @Override
    public void onClick(View v) {
        removePoppedViewAndClear();
    }

    private void removePoppedViewAndClear() {

        // remove view
        if (mWindowManager != null && mWholeView != null) {
            mWindowManager.removeView(mWholeView);
        }
        if (mViewExitListener != null) {
            mViewExitListener.onViewExit();
        }

        // remove listeners
//        mContentView.setOnClickListener(null);
        mWholeView.setOnTouchListener(null);
        mWholeView.setOnKeyEventListerner(null);
    }


    @Override
    public void onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            removePoppedViewAndClear();
        }
    }

}




