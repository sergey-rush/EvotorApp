package ru.evotorapp;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sergey-rush on 30.11.2017.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private EditText etApiKey;
    public SettingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        context = view.getContext();

        etApiKey = (EditText) view.findViewById(R.id.etApiKey);
        String apiKey = LocalSettings.getApiKey(context);
        etApiKey.setText(apiKey);

        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                onButtonSaveClick(view);
                break;
        }
    }

    public void onButtonSaveClick(View view) {
        String apiKey = etApiKey.getText().toString();
        LocalSettings.setApiKey(context, apiKey);
        String key = LocalSettings.getApiKey(context);
    }
}
