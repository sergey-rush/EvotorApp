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
 * Created by sergey-rush on 30.11.2017.
 */
public class RequestActivity extends IntegrationActivity implements AnimationEndListener,
        FragmentManager.OnBackStackChangedListener, View.OnClickListener {

    private Context context;
    private CancelFragment cancelFragment;
    private TextView tvHeader;
    private AppContext appContext;
    private boolean animating = false;
    private State state = State.COLLAPSED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        tvHeader = (TextView) findViewById(R.id.tvHeader);

        context = RequestActivity.this;
        appContext = AppContext.getInstance();

        String imei = LocalSettings.getDeviceId(context);
        RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(imei);
        registerAsyncTask.execute();

        cancelFragment = new CancelFragment();
        cancelFragment.setClickListener(this);
        cancelFragment.setOnCancelFragmentAnimationEnd(this);
        getFragmentManager().addOnBackStackChangedListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        boolean handled = false;

        Fragment fragment = getFragmentManager().findFragmentById(R.id.request_container);

        if (fragment instanceof ConfirmationFragment) {
            handled = true;
            cancelApp();
        }

        if (fragment instanceof ProductListFragment) {
            showFragment(new StartFragment());
            handled = true;
        }

        if (fragment instanceof SlipFragment) {
            showFragment(new StartFragment());
            handled = true;
        }

        if (fragment instanceof PhoneFragment) {
            showFragment(new StartFragment());
            handled = true;
        }

        if (fragment instanceof CheckoutFragment) {
            showFragment(new StartFragment());
            handled = true;
        }

        if (!handled) {
            toggleCancelFragment();
            //super.onBackPressed();
        }
    }

    @Override
    public void onBackStackChanged() {
        //Toast.makeText(context, "State: " + state + " Animating: " + animating, Toast.LENGTH_SHORT).show();
        if (state == State.COLLAPSED) {
            slideOut();
        }
    }

    public void toggleCancelFragment() {

        //Toast.makeText(context, "State: " + state + " Animating: " + animating, Toast.LENGTH_SHORT).show();

        if (animating) {
            return;
        }
        animating = true;

        if (state == State.EXPANDED) {
            state = State.COLLAPSED;
            getFragmentManager().popBackStack();
        } else {
            Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator arg0) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.animator.enter, 0,0,R.animator.exit);
                    transaction.add(R.id.request_container, cancelFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            };
            state = State.EXPANDED;
            slideIn(listener);
        }
    }

    public void slideIn(Animator.AnimatorListener listener) {
        ObjectAnimator shadowViewAnimator = ObjectAnimator.ofFloat(appContext.getCurrentFragment().getView(), "alpha", 1.0f, 0.5f);
        AnimatorSet s = new AnimatorSet();
        s.playTogether(shadowViewAnimator);
        s.setStartDelay(getResources().getInteger(R.integer.delay_animation));
        s.addListener(listener);
        s.start();
    }

    public void slideOut() {
        ObjectAnimator shadowViewAnimator = ObjectAnimator.ofFloat(appContext.getCurrentFragment().getView(), "alpha", 0.5f, 1.0f);
        AnimatorSet s = new AnimatorSet();
        s.playTogether(shadowViewAnimator);
        s.setStartDelay(getResources().getInteger(R.integer.delay_animation));
        s.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animating = false;
            }
        });
        s.start();
    }

    public void onAnimationEnd() {
        animating = false;
    }

    private void ApplicationNotRegistered() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.not_registered)
                .setMessage(R.string.register_message)
                .setIcon(R.drawable.ic_error)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        showFragment(new SettingsFragment());
                        dialog.dismiss();
                    }

                }).create();
        alertDialog.show();
    }

    public void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.request_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void cancelApp(){
        finish();
    }

    public void setHeader(int header){
        tvHeader.setText(header);
    }

    private void loadDataCallback(Device device) {
        if(device.IsActive){
            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show();
            showFragment(new StartFragment());

            AppContext appContext = new AppContext();
            appContext.setProductList(context, device);

        }else{
            ApplicationNotRegistered();
        }
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;
        private String imei;
        private Device device;

        private int responseCode;

        public RegisterAsyncTask(String imei) {
            this.imei = imei;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Пожалуйста, подождите...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            WebAccess webAccess = WebAccess.getInstance();
            responseCode = webAccess.getRegister(imei);
            device = webAccess.Device;
            return null;
        }

        @Override
        protected void onPostExecute(Void output) {
            super.onPostExecute(output);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (responseCode == 200) {
                loadDataCallback(device);
            } else if (responseCode == 403) {
                ApplicationFailed();
            } else {
                ConnectionFailed();
            }
            device.IsActive = false;
            loadDataCallback(device);
        }

        private void ApplicationFailed() {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Касса незарегистрирована")
                    .setMessage("Касса незарегистрирована. Зарегистрируйте кассу и повторите запрос")
                    .setIcon(R.drawable.ic_error)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }

                    }).create();
            alertDialog.show();
        }

        private void ConnectionFailed() {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.connection_failed)
                    .setMessage(R.string.connection_not_available)
                    .setIcon(R.drawable.ic_error)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }

                    }).create();
            alertDialog.show();
        }
    }
}
