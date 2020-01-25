package com.example.styleomega.ViewHolder;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.styleomega.Interface.itemClickListner;
import com.example.styleomega.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName,txtProductPrice,txtProductQuantity,txtSize;

    public itemClickListner listener; // this is used when clicking on the one item

    public CartViewHolder(@NonNull View itemView) {


        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtSize = itemView.findViewById(R.id.cart_product_size);
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListener(itemClickListner listener) {
        this.listener = listener;
    }
}
