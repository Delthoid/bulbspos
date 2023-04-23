package com.example.bulbspos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class Cart extends RecyclerView.Adapter<Cart.ViewHolder> {

    private boolean showItemButtons = true;
    public static HashMap<Long, CartItem> cartItems = new HashMap<Long, CartItem>();

    public static void addItem(Product product) {
        //Check if item is already exist on the list
        if (cartItems.containsKey(product.id)) {
            CartItem existing = cartItems.get(product.id);
            cartItems.put(product.id, new CartItem(existing.product, existing.qty + 1));
        } else {
            cartItems.put(product.id, new CartItem(product, 1));
        }
    }

    public static void updateItem(CartItem item, double qty) {
        final CartItem updatedItem = item;
        updatedItem.qty = qty;
        cartItems.put(item.product.id, updatedItem);
    }

    public static void clear() {
        cartItems.clear();
    }

    public static void removeItem(CartItem item) {
        cartItems.remove(item.product.id);
    }

    public static double getTotal() {
        double total = 0.0;

        for (CartItem cartitem : cartItems.values()) {
            total += cartitem.getTotal();
        }
        return total;
    }

    public static ArrayList<CartItem> getCartItemsAsArrayList() {
        final ArrayList<CartItem> items = new ArrayList<>();
        for (CartItem item: cartItems.values()) {
            items.add(item);
        }

        return items;
    }

    public Cart() {

    }

    public Cart(boolean showItemButtons) {
        this.showItemButtons = showItemButtons;
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        int position;
        private CartItem item;
        private final TextView nameLabel;
        private final TextView priceLabel;
        private final TextView qtyLabel;
        private final TextView totalLabel;
        private final ImageButton removeBtn;
        private final ImageButton editBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameLabel = (TextView) itemView.findViewById(R.id.cart_product_name_label);
            priceLabel = (TextView) itemView.findViewById(R.id.cart_product_price_label);
            qtyLabel = (TextView) itemView.findViewById(R.id.cart_product_qty_label);
            totalLabel = (TextView) itemView.findViewById(R.id.cart_item_total_label);
            removeBtn = (ImageButton) itemView.findViewById(R.id.cart_item_remove);
            editBtn = (ImageButton) itemView.findViewById(R.id.cart_item_edit);

            removeBtn.setVisibility(showItemButtons ? View.VISIBLE : View.GONE);
            editBtn.setVisibility(showItemButtons ? View.VISIBLE : View.GONE);

            if (showItemButtons) {
                //Remove item HAHAHA tangina
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, item.product.name + " removed from cart", Snackbar.LENGTH_SHORT).show();

                        removeItem(item);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItems.size());
                    }
                });

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Edit Quantity");

                        final EditText input = new EditText(view.getContext());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);

                        input.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                final double newQty = Double.parseDouble(input.getText().toString());
                                if (newQty > item.product.stocks) {
                                    input.setError("Opps andami mong order AHHAHAAHA, onti na lang stocks eh. Bobo ka ba?");
                                }
                            }
                        });

                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final double newQty = Double.parseDouble(input.getText().toString());
                                updateItem(item, newQty);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, cartItems.size());
                            }
                        });

                        builder.show();
                    }
                });
            }
        }

        public TextView getPriceLabel() {
            return priceLabel;
        }

        public TextView getNameLabel() {
            return nameLabel;
        }

        public TextView getQtyLabel() {
            return qtyLabel;
        }

        public TextView getTotalLabel() {
            return totalLabel;
        }

        public void setItem(CartItem item) {
            this.item = item;
        }
    }

    @NonNull
    @Override
    public Cart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new Cart.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cart.ViewHolder holder, int position) {
        final CartItem item = cartItems.get(cartItems.keySet().toArray()[position]);
        holder.nameLabel.setText(item.product.name);
        holder.priceLabel.setText(String.valueOf(item.product.price));
        holder.qtyLabel.setText(String.valueOf(item.qty));
        holder.totalLabel.setText("₱" + String.valueOf(item.getTotal()));
        holder.setItem(item);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
