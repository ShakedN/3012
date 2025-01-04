package com.example.a3012.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a3012.CartAddapter;
import com.example.a3012.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import moudles.GroceryItem;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentFour#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentFour extends Fragment {


    private String email;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_Email = "email";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<GroceryItem> cartDataset = new ArrayList<>();

    private RecyclerView recyclerView;
    //private LinearLayoutManager layoutManager;
    private CartAddapter cartAdd;
    TextView textviewTotal;
    TextView amounttext;

    public fragmentFour() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFour.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentFour newInstance(String param1, String param2,String email) {
        fragmentFour fragment = new fragmentFour();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_Email, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // קבלת שם המשתמש מתוך ה-Bundle
        if (getArguments() != null) {
            email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, container, false);

       TextView usernameTextView = view.findViewById(R.id.clientName);
        usernameTextView.setText(email+"'s Shopping Cart");

        // Initialize the TextView for total price
        textviewTotal = view.findViewById(R.id.textViewTotalPrice);

        recyclerView = view.findViewById(R.id.cartView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartAdd = new CartAddapter(cartDataset,textviewTotal);
        recyclerView.setAdapter(cartAdd);


        loadCartData();

// Calculate the total price and update the UI
        cartAdd.calculateTotalPrice(this, new CartAddapter.OnTotalPriceCalculatedListener() {
            @Override
            public void onTotalPriceCalculated(double totalPrice) {
                // Update the TextView with the total price
                textviewTotal.setText(String.format("Total: $%.2f", totalPrice));
            }
        });

        return view;
    }


    private void loadCartData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("user");

                usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cartDataset.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                DatabaseReference cartRef = usersRef.child(userSnapshot.getKey()).child("cart");

                                cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                                            GroceryItem product = productSnapshot.getValue(GroceryItem.class);
                                            cartDataset.add(product);
                                        }
                                        cartAdd.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle errors
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });
            }
        }
    }
    public void updateTotalPriceTextView(double totalPrice) {
        TextView totalTextView = getView().findViewById(R.id.textViewTotalPrice); // Adjust the ID to match your layout
        if (totalTextView != null) {
            totalTextView.setText(String.format("Total: $%.2f", totalPrice));
        }
    }

    }




