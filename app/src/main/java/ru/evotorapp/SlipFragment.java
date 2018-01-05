package ru.evotorapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ru.evotor.framework.core.IntegrationException;
import ru.evotor.framework.core.IntegrationManagerCallback;
import ru.evotor.framework.core.IntegrationManagerFuture;
import ru.evotor.framework.core.action.command.open_receipt_command.OpenSellReceiptCommand;
import ru.evotor.framework.core.action.command.print_receipt_command.PrintReceiptCommandResult;
import ru.evotor.framework.core.action.command.print_receipt_command.PrintSellReceiptCommand;
import ru.evotor.framework.core.action.event.receipt.changes.position.PositionAdd;
import ru.evotor.framework.payment.PaymentSystem;
import ru.evotor.framework.payment.PaymentType;
import ru.evotor.framework.receipt.Payment;
import ru.evotor.framework.receipt.Position;
import ru.evotor.framework.receipt.PrintGroup;
import ru.evotor.framework.receipt.Receipt;


/**
 * Created by sergey-rush on 22.12.2017.
 */
public class SlipFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private Slip slip;
    private static final String ARG_SLIP_ID = "slipId";
    private int slipId;
    private DataAccess dataAccess;

    public SlipFragment() {
    }

    public static SlipFragment newInstance(int slipId) {
        SlipFragment fragment = new SlipFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SLIP_ID, slipId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            slipId = getArguments().getInt(ARG_SLIP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slip, container, false);

        ResponseActivity responseActivity = (ResponseActivity) getActivity();
        responseActivity.setHeader(R.string.slip_fragment_title);

        context = view.getContext();
        dataAccess = DataAccess.getInstance(context);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Button btnRemove = (Button) view.findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);

        Button btnProcess = (Button) view.findViewById(R.id.btnProcess);
        btnProcess.setOnClickListener(this);

        DataAccess dataAccess = DataAccess.getInstance(context);
        slip = dataAccess.getSlip(slipId);

        TextView tvId = (TextView) view.findViewById(R.id.tvId);
        String header = String.format("Заявка № %s", Integer.toString(slip.Id));
        tvId.setText(header);

        TextView tvUuid = (TextView) view.findViewById(R.id.tvUuid);
        String uuid = String.format("№ %s", slip.Uuid);
        tvUuid.setText(uuid);

        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        String status = String.format("Статус: %s", SlipStatus.getName(slip.SlipStatus));
        tvStatus.setText(status);

        TextView tvCreated = (TextView) view.findViewById(R.id.tvCreated);
        tvCreated.setText(AppContext.formatDate(slip.Created));

        List<Product> slipList = slip.Products;
        ProductAdapter adapter = new ProductAdapter(context, slipList);

        ListView listView = (ListView) view.findViewById(R.id.lvProducts);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProcess:
                onProcess(view);
                break;
            case R.id.btnRemove:
                onDeleteDialog();
                break;
        }
    }

    private void onProcess(View view) {

        List<Position> list = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : slip.Products) {

            BigDecimal price = new BigDecimal((product.Price * product.Quantity) / 100);
            totalPrice = totalPrice.add(price);

            list.add(Position.Builder.newInstance(product.Uuid, product.ProductUuid, product.Name, product.MeasureName, product.MeasurePrecision, new BigDecimal(product.Price / 100), new BigDecimal(product.Quantity)).build());
        }

        //Способ оплаты
        HashMap payments = new HashMap<Payment, BigDecimal>();
        payments.put(new Payment(
                UUID.randomUUID().toString(),
                totalPrice,
                //PaymentType задает тип оплаты
                new PaymentSystem(PaymentType.CASH, "Internet", "12424"),
                null,
                null,
                null
        ), totalPrice);


        PrintGroup printGroup = new PrintGroup(UUID.randomUUID().toString(), PrintGroup.Type.CASH_RECEIPT, "7Seconds", "3462546434", "Набережная", null, true);
        Receipt.PrintReceipt printReceipt = new Receipt.PrintReceipt(printGroup, list, payments, new HashMap<Payment, BigDecimal>());

        ArrayList<Receipt.PrintReceipt> listDocs = new ArrayList<>();
        listDocs.add(printReceipt);


        new PrintSellReceiptCommand(listDocs, null, "79267026528", "sergey.rush@hotmail.com", null).process(getActivity(), new IntegrationManagerCallback() {
            @Override
            public void run(IntegrationManagerFuture integrationManagerFuture) {
                try {
                    IntegrationManagerFuture.Result result = integrationManagerFuture.getResult();
                    switch (result.getType()) {
                        case OK:
                            PrintReceiptCommandResult printSellReceiptResult = PrintReceiptCommandResult.create(result.getData());
                            Toast.makeText(context, R.string.print_is_over, Toast.LENGTH_LONG).show();
                            break;
                        case ERROR:
                            Toast.makeText(context, result.getError().getMessage(), Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (IntegrationException e) {
                    e.printStackTrace();
                }
            }
        });

        dataAccess.updateSlipBySlipStatus(slipId, SlipStatus.Printed);
        onRedirect();
    }

    private void onDeleteDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.remove_slip_title)
                .setMessage(R.string.are_you_really_want_to_delete_this_app)
                .setIcon(R.drawable.ic_question)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dataAccess.removeSlip(slipId);
                        onRedirect();
                        Toast.makeText(context, "Заявка удалена", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void onRedirect() {
        SlipListFragment slipListFragment = new SlipListFragment();
        ResponseActivity responseActivity = (ResponseActivity) getActivity();
        responseActivity.showFragment(slipListFragment);
    }

    public void createSlipAndOpen() {

        List<PositionAdd> list = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : slip.Products) {

            BigDecimal price = new BigDecimal((product.Price * product.Quantity) / 100);
            totalPrice = totalPrice.add(price);

            list.add(new PositionAdd(Position.Builder.newInstance(product.Uuid, product.ProductUuid, product.Name, product.MeasureName, product.MeasurePrecision, new BigDecimal(product.Price / 100), new BigDecimal(product.Quantity)).build()));
        }

        //Открытие чека продажи. Передаются: список наименований, дополнительные поля для приложения
        new OpenSellReceiptCommand(list, null).process(getActivity(), new IntegrationManagerCallback() {
            @Override
            public void run(IntegrationManagerFuture future) {
                try {
                    IntegrationManagerFuture.Result result = future.getResult();
                    if (result.getType() == IntegrationManagerFuture.Result.Type.OK) {
                        startActivity(new Intent("evotor.intent.action.payment.SELL"));
                    }
                } catch (IntegrationException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
