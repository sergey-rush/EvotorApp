package ru.evotorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import java.util.List;


/**
 * Created by sergey-rush on 30.11.2017.
 */
public class PhoneFragment extends Fragment implements View.OnClickListener, ValueChangedListener{

    private Context context;
    private TextView tvPhone;
    private Button btnCreateApp;
    private AppContext appContext;
    public PhoneFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);

        context = view.getContext();

        appContext = AppContext.getInstance();

        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.setHeader(R.string.phone_fragment_title);

        NumPadView numPadView = (NumPadView) view.findViewById(R.id.num_pad_view);
        numPadView.setOnValueChangedListener(this);

        btnCreateApp = (Button) view.findViewById(R.id.btnCreateApp);
        btnCreateApp.setOnClickListener(this);
        btnCreateApp.setBackgroundColor(Color.GRAY);
        //btnCreateApp.setEnabled(false);

        Button btnCancelApp = (Button) view.findViewById(R.id.btnCancelApp);
        btnCancelApp.setOnClickListener(this);

        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvPhone.setText("+");

        return view;
    }

    public String onValueChanged(String value) {

        String input = formatPhone(value);
        int inputLength = input.length();

        if(inputLength > 16){
            input = input.substring(0, 16);
        }

        boolean valid;
        String regex = "^(?:\\+?7[-. ]?)?\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{2})[-. ]?([0-9]{2})$";
        if (input.matches(regex)) {
            btnCreateApp.setEnabled(true);
            btnCreateApp.setBackgroundColor(Color.parseColor("#009900"));
            appContext.setPhone(input);
        }
        else {
            btnCreateApp.setEnabled(false);
            btnCreateApp.setBackgroundColor(Color.GRAY);
        }

        tvPhone.setText(input);
        return input;
    }

    public String formatPhone(String input) {
        int inputLength = input.length();
        if(inputLength == 2){
            return input.concat("(");
        }
        if(inputLength == 6){
            return input.concat(")");
        }
        if(inputLength == 10){
            return input.concat("-");
        }
        if(inputLength == 13){
            return input.concat("-");
        }

        return input;
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
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new ConfirmationFragment());
    }

    private void onCancelApp(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new StartFragment());
    }


    private void loadDataCallback(String applicationGuid) {
        String info = String.format("Заявка %s принята на рассмотрение", applicationGuid);
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    public void onButtonSendClick(View view) {

        Order order = createOrder();
        OrderAsyncTask orderAsyncTask = new OrderAsyncTask(order);
        orderAsyncTask.execute();
    }

    private Order createOrder() {
        Order order = new Order();
        order.IsOffline = "true";
        order.Password = "fd7846c1b17d4d8bb89a32bdfc7db1d5";
        order.Merchant = "08d32fc6-76c6-4699-9d57-b96ba3ee2092";
        order.OrderId = "44";
        order.OrderDesc = "Заказ из мобильного приложения";
        order.Amount = "6009000";
        order.AmountWithDiscount = "6009000";
        order.DeliveryPrice = "0";
        order.FirstName = "Кирилл";
        order.LastName = "Поздняков";
        order.MiddleName = "Валерьевич";
        order.Phone = "89345638723";
        order.Address = "Москва, Балаклавский проспект, дом 34, кв 33";
        order.LoanTerm = "4";
        order.Cart = new ArrayList<>();
        order.Cart.add(createCart());
        return order;
    }

    private Cart createCart() {
        Cart cart = new Cart();
        cart.Category = new ArrayList<>();
        cart.Category.add("Электроника");
        cart.Category.add("Компьютеры");
        cart.Category.add("Аксессуары");
        cart.ProductId = "23551";
        cart.ProductName = "Кабель Usb";
        cart.Price = "6009000";
        cart.PriceWithDiscount = "6009000";
        cart.Quantity = "1";
        return cart;
    }

    private class OrderAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;
        private Order order;
        private String applicationGuid;
        private int responseCode;

        public OrderAsyncTask(Order order) {
            this.order = order;
        }

        private String formatPayload(Order o) {
            String cart = "";
            for (Cart c : o.Cart) {
                cart += "{";
                String category = "";

                for (String name : c.Category) {
                    category += String.format("\"%s\",", name);
                }

                cart += String.format("\"Category\":[%s], \"ProductID\":\"%s\", \"ProductName\":\"%s\", \"Price\":%s, \"PriceWithDiscount\":%s, \"Quantity\":%s},", category, c.ProductId, c.ProductName, c.Price, c.PriceWithDiscount, c.Quantity);
            }

            String p = String.format("{\"IsOffline\":\"%s\",\"Password\":\"%s\",\"Merchant\":\"%s\",\"OrderID\":\"%s\",\"OrderDesc\":\"%s\",\"Amount\":%s,\"AmountWithDiscount\":%s,\"DeliveryPrice\":%s,\"FirstName\":\"%s\",\"LastName\":\"%s\",\"LoanTerm\":%s,\"MiddleName\":\"%s\",\"Phone\":\"%s\",\"Address\":\"%s\",\"Cart\":[%s]}", o.IsOffline, o.Password, o.Merchant, o.OrderId, o.OrderDesc, o.Amount, o.AmountWithDiscount, o.DeliveryPrice, o.FirstName, o.LastName, o.LoanTerm, o.MiddleName, o.Phone, o.Address, cart);
            return p;
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

            String postData = formatPayload(order);
            //String postData = "{\"IsOffline\":true,\"Password\":\"fd7846c1b17d4d8bb89a32bdfc7db1d5\",\"Merchant\":\"08d32fc6-76c6-4699-9d57-b96ba3ee2092\",\"OrderID\":\"43\",\"OrderDesc\":\"Заказ в магазине ООО \\\"Юлмарт\\\"\",\"Amount\":6009000,\"AmountWithDiscount\":6009000,\"DeliveryPrice\":0,\"FirstName\":\"\",\"LastName\":\"\",\"LoanTerm\":null,\"MiddleName\":\"\",\"Phone\":\"\",\"Address\":\"\",\"Cart\":[{\"Category\":[\"Прочие электротовары \"],\"ProductID\":\"1\",\"ProductName\":\"Тест\",\"Price\":6009000,\"PriceWithDiscount\":6009000,\"Quantity\":1}]}";
            OrderProvider orderProvider = new OrderProvider();
            applicationGuid = orderProvider.postOrder(postData);
            responseCode = orderProvider.responseCode;

            return null;
        }

        @Override
        protected void onPostExecute(Void output) {
            super.onPostExecute(output);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (responseCode == 200) {
                loadDataCallback(applicationGuid);
            } else if (responseCode == 400) {
                ApplicationFailed();
            } else {
                ConnectionFailed();
            }
            if (responseCode != 200) {
                Toast.makeText(context, String.format("ResponseCode: %d", responseCode), Toast.LENGTH_SHORT).show();
            }
        }

        private void ApplicationFailed() {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Заявка недействительна")
                    .setMessage("Кредитная заявка недействительна")
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
