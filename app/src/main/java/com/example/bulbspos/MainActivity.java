package com.example.bulbspos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProductsAdapter adapter;
    private ProductsDbHelper dbHelper;
    private RecyclerView productsRecyclerView;
    private EditText editTextSeach;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ProductsDbHelper(this);
        adapter = new ProductsAdapter(dbHelper.getProducts(""));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        productsRecyclerView = (RecyclerView) findViewById(R.id.products_recycler_view);
        editTextSeach = (EditText) findViewById(R.id.pos_search_field);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        productsRecyclerView.setAdapter(adapter);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);

        editTextSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               handleSearchChanged(editable);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), CartActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = new ProductsDbHelper(this);
        adapter = new ProductsAdapter(dbHelper.getProducts(editTextSeach.getText().toString()));
        productsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.products_menu_search_pos).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.products_menu:
                openProductScreen();
                break;
        }
        return true;
    }

    private void handleSearchChanged(Editable editable) {
        onStart();
    }

    private void openProductScreen() {
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }
}