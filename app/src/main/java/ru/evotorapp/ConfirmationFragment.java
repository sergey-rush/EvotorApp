package ru.evotorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sergey-rush on 28.12.2017.
 */
public class ConfirmationFragment extends Fragment implements View.OnClickListener{
    private Context context;
    public ConfirmationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        context = view.getContext();

        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.setHeader(R.string.confirmation_fragment_title);

        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                onConfirm(view);
                break;
        }
    }

    private void onConfirm(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.cancelApp();
    }

}
