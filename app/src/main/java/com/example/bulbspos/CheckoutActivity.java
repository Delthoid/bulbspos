package com.example.bulbspos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CheckoutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView cartItemsRecyclerView;
    private Cart cartAdapter;

    //Totals
    private EditText totalField, paymentField, changeField;
    private ExtendedFloatingActionButton fab;
    private ProductsDbHelper productsDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        productsDbHelper = new ProductsDbHelper(this);

        toolbar = (Toolbar) findViewById(R.id.checkout_toolbar);
        cartItemsRecyclerView = (RecyclerView) findViewById(R.id.cart_items_recycler_view);

        totalField = (EditText) findViewById(R.id.total_field);
        paymentField = (EditText) findViewById(R.id.payment_field);
        changeField = (EditText) findViewById(R.id.change_field);
        fab = (ExtendedFloatingActionButton) findViewById(R.id.accept_payment_fab);

        totalField.setText(Cart.getTotal() + "");
        paymentField.setText(Cart.getTotal() + "");

        cartAdapter = new Cart(false);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");

        calculate();

        paymentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculate();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Payment Accepted", Toast.LENGTH_LONG).show();

                productsDbHelper.updateStocks(Cart.getCartItemsAsArrayList());

                Cart.clear();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(intent);
            }
        });
    }

    void calculate() {
        if (paymentField.getText().toString().isEmpty() == false) {
            final double paymentAmount = Double.parseDouble(paymentField.getText().toString());
            final double totalAmount = Cart.getTotal();
            final double change = paymentAmount - totalAmount;
            changeField.setText(change + "");

            fab.setEnabled(paymentAmount >= totalAmount);
        }
    }
}