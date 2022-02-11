package com.example.lab_1_2_kalaichau_c0837213_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lab_1_2_kalaichau_c0837213_android.model.Product;
import com.example.lab_1_2_kalaichau_c0837213_android.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProdcutAdapter extends ArrayAdapter {

    Context context;
    int layoutRes;
    List<Product> productList;
    DatabaseHelper dbHelper;

    public ProdcutAdapter(@NonNull Context context, int resource, @NonNull List<Product> productList, DatabaseHelper db) {
        super(context, resource, productList);
        this.context = context;
        this.productList = productList;
        this.layoutRes = resource;
        this.dbHelper = db;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        if (v == null) v = inflater.inflate(layoutRes, null);
        TextView nameTV = v.findViewById(R.id.tv_name);
        TextView descTV = v.findViewById(R.id.tv_desc);
        TextView priceTV = v.findViewById(R.id.tv_price);
        TextView latTV = v.findViewById(R.id.tv_lat);
        TextView lonTV = v.findViewById(R.id.tv_lon);

        final Product product = productList.get(position);
        nameTV.setText(product.getName());
        descTV.setText(product.getDesc());
        priceTV.setText(String.valueOf(product.getPrice()));
        latTV.setText(String.valueOf(product.getLat()));
        lonTV.setText(String.valueOf(product.getLon()));

        v.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateProduct(product); }

            private void updateProduct(final Product product) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.edit_product_dialog, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText etName = view.findViewById(R.id.et_name);
                final EditText etDesc = view.findViewById(R.id.et_desc);
                final EditText etPrice = view.findViewById(R.id.et_price);
                final EditText etLat = view.findViewById(R.id.et_lat);
                final EditText etLon = view.findViewById(R.id.et_lon);

                etName.setText(product.getName());
                etDesc.setText(String.valueOf(product.getDesc()));
                etPrice.setText(String.valueOf(product.getPrice()));
                etLat.setText(String.valueOf(product.getLat()));
                etLon.setText(String.valueOf(product.getLon()));

                view.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

                        if (lat.isEmpty()) {
                            etLat.setError("Latitude cannot be empty or character");
                            etLat.requestFocus();
                            return;
                        }
                        if (lon.isEmpty()) {
                            etLon.setError("Longtitude cannot be empty or character");
                            etLon.requestFocus();
                            return;
                        }

                        if (dbHelper.updateProduct(product.getId(), name, desc,
                                Double.parseDouble(price),
                                Double.parseDouble(lat),
                                Double.parseDouble(lon))){
                            alertDialog.dismiss();
                            mapCursorToProduct();
                        }
                    }
                });
            }
        });

        v.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(product);
            }

            private void deleteProduct(final Product product) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dbHelper.deleteProduct(product.getId())){
                            dialog.dismiss();
                            mapCursorToProduct();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(context, "The product (" + product.getName() + ") is not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        v.findViewById(R.id.map_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity2.class);
                intent.putExtra("lat", product.getLat());
                intent.putExtra("lon", product.getLon());
                context.startActivity(intent);
            }
        });
        return v;
    }

    private void mapCursorToProduct() {
        Cursor cursor = dbHelper.getAllProducts();
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
        notifyDataSetChanged();
    }
}
