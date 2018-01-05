package ru.evotorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey-rush on 30.11.2017.
 */
public class DataProvider extends DataAccess {

    public DataProvider(Context context) {
        super(context);
    }

    @Override
    public Slip getSlip(int id) {
        Slip slip = getSlipById(id);
        slip.Products = getProductsBySlipId(id);
        return slip;
    }

    // Id, SlipStatus, MaxAmount, MinAmount, Uuid, AppGuid, Created
    private Slip getSlipById(int id) {
        Slip slip = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SlipStatus, MaxAmount, MinAmount, Uuid, AppGuid, Created FROM Slips WHERE Id = ?", new String[]{String.valueOf(id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                slip = new Slip();
                slip.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                slip.SlipStatus = SlipStatus.fromInt(cursor.getInt(cursor.getColumnIndex("SlipStatus")));
                slip.MaxAmount = cursor.getInt(cursor.getColumnIndex("MaxAmount"));
                slip.MinAmount = cursor.getInt(cursor.getColumnIndex("MinAmount"));
                slip.Uuid = cursor.getString(cursor.getColumnIndex("Uuid"));
                slip.AppGuid = cursor.getString(cursor.getColumnIndex("AppGuid"));
                String created = cursor.getString(cursor.getColumnIndex("Created"));
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                slip.Created = format.parse(created);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return slip;
    }

    @Override
    public List<Slip> getSlipsBySlipStatus(SlipStatus slipStatus) {
        List<Slip> slips = new ArrayList<Slip>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SlipStatus, MaxAmount, MinAmount, Uuid, AppGuid, Created FROM Slips WHERE SlipStatus = ?", new String[]{String.valueOf(slipStatus.ordinal())});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Slip slip = new Slip();
                slip.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                slip.SlipStatus = SlipStatus.fromInt(cursor.getInt(cursor.getColumnIndex("SlipStatus")));
                slip.MaxAmount = cursor.getInt(cursor.getColumnIndex("MaxAmount"));
                slip.MinAmount = cursor.getInt(cursor.getColumnIndex("MinAmount"));
                slip.Uuid = cursor.getString(cursor.getColumnIndex("Uuid"));
                slip.AppGuid = cursor.getString(cursor.getColumnIndex("AppGuid"));
                String created = cursor.getString(cursor.getColumnIndex("Created"));
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                slip.Created = format.parse(created);
                slips.add(slip);
                cursor.moveToNext();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return slips;
    }

    @Override
    public List<Slip> getSlips(int limit) {
        List<Slip> slips = getSlipsByLimit(limit);
        for (Slip slip:slips) {
            slip.Products = getProductsBySlipId(slip.Id);
        }
        return slips;
    }

    private List<Slip> getSlipsByLimit(int limit) {
        List<Slip> slips = new ArrayList<Slip>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SlipStatus, MaxAmount, MinAmount, Uuid, AppGuid, Created FROM Slips ORDER BY Created DESC Limit ?", new String[]{String.valueOf(limit)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Slip slip = new Slip();
                slip.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                slip.SlipStatus = SlipStatus.fromInt(cursor.getInt(cursor.getColumnIndex("SlipStatus")));
                slip.MaxAmount = cursor.getInt(cursor.getColumnIndex("MaxAmount"));
                slip.MinAmount = cursor.getInt(cursor.getColumnIndex("MinAmount"));
                slip.Uuid = cursor.getString(cursor.getColumnIndex("Uuid"));
                slip.AppGuid = cursor.getString(cursor.getColumnIndex("AppGuid"));
                String created = cursor.getString(cursor.getColumnIndex("Created"));
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                slip.Created = format.parse(created);
                slips.add(slip);
                cursor.moveToNext();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return slips;
    }

