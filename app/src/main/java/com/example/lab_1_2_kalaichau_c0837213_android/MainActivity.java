package com.example.lab_1_2_kalaichau_c0837213_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab_1_2_kalaichau_c0837213_android.model.Product;
import com.example.lab_1_2_kalaichau_c0837213_android.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper dbHelper;
    List<Product> productList;
    ListView prod_lv;
    SearchView sv;
    Button add_btn;
    private static final String tag = "svsvsvsvsv";
    private static final String DATABASE_NAME = "product_database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prod_lv = findViewById(R.id.lv_product);
        sv = findViewById(R.id.sv);
        add_btn = findViewById(R.id.add_btn);

        sqLiteDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getAllProducts();
        mapCursorToProduct(cursor);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Cursor cursor = dbHelper.getProductByName(s);
                if (cursor.moveToFirst()){
                    mapCursorToProduct(cursor);
                }else{
                    Toast.makeText(getApplicationContext(),
                    "No records found",Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Cursor cursor = dbHelper.getAllProducts();
                mapCursorToProduct(cursor);
                return false;
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View add_dialog = layoutInflater.inflate(R.layout.add_product_dialog, null);
                builder.setView(add_dialog);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button btn = add_dialog.findViewById(R.id.create_btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText etName = add_dialog.findViewById(R.id.et_name1);
                        final EditText etDesc = add_dialog.findViewById(R.id.et_desc1);
                        final EditText etPrice = add_dialog.findViewById(R.id.et_price1);
                        final EditText etLat = add_dialog.findViewById(R.id.et_lat1);
                        final EditText etLon = add_dialog.findViewById(R.id.et_lon1);

                        String name = etName.getText().toString();
                        String desc = etDesc.getText().toString();
                        String price = etPrice.getText().toString();
                        String lat = etLat.getText().toString();
                        String lon = etLon.getText().toString();

                        if (name.isEmpty()) {
                            etName.setError("name field cannot be empty");
                            etName.requestFocus();
                            return;
                        }

                        if (desc.isEmpty()) {
                            etDesc.setError("Description cannot be empty");
                            etDesc.requestFocus();
                            return;
                        }

                        if (price.isEmpty()) {
                            etPrice.setError("Price cannot be empty or character");
                            etPrice.requestFocus();
                            return;
                        }

                        if (price.isEmpty()) {
                            etLat.setError("Latitude cannot be empty or character");
                            etLat.requestFocus();
                            return;
                        }
                        if (price.isEmpty()) {
                            etLon.setError("Longtitude cannot be empty or character");
                            etLon.requestFocus();
                            return;
                        }

                        if (dbHelper.addProduct(name, desc,
                                Double.parseDouble(price),
                                Double.parseDouble(lat),
                                Double.parseDouble(lon))){
                            alertDialog.dismiss();
                            Cursor cursor = dbHelper.getAllProducts();
                            mapCursorToProduct(cursor);
                        }
                    }
                });
            }
        });
    }

    private void mapCursorToProduct(Cursor cursor) {
        productList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // create an employee instance
                productList.add(new Product(
                    cursor.getInt(0),
                    cursor.getString(1), //name
                    cursor.getString(2), //desc
                    cursor.getDouble(3), //price
                    cursor.getDouble(4), //lat
                    cursor.getDouble(5)//lon
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        ProdcutAdapter productAdapter = new ProdcutAdapter(this,
                R.layout.product_list_layout,
                productList,
                dbHelper);
        prod_lv.setAdapter(productAdapter);
    }



}