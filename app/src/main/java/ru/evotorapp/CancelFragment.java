package ru.evotorapp;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by sergey-rush on 30.11.2017.
 */
public class CancelFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private View.OnClickListener clickListener;
    private AnimationEndListener listener;
    public CancelFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cancel, container, false);
        view.setOnClickListener(clickListener);

        context = view.getContext();

        Button btnRemoveApp = (Button) view.findViewById(R.id.btnRemoveApp);
        btnRemoveApp.setOnClickListener(this);

        Button btnCancelAction = (Button) view.findViewById(R.id.btnCancelAction);
        btnCancelAction.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRemoveApp:
                onRemoveApp(view);
                break;
            case R.id.btnCancelAction:
                onbtnCancelAction(view);
                break;
        }
    }

    private void onRemoveApp(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.cancelApp();
    }

    private void onbtnCancelAction(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.toggleCancelFragment();
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (nextAnim > 0) {
            Animator anim = AnimatorInflater.loadAnimator(getActivity(), nextAnim);
            if (enter) {
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listener.onAnimationEnd();
                    }
                });
            }
            return anim;
        }
        return null;
    }

    public void setOnCancelFragmentAnimationEnd(AnimationEndListener listener)
    {
        this.listener = listener;
    }
}
