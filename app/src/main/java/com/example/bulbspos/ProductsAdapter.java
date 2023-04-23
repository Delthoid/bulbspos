package com.example.bulbspos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private ArrayList<Product> products = new ArrayList<>();
    private static boolean displayOnly;

    public final class ViewHolder extends RecyclerView.ViewHolder {
        int position;
        private Product product;
        private final TextView nameLabel;
        private final TextView descriptionLabel;
        private final TextView priceLabel;
        private final TextView stocksLabel;
        private final ImageButton editButton;
        private CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameLabel = (TextView) itemView.findViewById(R.id.product_name_label_grid);
            descriptionLabel = (TextView) itemView.findViewById(R.id.product_description_label);
            priceLabel = (TextView) itemView.findViewById(R.id.product_price_label);
            stocksLabel = (TextView) itemView.findViewById(R.id.product_stocks_label);
            card = (CardView) itemView.findViewById(R.id.product_item_card);
            editButton = (ImageButton) itemView.findViewById(R.id.product_edit_button);

            editButton.setVisibility(displayOnly ? View.VISIBLE : View.GONE);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!displayOnly) {
                        Cart.addItem(product);
                        Snackbar.make(view, product.name + " added to cart", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            if (displayOnly) {
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Update Quantity");

                        final EditText input = new EditText(view.getContext());
                        input.setInputType(InputType.TYPE_CLASS_DATETIME);
                        builder.setView(input);

                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ProductsDbHelper dbHelper = new ProductsDbHelper(view.getContext());
                                product.stocks = Double.parseDouble(input.getText().toString());
                                dbHelper.updateProductStocks(product);

                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, products.size());
                                Snackbar.make(view, product.name + " stocks updated successfully", Snackbar.LENGTH_SHORT).show();
                            }
                        });

                        builder.show();
                    }
                });
            }
        }

        public TextView getNameLabel() {
            return nameLabel;
        }

        public TextView getDescriptionLabel() {
            return descriptionLabel;
        }

        public TextView getPriceLabel() {
            return priceLabel;
        }

        public TextView getStocksLabel() {
            return stocksLabel;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    public ProductsAdapter(ArrayList<Product> products) {
        this.products = products;
        this.displayOnly = false;
    }

    public ProductsAdapter(ArrayList<Product> products, boolean displayOnly) {
        this.products = products;
        this.displayOnly = displayOnly;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.nameLabel.setText(product.name);

        if (product.description.isEmpty()) {
            holder.descriptionLabel.setVisibility(View.GONE);
        } else {
            holder.descriptionLabel.setText(product.description);
        }

        holder.priceLabel.setText(String.valueOf(product.price));
        holder.stocksLabel.setText(String.valueOf(product.stocks));
        holder.setProduct(product);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