    @Override
    public int countSlips() {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) AS Total FROM Slips", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(cursor.getColumnIndex("Total"));
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    @Override
    public int insertSlip(Slip slip) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("SlipStatus", slip.SlipStatus.ordinal());
        contentValues.put("Uuid", slip.Uuid);
        contentValues.put("MaxAmount", slip.MaxAmount);
        contentValues.put("MinAmount", slip.MinAmount);
        contentValues.put("Created", dateFormat.format(slip.Created));
        int ret = (int) db.insert("Slips", null, contentValues);
        return ret;
    }

    @Override
    public boolean updateSlipBySlipStatus(int id, SlipStatus slipStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("SlipStatus", slipStatus.ordinal());
        int ret = (int) db.update("Slips", contentValues, "Id = ?", new String[]{String.valueOf(id)});
        return ret > 0;
    }

    private boolean deleteSlipById(int id) {
        int ret = db.delete("Slips", "Id = ?", new String[]{String.valueOf(id)});
        return ret == 1;
    }

    @Override
    public int addSlip(Slip slip) {

        int slipId = insertSlip(slip);
        for (Product product : slip.Products) {
            product.SlipId = slipId;
            insertProduct(product);
        }
        return slipId;
    }

    @Override
    public void removeSlipsByStatus(SlipStatus slipStatus) {
        List<Slip> slips = getSlipsBySlipStatus(slipStatus);
        for (Slip slip : slips) {
            deleteProductsBySlipId(slip.Id);
            deleteSlipById(slip.Id);
        }
    }

    @Override
    public boolean removeSlip(int id) {
        deleteProductsBySlipId(id);
        deleteSlipById(id);
        return true;
    }

    @Override
    public Product getProductById(int id) {
        Product product = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SlipId, Uuid, ProductUuid, ProductCode, Name, MeasureName, MeasurePrecision, Price, Discount, Quantity FROM Products WHERE Id = ?", new String[]{String.valueOf(id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                product = new Product();
                product.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                product.SlipId = cursor.getInt(cursor.getColumnIndex("SlipId"));
                product.Uuid = cursor.getString(cursor.getColumnIndex("Uuid"));
                product.ProductUuid = cursor.getString(cursor.getColumnIndex("ProductUuid"));
                product.ProductCode = cursor.getString(cursor.getColumnIndex("ProductCode"));
                product.Name = cursor.getString(cursor.getColumnIndex("Name"));
                product.MeasureName = cursor.getString(cursor.getColumnIndex("MeasureName"));
                product.MeasurePrecision = cursor.getInt(cursor.getColumnIndex("MeasurePrecision"));
                product.Price = cursor.getInt(cursor.getColumnIndex("Price"));
                product.Discount = cursor.getInt(cursor.getColumnIndex("Discount"));
                product.Quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return product;
    }

    @Override
    public List<Product> getProductsBySlipId(int slipId) {
        List<Product> products = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SlipId, Uuid, ProductUuid, ProductCode, Name, MeasureName, MeasurePrecision, Price, Discount, Quantity FROM Products WHERE SlipId = ? ORDER BY Id", new String[]{String.valueOf(slipId)});
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Product product = new Product();
                product.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                product.SlipId = cursor.getInt(cursor.getColumnIndex("SlipId"));
                product.Uuid = cursor.getString(cursor.getColumnIndex("Uuid"));
                product.ProductUuid = cursor.getString(cursor.getColumnIndex("ProductUuid"));
                product.ProductCode = cursor.getString(cursor.getColumnIndex("ProductCode"));
                product.Name = cursor.getString(cursor.getColumnIndex("Name"));
                product.MeasureName = cursor.getString(cursor.getColumnIndex("MeasureName"));
                product.MeasurePrecision = cursor.getInt(cursor.getColumnIndex("MeasurePrecision"));
                product.Price = cursor.getInt(cursor.getColumnIndex("Price"));
                product.Discount = cursor.getInt(cursor.getColumnIndex("Discount"));
                product.Quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                products.add(product);
                cursor.moveToNext();
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return products;
    }

    @Override
    public int countProducts() {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) AS Total FROM Products", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(cursor.getColumnIndex("Total"));
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    @Override
    public int insertProduct(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("SlipId", product.SlipId);
        contentValues.put("Uuid", product.Uuid);
        contentValues.put("ProductUuid", product.ProductUuid);
        contentValues.put("ProductCode", product.ProductCode);
        contentValues.put("Name", product.Name);
        contentValues.put("MeasureName", product.MeasureName);
        contentValues.put("MeasurePrecision", product.MeasurePrecision);
        contentValues.put("Price", product.Price);
        contentValues.put("Discount", product.Discount);
        contentValues.put("Quantity", product.Quantity);
        int ret = (int) db.insert("Products", null, contentValues);
        return ret;
    }

    @Override
    public boolean deleteProductsBySlipId(int slipId) {
        int ret = db.delete("Products", "SlipId = ?", new String[]{String.valueOf(slipId)});
        return ret == 1;
    }

}


