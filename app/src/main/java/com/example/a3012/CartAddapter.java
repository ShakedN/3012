package com.example.a3012;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3012.fragment.fragmentFour;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import moudles.GroceryItem;

public class CartAddapter extends RecyclerView.Adapter<CartAddapter.CartViewHolder> {
    private  TextView totalTextView;

    private ArrayList<GroceryItem> cartDataset;

    public CartAddapter(ArrayList<GroceryItem> cartDataset,TextView totalTextView) {
        this.cartDataset = cartDataset;
        this.totalTextView=totalTextView;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewCart;
        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewIcon;
        TextView plus;
        TextView minus;
        TextView productAmountTextView;
        TextView total;
        DatabaseReference userRef;
        public CartViewHolder(View itemView) {
            super(itemView);
            cardViewCart = itemView.findViewById(R.id.cartView);
            textViewName = itemView.findViewById(R.id.textView);
            textViewPrice = itemView.findViewById(R.id.price);
            imageViewIcon = itemView.findViewById(R.id.imageView);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minusButton);
            productAmountTextView = itemView.findViewById(R.id.productAmountTextView);
            total=itemView.findViewById(R.id.textViewTotalPrice);
        }
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewcart, parent, false);
        return new CartViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        GroceryItem currentItem = cartDataset.get(position);
        holder.textViewName.setText(currentItem.getName());
        holder.textViewPrice.setText(String.valueOf(currentItem.getPrice()));
        holder.imageViewIcon.setImageResource(currentItem.getImage());
        holder.productAmountTextView.setText(String.valueOf(currentItem.getAmount()));
        holder.plus.setOnClickListener(v -> {
            changeAmount(currentItem, v.getContext(), true); // Increase quantity
        });


        // Handling minus button click
        holder.minus.setOnClickListener(v -> {
            changeAmount(currentItem, v.getContext(), false); // Decrease quantity
        });


    }


    @Override
    public int getItemCount() {
        return cartDataset.size();
    }

    private void changeAmount(final GroceryItem product, final Context context, boolean isIncrease) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(context, "User not authenticated!", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = currentUser.getEmail();
        if (email == null || email.isEmpty()) {
            Toast.makeText(context, "Email not available for the current user!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("user");

        userRef.get().addOnSuccessListener(dataSnapshot -> {
            if (!dataSnapshot.exists()) {
                Toast.makeText(context, "No users found in database!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean userFound = false;
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                String userEmail = userSnapshot.child("email").getValue(String.class);
                if (userEmail != null && userEmail.equalsIgnoreCase(email)) {
                    userFound = true;
                    String phone = userSnapshot.getKey();
                    DatabaseReference cartRef = userRef.child(phone).child("cart");
                    String productId = "product" + product.getId();

                    cartRef.child(productId).get().addOnSuccessListener(snapshot -> {
                        int currentAmount = snapshot.exists() ? snapshot.child("amount").getValue(Integer.class) : 0;
                        int newAmount = isIncrease ? currentAmount + 1 : Math.max(currentAmount - 1, 0);

                        if (newAmount == 0) {
                            // Remove the product from the cart if amount is 0
                            cartRef.child(productId).removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        cartDataset.remove(product); // Update local dataset
                                        notifyDataSetChanged();
                                        updateTotalPrice(userRef.child(phone), context); // Update total price
                                        Toast.makeText(context, "Item removed from cart!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> handleFirebaseError(context, "removing item", e));
                        } else {
                            // Update the amount of the product
                            cartRef.child(productId).child("amount").setValue(newAmount)
                                    .addOnSuccessListener(aVoid -> {
                                        product.setAmount(newAmount); // Update local product's amount
                                        notifyDataSetChanged();
                                        updateTotalPrice(userRef.child(phone), context); // Update total price
                                        Toast.makeText(context, "Quantity updated!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> handleFirebaseError(context, "updating quantity", e));
                        }
                    }).addOnFailureListener(e -> handleFirebaseError(context, "checking product in cart", e));
                    break; // Stop iterating after finding the correct user
                }
            }

            if (!userFound) {
                Toast.makeText(context, "User email not found in database!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> handleFirebaseError(context, "fetching user data", e));
    }


    private void updateTotalPrice(DatabaseReference userRef, Context context) {
        DatabaseReference cartRef = userRef.child("cart");

        cartRef.get().addOnSuccessListener(cartSnapshot -> {
            double totalPrice = 0.0;

            // Calculate total price
            for (DataSnapshot productSnapshot : cartSnapshot.getChildren()) {
                Integer amount = productSnapshot.child("amount").getValue(Integer.class);
                Double price = productSnapshot.child("price").getValue(Double.class);

                if (amount != null && price != null) {
                    totalPrice += amount * price;
                }
            }

            double finalTotalPrice = totalPrice;

            // Update total price in Firebase
            userRef.child("totalPrice").setValue(totalPrice)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DEBUG", "Total price updated in Firebase: " + finalTotalPrice);

                        // Update the TextView for the total price in the UI
                        if (totalTextView != null) {
                            totalTextView.setText(String.format("Total: $%.2f", finalTotalPrice));
                        } else {
                            Log.e("DEBUG", "totalTextView is null");
                        }
                    })
                    .addOnFailureListener(e -> handleFirebaseError(context, "updating total price in Firebase", e));
        }).addOnFailureListener(e -> handleFirebaseError(context, "fetching cart data", e));
    }

    private void handleFirebaseError(Context context, String operation, Exception e) {
        Log.e("DEBUG", "Error " + operation + ": ", e);
        Toast.makeText(context, "Failed to " + operation + "!", Toast.LENGTH_SHORT).show();
    }




    public void calculateTotalPrice(final fragmentFour context, final OnTotalPriceCalculatedListener listener) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("user");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("DEBUG", "User not authenticated!");
            return;
        }

        String email = currentUser.getEmail();
        if (email == null || email.isEmpty()) {
            Log.e("DEBUG", "Email not available for the current user!");
            return;
        }

        final double[] totalPrice = {0.0};

        userRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);
                    if (userEmail != null && userEmail.equalsIgnoreCase(email)) {
                        String phone = userSnapshot.getKey();
                        DatabaseReference cartRef = userRef.child(phone).child("cart");

                        cartRef.get().addOnSuccessListener(cartSnapshot -> {
                            if (cartSnapshot.exists()) {
                                for (DataSnapshot productSnapshot : cartSnapshot.getChildren()) {
                                    GroceryItem product = productSnapshot.getValue(GroceryItem.class);
                                    if (product != null) {
                                        Integer amount = productSnapshot.child("amount").getValue(Integer.class);
                                        if (amount != null) {
                                            totalPrice[0] += product.getPrice() * amount;
                                        }
                                    }
                                }
                                // Once the total price is calculated, notify the listener
                                listener.onTotalPriceCalculated(totalPrice[0]);
                            } else {
                                Log.d("DEBUG", "Cart is empty.");
                            }
                        }).addOnFailureListener(e -> {
                            Log.e("DEBUG", "Failed to fetch cart data", e);
                        });

                        break;
                    }
                }
            } else {
                Log.e("DEBUG", "No users found in database!");
            }
        }).addOnFailureListener(e -> {
            Log.e("DEBUG", "Failed to fetch user data", e);
        });
    }

    public interface OnTotalPriceCalculatedListener {
        void onTotalPriceCalculated(double totalPrice);
    }




}
