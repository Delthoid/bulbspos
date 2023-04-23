package com.example.bulbspos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class NewProduct extends AppCompatActivity {

    private ProductsDbHelper productsDbHelper;

    private Toolbar toolbar;
    private FloatingActionButton saveNewProductFab;

    private EditText nameField, descriptionField, priceField, stockField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        productsDbHelper = new ProductsDbHelper(this);

        toolbar = (Toolbar) findViewById(R.id.new_product_tool_bar);
        saveNewProductFab = (FloatingActionButton) findViewById(R.id.save_new_product_fab);
        nameField = (EditText) findViewById(R.id.productNameField);
        descriptionField = (EditText) findViewById(R.id.productDescriptionField);
        priceField = (EditText) findViewById(R.id.productPriceField);
        stockField = (EditText) findViewById(R.id.productStockField);

        saveNewProductFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct(view);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Product");
    }

    private void saveProduct(View view) {
        //Required fields are supplied
        if (validateForm()) {
            Product newProduct = new Product();
            newProduct.name = nameField.getText().toString();
            newProduct.description = descriptionField.getText().toString().isEmpty() ? "" : descriptionField.getText().toString();
            newProduct.price = Double.parseDouble(priceField.getText().toString());
            newProduct.stocks = Double.parseDouble(stockField.getText().toString());

            productsDbHelper.addNewProduct(newProduct);

            Toast.makeText(view.getContext(), "Product saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Snackbar.make(view, "Please", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        Log.d("Name", nameField.getText().toString());
        if (nameField.getText().toString().isEmpty() || priceField.getText().toString().isEmpty() || stockField.getText().toString().isEmpty()) {

            if (nameField.getText().toString().isEmpty()) {
                nameField.setError("Please enter new product name");
            }

            if (priceField.getText().toString().isEmpty()) {
                priceField.setError("Please enter new product price");
            }

            if (stockField.getText().toString().isEmpty()) {
                stockField.setError("Please enter new product stocks");
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        ProductsDbHelper dbHelper = new ProductsDbHelper(this);
        dbHelper.getProducts("");
        super.onStart();
    }
}