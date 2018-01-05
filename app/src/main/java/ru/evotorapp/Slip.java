package ru.evotorapp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by sergey-rush on 13.12.2017.
 */

public class Slip {
    public int Id;
    public String Uuid;
    public String AppGuid;
    public int MaxAmount;
    public int MinAmount;
    public SlipStatus SlipStatus;
    public int Amount;
    public List<Product> Products;
    public Date Created;
}
