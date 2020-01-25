package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.styleomega.Model.Cart;
import com.example.styleomega.Prevalent.Prevalent;
import com.example.styleomega.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.text.ParseException;

public class cartActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button checkoutButton;
    private TextView textViewTotalAmount;
    private float calculateTotalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        checkoutButton =findViewById(R.id.checkout_button);
        textViewTotalAmount =findViewById(R.id.total_price);


        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
               // textViewTotalAmount.setText(String.valueOf(calculateTotalPrice));
                Intent intent = new Intent(cartActivity.this,ConfirmFinalOrderDetails.class);
                intent.putExtra("TotalPrice",String.valueOf(calculateTotalPrice));
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        //checkOrderStatus();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(Prevalent.currentUser.getPhone()).child("Products"), Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.txtProductQuantity.setText("Quantity : " + model.getQuantity());
                holder.txtProductPrice.setText("Price : " + model.getPrice());
                holder.txtProductName.setText(model.getProductName());
                holder.txtSize.setText("Size : "+model.getSize());

                String value=model.getPrice();
                NumberFormat format = NumberFormat.getCurrencyInstance();
                Number number = null;
                try {
                    number = format.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                float price=Float.valueOf(number.toString());
                float quantity=Float.valueOf(model.getQuantity());
                float productTotalPrice = price*quantity;

                calculateTotalPrice = calculateTotalPrice+ productTotalPrice;
                textViewTotalAmount.setText("Total Price : $"+ calculateTotalPrice);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[] = new CharSequence[]{
                                "Edit", "Delete"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(cartActivity.this);
                        builder.setTitle("Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 0) {
                                    Intent intent = new Intent(cartActivity.this, ProductDetails.class);
                                    intent.putExtra("pID", model.getpID());
                                    startActivity(intent);


                                }
                                if (which == 1) {
                                    cartListRef.child("User View").child(Prevalent.currentUser.getPhone())
                                            .child("Products").child(model.getpID()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(cartActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(cartActivity.this, Home.class));
                                                    }
                                                }
                                            });
                                    cartListRef.child("Admin View").child(Prevalent.currentUser.getPhone())
                                            .child("Products").child(model.getpID()).removeValue();

                                }
                            }
                        });
                        builder.show();
                    }
                });



            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter); //settings the adapter
        adapter.startListening();
    }
}
