package com.kisita.caritas;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by HuguesKi on 03-12-17.
 */

public class CaritasViewPager extends ViewPager {

    private boolean isEnabled = false;

    public CaritasViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CaritasViewPager(Context context) {
        super(context);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
