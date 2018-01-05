package ru.evotorapp;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by sergey-rush on 23.11.2017.
 */

public class NumPadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private OnNumberClickListener mOnNumberClickListener;
    private OnDeleteClickListener mOnDeleteClickListener;

    private NumPadStyle numPadStyle;

    private List<Key> keys;

    public NumPadAdapter(Context context, List<Key> keys) {
        this.context = context;
        this.keys = keys;
    }

    public void setNumPadStyle(NumPadStyle numPadStyle) {
        this.numPadStyle = numPadStyle;
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    @Override
    public int getItemViewType(int position) {
        Key key = keys.get(position);
        return key.KeyType.getValue();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        KeyType keyType = KeyType.fromInt(viewType);

        if (keyType == KeyType.NUMBER) {
            View view = inflater.inflate(R.layout.item_number, parent, false);
            viewHolder = new NumberViewHolder(view);
        }

        if (keyType == KeyType.DELETE) {
            View view = inflater.inflate(R.layout.item_delete, parent, false);
            viewHolder = new DeleteViewHolder(view);
        }

        if (keyType == KeyType.SYMBOL) {
            View view = inflater.inflate(R.layout.item_number, parent, false);
            viewHolder = new SymbolViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Key key = keys.get(position);

        if (key.KeyType == KeyType.NUMBER) {
            NumberViewHolder vh1 = (NumberViewHolder) holder;
            configureNumberButtonHolder(vh1, key);
        }

        if (key.KeyType == KeyType.DELETE) {
            DeleteViewHolder vh2 = (DeleteViewHolder) holder;
            configureDeleteButtonHolder(vh2);
        }

        if (key.KeyType == KeyType.SYMBOL) {
            SymbolViewHolder vh3 = (SymbolViewHolder) holder;
            configureDotButtonHolder(vh3, key);
        }
    }

    private void configureDeleteButtonHolder(DeleteViewHolder holder) {
        if (holder != null) {
            holder.mButtonImage.setBackgroundColor(numPadStyle.getBackgroundColor());
            holder.mButtonImage.setVisibility(View.VISIBLE);
            holder.mButtonImage.setColorFilter(numPadStyle.getTextColor(), PorterDuff.Mode.SRC_ATOP);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(numPadStyle.getButtonSize(), numPadStyle.getButtonSize());
            holder.mLinearLayout.setLayoutParams(params);
        }
    }

    private void configureDotButtonHolder(SymbolViewHolder holder, Key key) {
        holder.mNumberButton.setText(key.Name);
        holder.mNumberButton.setVisibility(View.VISIBLE);
        holder.mNumberButton.setTag(key.Name);

        holder.mNumberButton.setTextColor(numPadStyle.getTextColor());
        holder.mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, numPadStyle.getTextSize());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(numPadStyle.getButtonSize(), numPadStyle.getButtonSize());
        holder.mNumberButton.setLayoutParams(params);
    }

    private void configureNumberButtonHolder(NumberViewHolder holder, Key key) {
        holder.mNumberButton.setText(key.Name);
        holder.mNumberButton.setVisibility(View.VISIBLE);
        holder.mNumberButton.setTag(key.Name);

        holder.mNumberButton.setTextColor(numPadStyle.getTextColor());
        holder.mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, numPadStyle.getTextSize());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(numPadStyle.getButtonSize(), numPadStyle.getButtonSize());
        holder.mNumberButton.setLayoutParams(params);

    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {
        final Button mNumberButton;

        public NumberViewHolder(final View itemView) {
            super(itemView);
            mNumberButton = (Button) itemView.findViewById(R.id.button);
            mNumberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNumberClickListener != null) {
                        MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
                        mp.start();
                        String tag = v.getTag().toString();
                        mOnNumberClickListener.onNumberClicked(tag);
                    }
                }
            });
        }
    }

    public class DeleteViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLinearLayout;
        ImageView mButtonImage;

        public DeleteViewHolder(final View itemView) {
            super(itemView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.button);
            mButtonImage = (ImageView) itemView.findViewById(R.id.buttonImage);

            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDeleteClickListener != null) {
                        MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
                        mp.start();
                        mOnDeleteClickListener.onDeleteClicked();
                    }
                }
            });
        }
    }

    public class SymbolViewHolder extends RecyclerView.ViewHolder {
        Button mNumberButton;

        public SymbolViewHolder(final View itemView) {
            super(itemView);

            mNumberButton = (Button) itemView.findViewById(R.id.button);

            mNumberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNumberClickListener != null) {
                        MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
                        mp.start();
                        String tag = v.getTag().toString();
                        mOnNumberClickListener.onNumberClicked(tag);
                    }
                }
            });
        }
    }

    public OnNumberClickListener getOnItemClickListener() {

        return mOnNumberClickListener;
    }

    public void setOnItemClickListener(OnNumberClickListener onNumberClickListener) {
        this.mOnNumberClickListener = onNumberClickListener;
    }

    public OnDeleteClickListener getOnDeleteClickListener() {
        return mOnDeleteClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    public interface OnNumberClickListener {
        void onNumberClicked(String keyValue);
    }

    public interface OnDeleteClickListener {
        void onDeleteClicked();
    }
}