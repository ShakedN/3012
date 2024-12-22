package com.example.a3012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import moudles.GroceryItem;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final ArrayList<GroceryItem> dataset;
    private final ArrayList<GroceryItem> filteredDataset;

    public CustomAdapter(ArrayList<GroceryItem> dataset) {
        this.dataset = new ArrayList<>(dataset);
        this.filteredDataset = new ArrayList<>(dataset);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewRes);
            textViewName = itemView.findViewById(R.id.textView);
            textViewPrice = itemView.findViewById(R.id.textView2);
            imageViewIcon = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        GroceryItem currentItem = filteredDataset.get(position);
        holder.textViewName.setText(currentItem.getName());
        holder.textViewPrice.setText(String.valueOf(currentItem.getPrice()));
        holder.imageViewIcon.setImageResource(currentItem.getImage());

        holder.itemView.findViewById(R.id.addToCartButton).setOnClickListener(v -> {
            addProductToCart(currentItem, v.getContext());
        });

        holder.itemView.setOnClickListener(v -> {
            String message = "Name: " + currentItem.getName() + "\nDescription: " + currentItem.getPrice();
            Toast.makeText(v.getContext(), message, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return filteredDataset.size();
    }

    private void addProductToCart(GroceryItem product, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(context, "User not authenticated!", Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = mAuth.getCurrentUser().getPhoneNumber();
        if (phone == null) {
            Toast.makeText(context, "Phone number not available!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(phone)
                .child("cart");

        String productId = "product" + System.currentTimeMillis();
        myRef.child(productId).setValue(product);

        Toast.makeText(context, "Product added to cart!", Toast.LENGTH_SHORT).show();
    }

    public void filter(String query) {
        filteredDataset.clear();
        if (query == null || query.isEmpty()) {
            filteredDataset.addAll(dataset);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (GroceryItem item : dataset) {
                if (item.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredDataset.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
