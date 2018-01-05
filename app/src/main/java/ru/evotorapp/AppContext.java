package ru.evotorapp;

import android.app.Fragment;
import android.content.Context;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.evotor.framework.receipt.Position;
import ru.evotor.framework.receipt.Receipt;
import ru.evotor.framework.receipt.ReceiptApi;

/**
 * Created by sergey-rush on 30.11.2017.
 */
public class AppContext {

    final static NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
    private Slip slip;
    private Context context;
    public boolean CommonDiscount = true;
    private DataAccess dataAccess = DataAccess.getInstance(context);
    private double basketDiscount = 0;
    private static AppContext current;

    public static AppContext getInstance() {
        if (current == null) {
            current = new AppContext();
        }
        return current;
    }

    private Fragment fragment;
    public void setCurrentFragment(Fragment fragment){
        this.fragment = fragment;
    }
    public Fragment getCurrentFragment(){
        return fragment;
    }

    private String phone = "";

    public String getPhone(){
        return phone;
    }

    public void setPhone(String value) {
        phone = value;
    }

    public Slip getSlip(){
        //slip.setAmount(sumProductsPrice());
        return slip;
    }

    public void setProductList(Context context, Device device) {
        this.context = context;

        // TODO: update on release
        //dataAccess.removeSlipsByStatus(SlipStatus.Registered);

        slip = new Slip();
        slip.SlipStatus = SlipStatus.Registered;
        slip.Created = new Date();

        List<Product> products = new ArrayList<>();

        Receipt receipt = ReceiptApi.getReceipt(context, Receipt.Type.SELL);

        for (Position position : receipt.getPositions()) {
            Product product = new Product();
            product.Uuid = position.getUuid();
            product.ProductUuid = position.getProductUuid();
            product.ProductCode = position.getProductCode();
            product.Name = position.getName();
            product.MeasurePrecision = position.getMeasurePrecision();
            product.MeasureName = position.getMeasureName();
            product.Quantity = position.getQuantity().intValue();
            product.Price = position.getPrice().intValue() * 100;
            products.add(product);
        }

        Receipt.Header header = receipt.getHeader();
        slip.Uuid = header.getUuid();
        slip.MaxAmount = device.MaxAmount;
        slip.MinAmount = device.MinAmount;
        slip.Products = products;
        int slipId = dataAccess.addSlip(slip);
        slip = dataAccess.getSlip(slipId);
    }

    public double sumProductsPrice(){
        int total = 0;
        for (Product product:slip.Products) {
            total += product.Price*product.Quantity;
        }
        return total/100;
    }



    public double getDiscount(){
        //BigDecimal result = new BigDecimal(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
        //return result.doubleValue();
        return basketDiscount;
    }

    public void setDiscount(double value) {
        //BigDecimal result = new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP);
        //discount = result.toString();
        basketDiscount = value;
    }

    public List<Product> getProducts() {
        return slip.Products;
    }

    public int countItems(){
        return slip.Products.size();
    }

    public Product getProduct(String productId) {
        for (Product product : slip.Products) {
//            if (product.Id == productId) {
//                return product;
//            }
        }
        return null;
    }

    public double getDiscountedPrice() {
        int total = 0;
        if (CommonDiscount) {
            double subtotal = sumProductsPrice();
            total = (int) ((subtotal - ((subtotal / 100) * basketDiscount)) * 100);
        } else {
            for (Product product : slip.Products) {
                int itemPrice = product.Price - ((product.Price / 100) * product.Discount);
                total += itemPrice * product.Quantity;
            }
        }
        return total / 100;
    }

    public static double roundDouble(double input){
        long cents = (long)(input * 100 + 0.5);
        double rounded = cents/100;
        return rounded;
    }

    public static String toString(double value){
        return currencyInstance.format(value);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }
}
