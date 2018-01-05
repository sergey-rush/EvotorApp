package ru.evotorapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by sergey-rush on 14.12.2017.
 */

public class FractionalLinearLayout extends LinearLayout {

    private float mYFraction;
    private int mScreenHeight;

    public FractionalLinearLayout(Context context) {
        super(context);
    }

    public FractionalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        setY(mScreenHeight);
    }

    public float getYFraction() {
        return mYFraction;
    }

    public void setYFraction(float yFraction) {
        mYFraction = yFraction;
        setY((mScreenHeight > 0) ? (mScreenHeight - yFraction * mScreenHeight) : 0);
    }
}
