package ru.evotorapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by sergey-rush on 22.12.2017.
 */
public class NoSlipFragment extends Fragment implements View.OnClickListener{

    public NoSlipFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_slip, container, false);

        ResponseActivity responseActivity = (ResponseActivity) getActivity();
        responseActivity.setHeader(R.string.no_slip_fragment_title);

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
        ResponseActivity responseActivity = (ResponseActivity)getActivity();
        responseActivity.onBackPressed();
    }
}
