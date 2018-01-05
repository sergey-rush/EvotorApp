package ru.evotorapp;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by sergey-rush on 30.11.2017.
 */
public class DiscountFragment extends Fragment implements View.OnClickListener, ValueChangedListener {

    private static final String ARG_MODE_ID = "modeId";
    private static final String ARG_PRODUCT_ID = "productId";
    private String productId;
    private Mode mode = Mode.Slip;
    private Product product;

    private double totalAmount;
    private Context context;
    private AppContext appContext;
    private TextView tvDiscount;
    private TextView tvResult;
    private int oldDiscount;

    public DiscountFragment() {
    }

    public static DiscountFragment newInstance(int modeId, String productId) {
        DiscountFragment fragment = new DiscountFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE_ID, modeId);
        args.putString(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int modeId = getArguments().getInt(ARG_MODE_ID);
            mode = Mode.fromInt(modeId);
            productId = getArguments().getString(ARG_PRODUCT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discount, container, false);
        RequestActivity requestActivity = (RequestActivity) getActivity();
        context = view.getContext();
        appContext = AppContext.getInstance();

        tvResult = (TextView) view.findViewById(R.id.tvResult);
        tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);

        NumPadView numPadView = (NumPadView) view.findViewById(R.id.num_pad_view);
        numPadView.setOnValueChangedListener(this);

        if (mode == Mode.Slip) {
            requestActivity.setHeader(R.string.common_discount_title);
            totalAmount = appContext.sumProductsPrice();
            oldDiscount = (int) (appContext.getDiscount() * 100);
            String discount = Double.toString(appContext.getDiscount());
            numPadView.setValue(discount);
        }

        if (mode == Mode.Product) {
            product = appContext.getProduct(productId);
            TextView tvHeader = (TextView) requestActivity.findViewById(R.id.tvHeader);
            tvHeader.setText(formatHeader(product.Name));
            totalAmount = product.Price;
            oldDiscount = product.Discount;
            String discount = Double.toString(product.Discount);
            numPadView.setValue(discount);
        }

        Button btnCreateApp = (Button) view.findViewById(R.id.btnCreateApp);
        btnCreateApp.setOnClickListener(this);

        Button btnCancelApp = (Button) view.findViewById(R.id.btnCancelApp);
        btnCancelApp.setOnClickListener(this);

        return view;
    }

    public String onValueChanged(String input) {

        boolean endedWithComma = false;
        boolean endedWithCommaAndZero = false;

        int count = countChar(input, ',');
        if (count == 2) {
            input = input.substring(0, input.length() - 1);
        }

        if (input.endsWith(",")) {
            endedWithComma = true;
        }

        if (input.endsWith(",0")) {
            endedWithCommaAndZero = true;
        }

        double percents = 0;

        Locale currentLocale = Locale.getDefault();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        DecimalFormat decimalFormat = new DecimalFormat("#.##", otherSymbols);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(currentLocale);
            Number number = numberFormat.parse(input);
            double temp = number.doubleValue();
            String fs = decimalFormat.format(temp);
            number = numberFormat.parse(fs);
            percents = number.doubleValue();
        } catch (ParseException e) {
            percents = appContext.getDiscount();
        }

        if (percents > 99.99) {
            percents = appContext.getDiscount();
        }

        if (mode == Mode.Product) {
            product.Discount = (int) (percents * 100);
        } else {
            appContext.setDiscount(percents);
        }

        double discount = (totalAmount / 100) * percents;
        double amount = AppContext.roundDouble(totalAmount - discount);

        String result = String.format("%s - %s %% = %s", AppContext.toString(totalAmount), String.valueOf(percents), AppContext.toString(amount));

        //₽

        tvResult.setText(result);

        String value = decimalFormat.format(percents);

        if (endedWithComma) {
            value = value.concat(",");
        }

        if (endedWithCommaAndZero) {
            value = value.concat(",0");
        }

        tvDiscount.setText(value);
        return value;
    }

    private int countChar(String input, char symbol) {
        int inputLength = input.length();
        int count = 0;
        for (int i = 0; i < inputLength; i++) {
            if (input.charAt(i) == symbol) {
                count++;
            }
        }
        return count;
    }

    private String formatHeader(String input) {
        String output = "";
        String title = String.format("Скидка на %s", input);
        if (title.length() > 28) {
            output = String.format("%s...", title.substring(0, 28));
        } else {
            output = title;
        }
        return output;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreateApp:
                onCreateApp(view);
                break;
            case R.id.btnCancelApp:
                onCancelApp(view);
                break;
        }
    }

    private void onCreateApp(View view) {

        RequestActivity requestActivity = (RequestActivity) getActivity();
        requestActivity.showFragment(appContext.getCurrentFragment());
    }

    private void onCancelApp(View view) {

        if (mode == Mode.Product) {
            product.Discount = oldDiscount;
        } else {
            appContext.setDiscount((double) (oldDiscount / 100));
        }

        RequestActivity requestActivity = (RequestActivity) getActivity();
        requestActivity.showFragment(appContext.getCurrentFragment());
    }
}
