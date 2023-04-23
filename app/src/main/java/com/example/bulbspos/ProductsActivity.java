package com.example.bulbspos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView productsRecyclerView;
    private ProductsDbHelper dbHelper;
    private ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        dbHelper = new ProductsDbHelper(this);
        adapter = new ProductsAdapter(dbHelper.getProducts(""), true);

        toolbar = (Toolbar) findViewById(R.id.products_toolbar);
        fab = (FloatingActionButton) findViewById(R.id.add_new_product_fab);
        productsRecyclerView = (RecyclerView) findViewById(R.id.products_recycler_view);

        productsRecyclerView.setAdapter(adapter);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewProduct.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = new ProductsDbHelper(this);
        adapter = new ProductsAdapter(dbHelper.getProducts(""), true);
        productsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}