package ru.evotorapp;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * Created by sergey-rush on 30.11.2017.
 */
public class ProductListFragment extends Fragment implements View.OnClickListener{

    public ProductListFragment() { }

    private ListView listView;
    private Context context;
    private View view;

    private AppContext appContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        context = view.getContext();

        appContext = AppContext.getInstance();
        appContext.setCurrentFragment(this);

        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.setHeader(R.string.product_list_fragment_title);

        Button btnCreateApp = (Button) view.findViewById(R.id.btnCreateApp);
        btnCreateApp.setOnClickListener(this);

        Button btnCancelApp = (Button) view.findViewById(R.id.btnCancelApp);
        btnCancelApp.setOnClickListener(this);

        CheckBox cbDiscount = (CheckBox) view.findViewById(R.id.cbDiscount);
        cbDiscount.setOnClickListener(this);
        cbDiscount.setChecked(appContext.CommonDiscount);

        initList();

        return view;
    }

    private void initList() {

        List<Product> statusList = appContext.getProducts();
        ProductAdapter adapter = new ProductAdapter(context, statusList);

        listView = (ListView) view.findViewById(R.id.lvProducts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                RelativeLayout layout = (RelativeLayout) view;
                TextView tvName = (TextView) layout.findViewById(R.id.tvName);
                String productId = String.valueOf(tvName.getTag());

                DiscountFragment fragment = DiscountFragment.newInstance(Mode.Product.getValue(), productId);
                RequestActivity requestActivity = (RequestActivity)getActivity();
                requestActivity.showFragment(fragment);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbDiscount:
                onDiscount(view);
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
        appContext.CommonDiscount = true;
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new CheckoutFragment());
    }

    private void onCreateApp(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new ConfirmationFragment());
    }

    private void onCancelApp(View view) {
        RequestActivity requestActivity = (RequestActivity)getActivity();
        requestActivity.showFragment(new StartFragment());
    }

}
