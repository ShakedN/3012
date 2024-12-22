package com.example.a3012.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a3012.CustomeAdapter;
import com.example.a3012.R;

import java.util.ArrayList;

import moudles.GroceryItem;

public class fragmenthree extends Fragment {

    private ArrayList<GroceryItem> dataset;
    private RecyclerView recyclerView;
    private CustomeAdapter adapter;
    private SearchView searchView;

    public fragmenthree() {
        // Required empty public constructor
    }
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_EMAIL = "email";

    private String mParam1;
    private String mParam2;
    private String getArgEmail;







    public static fragmenthree newInstance(String param1, String param2,String email) {
        fragmenthree fragment = new fragmenthree();
        Bundle args = new Bundle();
         args.putString(ARG_EMAIL, email);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragmentthree, container, false);
        String email = getArguments().getString("email");
                     String usernamePart = email.split("@")[0];
                // You can now use the email in your fragment logic// Initialize UI components
     //   Log.d("FragmentThree", "Email passed: " + email);
        recyclerView = view.findViewById(R.id.recyclerViewResult);
        searchView = view.findViewById(R.id.searchView);
        TextView clientName = view.findViewById(R.id.clientName);
        clientName.setText("Welcome " + usernamePart);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            getArgEmail= getArguments().getString(ARG_EMAIL);
        }
        // Prepare dataset
        dataset = new ArrayList<>();
        for (int i = 0; i < Data.nameArray.length; i++) {
            dataset.add(new GroceryItem(
                    Data.nameArray[i],
                    Data.descriptionArray[i],
                    Data.drawableArray[i],
                    Data.id_[i]
            ));
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomeAdapter(dataset);
        recyclerView.setAdapter(adapter);

        // Set up SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        // Search Icon click functionality
        // Search Icon click functionality
        ImageView searchIcon = view.findViewById(R.id.searchIcon);

        // Show the SearchView when search icon is clicked
        searchIcon.setOnClickListener(v -> {
            // Make the SearchView visible
            searchView.setVisibility(View.VISIBLE);
            searchView.requestFocus();
        });

        return view;
    }
}
