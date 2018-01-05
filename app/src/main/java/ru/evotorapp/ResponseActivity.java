package ru.evotorapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ru.evotor.framework.core.IntegrationActivity;
import ru.evotor.framework.core.action.event.receipt.payment.system.event.PaymentSystemEvent;
import ru.evotor.framework.core.action.event.receipt.payment.system.event.PaymentSystemSellCancelEvent;
import ru.evotor.framework.core.action.event.receipt.payment.system.result.PaymentSystemPaymentErrorResult;
import ru.evotor.framework.core.action.event.receipt.payment.system.result.PaymentSystemPaymentResult;
import ru.evotor.framework.receipt.Position;
import ru.evotor.framework.receipt.Receipt;
import ru.evotor.framework.receipt.ReceiptApi;

/**
 * Created by sergey-rush on 22.12.2017.
 */
public class ResponseActivity extends IntegrationActivity implements View.OnClickListener {
    private TextView tvHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        tvHeader = (TextView) findViewById(R.id.tvHeader);


        DataAccess dataAccess = DataAccess.getInstance(getApplicationContext());
        int count = dataAccess.countSlips();
        if (count > 0) {
            showFragment(new SlipListFragment());
        } else {
            showFragment(new NoSlipFragment());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

    public void setHeader(int header){
        tvHeader.setText(header);
    }

    @Override
    public void onBackPressed() {

        boolean handled = false;

        Fragment fragment = getFragmentManager().findFragmentById(R.id.response_container);

        if (fragment instanceof SlipFragment) {
            showFragment(new SlipListFragment());
            handled = true;
        }

        if(!handled){
            finish();
        }
    }

    public void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.response_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}