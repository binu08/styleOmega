package com.example.styleomega.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.styleomega.Interface.itemClickListner;
import com.example.styleomega.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtproductName, txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public itemClickListner listener; // this is used when clicking on the one post

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=(ImageView) itemView.findViewById(R.id.product_items_product_image);
        txtproductName=(TextView) itemView.findViewById(R.id.product_items_product_name);
        txtProductDescription=(TextView) itemView.findViewById(R.id.product_items_product_description);
        txtProductPrice=(TextView) itemView.findViewById(R.id.product_items_product_price);

    }

    @Override
    public void onClick(View v) {
 listener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClicklistner(itemClickListner listner)
    {
        this.listener=listner;
    }
}
