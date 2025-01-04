package com.example.a3012;

import android.content.Context;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
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
            textViewPrice = itemView.findViewById(R.id.price);
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

        String email = mAuth.getCurrentUser().getEmail(); // Get current user's email
        if (email == null || email.isEmpty()) {
            Toast.makeText(context, "Email not available for the current user!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("user");

        // Query the database to find the phone number based on the email
        userRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                boolean found = false;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);
                    if (userEmail != null && userEmail.equalsIgnoreCase(email)) {
                        found = true;

                        // Get the phone number (key of the node)
                        String phone = userSnapshot.getKey();
                        Log.d("DEBUG", "Phone number found: " + phone);

                        // Reference the user's cart
                        DatabaseReference cartRef = userRef.child(phone).child("cart");

                        // Generate a unique ID for the product to avoid overwriting
                        String productId = "product" + product.getId();

                        // Check if the product already exists in the cart
                        cartRef.child(productId).get().addOnSuccessListener(snapshot -> {
                            if (snapshot.exists()) {
                                // Product already in cart
                                Toast.makeText(context, "Product is already in your cart!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Add the product to the cart
                                cartRef.child(productId).setValue(product)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(context, "Product added to cart!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("DEBUG", "Error adding product to cart", e);
                                            Toast.makeText(context, "Failed to add product to cart!", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }).addOnFailureListener(e -> {
                            Log.e("DEBUG", "Error checking product in cart", e);
                            Toast.makeText(context, "Failed to check if product is in cart!", Toast.LENGTH_SHORT).show();
                        });

                        break; // Break out of the loop once the user is found and product is processed
                    }
                }

                if (!found) {
                    Toast.makeText(context, "User email not found in database!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "No users found in database!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("DEBUG", "Error fetching user data", e);
            Toast.makeText(context, "Failed to fetch user data!", Toast.LENGTH_SHORT).show();
        });
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
