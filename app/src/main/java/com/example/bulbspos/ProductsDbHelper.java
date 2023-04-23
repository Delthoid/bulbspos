package com.example.bulbspos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.Debug;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProductsDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " (" +
                    ProductContract.ProductEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    ProductContract.ProductEntry.COLUMN_NAME_NAME + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_NAME_PRICE + " REAL," +
                    ProductContract.ProductEntry.COLUMN_NAME_STOCK + " REAL)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ProductContract.ProductEntry.TABLE_NAME;

    public ProductsDbHelper(Context context) {
        super(context, ProductContract.ProductEntry.TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    //Methods for CRUD operations
    public void addNewProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.COLUMN_NAME_NAME, product.name);
            values.put(ProductContract.ProductEntry.COLUMN_NAME_DESCRIPTION, product.description);
            values.put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, product.price);
            values.put(ProductContract.ProductEntry.COLUMN_NAME_STOCK, product.stocks);

            long newRowId = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
            Log.d("New row ID", String.valueOf(newRowId));
            db.close();

        } catch (Exception e) {
            Log.d("Error on adding product", e.toString());
        }
    }

    //Return products from database as ArrayList
    public ArrayList<Product> getProducts(String searchQuery) {
        final ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + ProductContract.ProductEntry.TABLE_NAME;

        if (searchQuery.isEmpty() == false) {
            query = "SELECT * FROM " + ProductContract.ProductEntry.TABLE_NAME + " WHERE " + ProductContract.ProductEntry.COLUMN_NAME_NAME + " LIKE '%' || ? || '%'";
        }

        Cursor cursor = db.rawQuery(query, searchQuery.isEmpty() ? null : new String[]{searchQuery});

        while (cursor.moveToNext()) {
            final Product productItem = new Product();
            productItem.id = cursor.getLong(0);
            productItem.name = cursor.getString(1);
            productItem.description = cursor.getString(2);
            productItem.price = cursor.getDouble(3);
            productItem.stocks = cursor.getDouble(4);
            products.add(productItem);
        }

        for (Product i:
             products) {
            Log.d("Product", i.name);
        }
        cursor.close();
        db.close();
        return products;
    }

    //Update stocks
    public void updateStocks(ArrayList<CartItem> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (CartItem cartItem : cartItems) {
            final double newStock = cartItem.product.stocks - cartItem.qty;

            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.COLUMN_NAME_STOCK, newStock);

            db.update(ProductContract.ProductEntry.TABLE_NAME, values, "id = ?", new String[] {String.valueOf(cartItem.product.id)});
        }
        db.close();
    }

    //Update single item stock
    public void updateProductStocks(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME_STOCK, product.stocks);
        db.update(ProductContract.ProductEntry.TABLE_NAME, values, "id = ?", new String[]{String.valueOf(product.id)});
        db.close();
    }
}
