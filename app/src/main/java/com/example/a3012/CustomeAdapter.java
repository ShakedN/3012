package com.example.a3012;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import moudles.GroceryItem;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder> {

    private ArrayList<GroceryItem> dataset;
    private ArrayList<GroceryItem> filteredDataset;

    public CustomeAdapter(ArrayList<GroceryItem> dataset){
        this.dataset = new ArrayList<>(dataset);
        this.filteredDataset = dataset;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewIcon;

        public MyViewHolder (View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewRes);
            textViewName = itemView.findViewById(R.id.textView);
            textViewPrice = itemView.findViewById(R.id.textView2);
            imageViewIcon = itemView.findViewById(R.id.imageView);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        GroceryItem clickedItem = filteredDataset.get(position);
                        String message = "Name: " + clickedItem.getName() + "\n" +
                                "Description: " + clickedItem.getPrice();
                        Toast.makeText(v.getContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
    @NonNull
    @Override
    public CustomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter.MyViewHolder holder, int position) {
        TextView textViewName = holder.textViewName;
        TextView textViewPrice = holder.textViewPrice;
        ImageView imageView = holder.imageViewIcon;

        GroceryItem currentItem = filteredDataset.get(position);
        textViewName.setText(currentItem.getName());
        textViewPrice.setText(String.valueOf(currentItem.getPrice()));
        imageView.setImageResource(currentItem.getImage());
    }

    @Override
    public int getItemCount() {
        return filteredDataset.size();
    }


    public void filter(String query) {
        if (query == null || query.isEmpty()) {
            filteredDataset.clear();
            filteredDataset.addAll(dataset);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            filteredDataset.clear();

            for (GroceryItem item : dataset) {
                if (item.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredDataset.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }
}
