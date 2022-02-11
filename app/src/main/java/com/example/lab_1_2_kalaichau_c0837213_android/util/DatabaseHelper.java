package com.example.lab_1_2_kalaichau_c0837213_android.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.AnyRes;

import com.example.lab_1_2_kalaichau_c0837213_android.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "product_database";

    private static final String TABLE_NAME = "product";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_lat = "prod_lat";
    private static final String COLUMN_long = "prod_long";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArrayList<Product> list = new ArrayList();
        list.add(new Product(0, "Apple AirPods (2nd Generation)", "Universal fit that’s comfortable all day",
                149.99, 19.8838774, 110.4153075));
        list.add(new Product(0, "Snack Sized Assorted Candy", "Great for sharing in candy bowls, birthday candy, lunches, or entertaining; Also delicious to enjoy yourself",
                15.16, 3.3393833, -73.6300729));
        list.add(new Product(0, "Wireless Earbuds", "Battery Champ True Wireless Earbuds for both phone calls, music, and video", 59.49,
                59.4310564, 26.2664249));
        list.add(new Product(0, "TV Stick", "Our most powerful streaming stick - 40% more powerful than Fire TV Stick 4K, with faster app starts and more fluid navigation",
                64.99, 27.91072, 104.540674));
        list.add(new Product(0, "Wireless Mouse", "Illuminating wireless mouse with RGB 7-color LED backlight changeable by auto breathing",
                15.29, 43.6167837, 7.0125073));
        list.add(new Product(0, "Kindle", "Everything in the all-new Kindle Paperwhite, plus wireless charging, auto-adjusting front light, and 32 GB storage",
                169.99, -6.6845518, 111.4365318));
        list.add(new Product(0, "Bose SoundLink", "The best-performing portable speaker from Bose; delivers deep, loud, jaw-dropping sound with true 360-degree coverage",
                229.00, 30.210884, 66.048012));
        list.add(new Product(0, "USB C to USB Adapter", "Adapter for USB C to USB",
                12.39, 41.9164917, 20.2862003));
        list.add(new Product(0, "Eye Mask with Heat", "Reduce Eye Strain & Improve Sleeping",
                69.99, 40.052684, 20.1581282));
        list.add(new Product(0, "Water Shoes", "Comfortable & Breathable: High quality flexible fabrics uppers are skin-friendly and breathable",
                25.49, 43.4968733, -1.4703339));

        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER NOT NULL CONSTRAINT product_pk PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " VARCHAR(255) NOT NULL, " +
                COLUMN_DESC + " TEXT NOT NULL, " +
                COLUMN_PRICE + " REAL NOT NULL, " +
                COLUMN_lat + " REAL NOT NULL, " +
                COLUMN_long + " REAL NOT NULL);";
        db.execSQL(sql);

        for (Product prod : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME, prod.getName());
            contentValues.put(COLUMN_DESC, prod.getDesc());
            contentValues.put(COLUMN_PRICE, prod.getPrice());
            contentValues.put(COLUMN_lat, prod.getLat());
            contentValues.put(COLUMN_long, prod.getLon());

            db.insert(TABLE_NAME, null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    //retrieve all products
    public Cursor getAllProducts() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    //retrieve specific product
    public Cursor getProductByName(String keyword) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.query(true,
                TABLE_NAME, null,
                COLUMN_NAME + " LIKE ?" + " OR " + COLUMN_DESC + " LIKE ?",
                 new String[] {"%"+ keyword+ "%", "%"+ keyword+ "%"},
                null, null, null, null);
    }

    //add new product
    //insert will return -1, if transaction failed
    public boolean addProduct(String name, String desc, double price, double lat, double lon) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DESC, desc);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_lat, lat);
        contentValues.put(COLUMN_long, lon);

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) != -1;
    }

    //update certain product
    //update action return number of affected row
    public boolean updateProduct(int id, String name, String desc, double price, double lat, double lon) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DESC, desc);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_lat, lat);
        contentValues.put(COLUMN_long, lon);

        return sqLiteDatabase.update(TABLE_NAME,
                contentValues,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    //delete product
    //delete action return number of affected row
    public boolean deleteProduct(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}) > 0;
    }
}
