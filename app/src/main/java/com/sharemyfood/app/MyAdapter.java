package com.sharemyfood.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private ArrayList<Model> listItems;
    private Context context;
    private OnItemClickListener onItemClickListener;
    public MyAdapter(Context context, ArrayList<Model> listItems) {
        this.context = context;
        this.listItems = listItems;
    }
    //For the Items to be displayed when clicked
    public interface OnItemClickListener {
        void onItemClick(Model model);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model model = listItems.get(position);
        Glide.with(context).load(listItems.get(position).getImageUrl()).into(holder.imageView); //To Load Images in Recycler View

        //When the Images are clicked
        holder.imageView.setOnClickListener(v -> {
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(model);
            }
        });
        holder.itemName.setText(model.getName());
        holder.itemDescription.setText(model.getDescription());
        holder.itemQuantity.setText(model.getQuantity());
        holder.itemExpiryDate.setText(model.getExpiry());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView itemName;
        TextView itemDescription;
        TextView itemQuantity;
        TextView itemExpiryDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemExpiryDate = itemView.findViewById(R.id.itemExpiryDate);
        }
    }
}
