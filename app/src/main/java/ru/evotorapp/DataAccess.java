package ru.evotorapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by sergey-rush on 28.12.2017.
 */
public abstract class DataAccess extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SSDB.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    protected SQLiteDatabase db;

    protected DataAccess(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        db = getWritableDatabase();
    }

    private static DataAccess _instance = null;

    public static DataAccess getInstance(Context context) {
        if (_instance == null) {
            _instance = new DataProvider(context);
        }
        return _instance;
    }

    public abstract Slip getSlip(int id);

    public abstract List<Slip> getSlipsBySlipStatus(SlipStatus slipStatus);

    public abstract List<Slip> getSlips(int limit);

    public abstract int countSlips();

    public abstract int insertSlip(Slip slip);

    public abstract boolean updateSlipBySlipStatus(int id, SlipStatus slipStatus);

    public abstract void removeSlipsByStatus(SlipStatus status);

    public abstract int addSlip(Slip slip);

    public abstract boolean removeSlip(int id);

    public abstract Product getProductById(int id);

    public abstract List<Product> getProductsBySlipId(int slipId);

    public abstract int countProducts();

    public abstract int insertProduct(Product product);

    public abstract boolean deleteProductsBySlipId(int slipId);

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Slips(Id INTEGER PRIMARY KEY AUTOINCREMENT, SlipStatus INTEGER, MaxAmount INTEGER, MinAmount INTEGER, Uuid TEXT, AppGuid TEXT, Created TEXT)");
        db.execSQL("CREATE TABLE Products(Id INTEGER PRIMARY KEY AUTOINCREMENT, SlipId INTEGER, Uuid TEXT, ProductUuid TEXT, ProductCode TEXT, Name TEXT, MeasureName TEXT, MeasurePrecision INTEGER, Price INTEGER, Discount INTEGER, Quantity INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Slips");
        db.execSQL("DROP TABLE IF EXISTS Products");
        onCreate(db);
    }

    public void onDrop() {
        context.deleteDatabase(DATABASE_NAME);
    }
}