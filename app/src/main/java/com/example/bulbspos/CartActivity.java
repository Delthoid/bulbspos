package com.example.bulbspos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartItemsRecyclerView;
    private ExtendedFloatingActionButton checkoutFab;
    private Toolbar toolbar;
    private Cart cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItemsRecyclerView = (RecyclerView) findViewById(R.id.cart_items_recycler_view);
        checkoutFab = (ExtendedFloatingActionButton) findViewById(R.id.checkout_fab);
        toolbar = (Toolbar) findViewById(R.id.cart_toolbar);

        cartAdapter = new Cart();

        cartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                cartAdapter = new Cart();
                cartItemsRecyclerView.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();
            }
        });

        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");

        checkoutFab.setVisibility(Cart.cartItems.isEmpty() ? View.GONE : View.VISIBLE);
        checkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CheckoutActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cart_menu:
                clear();
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void clear() {
        Cart.clear();
        cartAdapter = new Cart();
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Cart items cleared", Toast.LENGTH_SHORT).show();
        checkoutFab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartAdapter = new Cart();
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }
}