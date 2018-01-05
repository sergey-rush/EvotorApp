package ru.evotorapp;

import android.content.Intent;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.evotor.framework.core.IntegrationService;
import ru.evotor.framework.core.action.event.receipt.before_positions_edited.BeforePositionsEditedEvent;
import ru.evotor.framework.core.action.event.receipt.before_positions_edited.BeforePositionsEditedEventProcessor;
import ru.evotor.framework.core.action.event.receipt.changes.position.IPositionChange;
import ru.evotor.framework.core.action.event.receipt.changes.position.PositionAdd;
import ru.evotor.framework.core.action.event.receipt.changes.position.SetExtra;
import ru.evotor.framework.core.action.event.receipt.discount.ReceiptDiscountEvent;
import ru.evotor.framework.core.action.event.receipt.discount.ReceiptDiscountEventProcessor;
import ru.evotor.framework.core.action.event.receipt.discount.ReceiptDiscountEventResult;
import ru.evotor.framework.core.action.event.receipt.payment.system.PaymentSystemProcessor;
import ru.evotor.framework.core.action.event.receipt.payment.system.event.PaymentSystemEvent;
import ru.evotor.framework.core.action.event.receipt.payment.system.event.PaymentSystemPaybackCancelEvent;
import ru.evotor.framework.core.action.event.receipt.payment.system.event.PaymentSystemPaybackEvent;
import ru.evotor.framework.core.action.event.receipt.payment.system.event.PaymentSystemSellCancelEvent;
import ru.evotor.framework.core.action.event.receipt.payment.system.event.PaymentSystemSellEvent;
import ru.evotor.framework.core.action.processor.ActionProcessor;
import ru.evotor.framework.receipt.Position;

/**
 * Created by sergey-rush on 19.12.2017.
 */

public class ApplicationService extends IntegrationService {

    @Nullable
    @Override
    protected Map<String, ActionProcessor> createProcessors() {

        Map<String, ActionProcessor> map = new HashMap<>();

        map.put(ReceiptDiscountEvent.NAME_SELL_RECEIPT, new ReceiptDiscountEventProcessor() {

            @Override
            public void call(@NonNull String action, @NonNull ReceiptDiscountEvent event, @NonNull Callback callback) {
                try {

                    Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
                    callback.startActivity(intent);
                    callback.skip();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return map;
    }
}