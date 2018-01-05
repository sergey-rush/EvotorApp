package ru.evotorapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by sergey-rush on 30.11.2017.
 */
public class StartFragment extends Fragment implements View.OnClickListener{

    private Context context;
    public StartFragment() {}
    private AppContext appContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        context = view.getContext();

        appContext = AppContext.getInstance();
        appContext.setCurrentFragment(this);

        RequestActivity requestActivity = (RequestActivity) getActivity();
        requestActivity.setHeader(R.string.main_fragment_title);

        Button btnAddDiscount = (Button) view.findViewById(R.id.btnAddDiscount);
        btnAddDiscount.setOnClickListener(this);

        Button btnCancelApp = (Button) view.findViewById(R.id.btnCancelApp);
        btnCancelApp.setOnClickListener(this);

        calcValues(view);
        return view;
    }

    private void calcValues(View view){

        TextView tvCountItems = (TextView) view.findViewById(R.id.tvCountItems);
        String itemsCount = Integer.toString(appContext.countItems());
        tvCountItems.setText(itemsCount);

        //final NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));

        TextView tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);
        tvTotalAmount.setText(AppContext.toString(appContext.sumProductsPrice()));

        TextView tvAmountWithDiscount = (TextView) view.findViewById(R.id.tvAmountWithDiscount);
        String amountWithDiscount = AppContext.toString(appContext.getDiscountedPrice());
        tvAmountWithDiscount.setText(amountWithDiscount);

        Button btnCreateApp = (Button) view.findViewById(R.id.btnCreateApp);
        btnCreateApp.setOnClickListener(this);
        String buttonText = String.format("%s %s Р", btnCreateApp.getText(), amountWithDiscount);
        btnCreateApp.setText(buttonText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddDiscount:
                onAddDiscount(view);
                break;
            case R.id.btnCreateApp:
                onCreateApp(view);
                break;
            case R.id.btnCancelApp:
                onCancelApp(view);
                break;
        }
    }

    private void onAddDiscount(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        if(appContext.CommonDiscount){
            requestActivity.showFragment(new CheckoutFragment());
        }else {
            requestActivity.showFragment(new ProductListFragment());
        }
    }

    private void onCreateApp(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new PhoneFragment());
    }

    private void onCancelApp(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.toggleCancelFragment();
    }
}
