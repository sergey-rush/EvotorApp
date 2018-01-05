package ru.evotorapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey-rush on 23.11.2017.
 */

public class NumPadView extends RecyclerView {

    private ValueChangedListener listener;

    public void setOnValueChangedListener(ValueChangedListener listener)
    {
        this.listener = listener;
    }

    private Context context;
    private String mNumber = "0";

    public void setValue(String value){
        mOnNumberClickListener.onNumberClicked(value);
    }

    private List<Key> Keys;
    
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mTextSize;
    private int mTextColor;
    private int mBackgroundColor;
    private int mButtonSize;

    private String mMode;
    private String mFirstSymbol;

    private NumPadAdapter mAdapter;

    private NumPadStyle mNumPadStyle;

    private int[] mCustomKeySet;

    public NumPadView(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public NumPadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public NumPadView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs, defStyle);
    }

    private NumPadAdapter.OnNumberClickListener mOnNumberClickListener = new NumPadAdapter.OnNumberClickListener() {

        @Override
        public void onNumberClicked(String keyValue) {
            mNumber = listener.onValueChanged(mNumber.concat(keyValue));
        }
    };

    private NumPadAdapter.OnDeleteClickListener mOnDeleteClickListener = new NumPadAdapter.OnDeleteClickListener() {

        @Override
        public void onDeleteClicked() {
            if (mNumber.length() > 1) {
                mNumber = mNumber.substring(0, mNumber.length() - 1);
            }
            else {
                mNumber = mFirstSymbol;
            }

            mNumber = listener.onValueChanged(mNumber);
        }
    };

    private void init(AttributeSet attributeSet, int defStyle) {

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.NumPadView);

        try {
            mTextColor = typedArray.getColor(R.styleable.NumPadView_numPadTextColor, getColor(R.color.white));
            mBackgroundColor = typedArray.getColor(R.styleable.NumPadView_numPadBackgroundColor, getColor(R.color.grey6));
            mTextSize = (int) typedArray.getDimension(R.styleable.NumPadView_numPadTextSize, getDimensionInPx(R.dimen.font16));

            mButtonSize = (int) typedArray.getDimension(R.styleable.NumPadView_numPadButtonSize, getDimensionInPx(R.dimen.dimen60));

            mHorizontalSpacing = (int) typedArray.getDimension(R.styleable.NumPadView_numPadHorizontalSpacing, getDimensionInPx(R.dimen.dimen2));
            mVerticalSpacing = (int) typedArray.getDimension(R.styleable.NumPadView_numPadVerticalSpacing, getDimensionInPx(R.dimen.dimen2));

            mMode = typedArray.getString(R.styleable.NumPadView_numPadMode);

            mFirstSymbol = typedArray.getString(R.styleable.NumPadView_numPadFirstSymbol);

            mNumber = mFirstSymbol;

        } finally {
            typedArray.recycle();
        }

        mNumPadStyle = new NumPadStyle();
        mNumPadStyle.setTextColor(mTextColor);
        mNumPadStyle.setBackgroundColor(mBackgroundColor);
        mNumPadStyle.setTextSize(mTextSize);
        mNumPadStyle.setButtonSize(mButtonSize);
        Keys = getKeys();
        initView();
    }

    public float getDimensionInPx(@DimenRes int id) {
        return context.getResources().getDimension(id);
    }

    public int getColor(@ColorRes int id) {
        return context.getResources().getColor(id);
    }

    private void initView() {
        setLayoutManager(new GridLayoutManager(context, 3));

        mAdapter = new NumPadAdapter(context, Keys);
        mAdapter.setOnItemClickListener(mOnNumberClickListener);
        mAdapter.setOnDeleteClickListener(mOnDeleteClickListener);
        mAdapter.setNumPadStyle(mNumPadStyle);

        setAdapter(mAdapter);

        addItemDecoration(new ItemSpaceDecoration(mHorizontalSpacing, mVerticalSpacing, 3, false));
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private List<Key> getKeys(){
        List<Key> keys = new ArrayList<>();

        keys.add(new Key("1", KeyType.NUMBER));
        keys.add(new Key("2", KeyType.NUMBER));
        keys.add(new Key("3", KeyType.NUMBER));
        keys.add(new Key("4", KeyType.NUMBER));
        keys.add(new Key("5", KeyType.NUMBER));
        keys.add(new Key("6", KeyType.NUMBER));
        keys.add(new Key("7", KeyType.NUMBER));
        keys.add(new Key("8", KeyType.NUMBER));
        keys.add(new Key("9", KeyType.NUMBER));
        if(mMode.equals("Phone")){
            keys.add(new Key("+", KeyType.SYMBOL));
        }else{
            keys.add(new Key(",", KeyType.SYMBOL));
        }

        keys.add(new Key("0", KeyType.NUMBER));
        keys.add(new Key("x", KeyType.DELETE));
        return keys;
    }
}