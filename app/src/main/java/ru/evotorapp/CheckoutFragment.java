package ru.evotorapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by sergey-rush on 30.11.2017.
 */
public class CheckoutFragment extends Fragment implements View.OnClickListener{

    public CheckoutFragment() {  }

    private AppContext appContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        appContext = AppContext.getInstance();
        appContext.setCurrentFragment(this);

        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.setHeader(R.string.checkout_fragment_title);

        CheckBox cbDiscount = (CheckBox) view.findViewById(R.id.cbDiscount);
        cbDiscount.setOnClickListener(this);
        cbDiscount.setChecked(appContext.CommonDiscount);

        Button btnChange = (Button) view.findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);

        Button btnCreateApp = (Button) view.findViewById(R.id.btnCreateApp);
        btnCreateApp.setOnClickListener(this);

        Button btnCancelApp = (Button) view.findViewById(R.id.btnCancelApp);
        btnCancelApp.setOnClickListener(this);
        calcValues(view);

        return view;
    }

    private void calcValues(View view){

        TextView tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);

        String discount = Double.toString(appContext.getDiscount());
        tvDiscount.setText(discount);

        TextView tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);
        tvTotalAmount.setText(AppContext.toString(appContext.sumProductsPrice()));

        TextView tvAmountWithDiscount = (TextView) view.findViewById(R.id.tvAmountWithDiscount);
        tvAmountWithDiscount.setText(AppContext.toString(appContext.getDiscountedPrice()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbDiscount:
                onDiscount(view);
                break;
            case R.id.btnChange:
                onChange(view);
                break;
            case R.id.btnCreateApp:
                onCreateApp(view);
                break;
            case R.id.btnCancelApp:
                onCancelApp(view);
                break;
        }
    }

    private void onDiscount(View view) {
        appContext.CommonDiscount = false;
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new ProductListFragment());
    }

    private void onChange(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new DiscountFragment());
    }

    private void onCreateApp(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new PhoneFragment());
    }

    private void onCancelApp(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new StartFragment());
    }
}
