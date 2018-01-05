package ru.evotorapp;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sergey-rush on 21.11.2017.
 */

public class Item {
    public String Id;
    public String Name;
    public BigDecimal Quantity;
    public BigDecimal Price;
    public BigDecimal DiscountPercents;
    public BigDecimal DiscountPositionSum;
    public BigDecimal PriceWithDiscountPosition;
    public BigDecimal TotalWithoutDiscounts;
    public BigDecimal Total;
}
