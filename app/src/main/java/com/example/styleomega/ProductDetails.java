package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.styleomega.Model.Products;
import com.example.styleomega.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProductDetails extends AppCompatActivity {

    private Button addToCartBtn;
    private Button buyNowBtn;
    private ImageView productImage, shareButton;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "";
    private String status = "normal";
    private Spinner sizeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pID");
        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_details);
        productPrice = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_description_details);
        productName = findViewById(R.id.product_name_details);
        sizeSpinner = findViewById(R.id.size_spinner);
        shareButton = findViewById(R.id.share_btn);
        addToCartBtn = findViewById(R.id.add_to_cart_button);
        buyNowBtn = findViewById(R.id.buy_now_button);
        List<String> sizes = new ArrayList<>(Arrays.asList("S", "M", "L", "XL"));
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(ProductDetails.this, android.R.layout.select_dialog_singlechoice, sizes);
        sizeAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sizeSpinner.setAdapter(sizeAdapter);
        getProductDetails(productID);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status.equals("order_placed") || status.equals("order_shipped")) {

                   Toast.makeText(ProductDetails.this, "You can order once again when your last order has been fulfilled", Toast.LENGTH_LONG).show();
                } else {
                    addToCartList();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = productName.getText().toString()+"\n"+productDescription.getText().toString()+"\n$"+productPrice.getText().toString()+"\nat Style Omega. Check it out now!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Product");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,  shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));


            }
        });

    }

    private void getProductDetails(String productID) {
        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");
        productReference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getProductName());
                    productPrice.setText("$" + products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).fit().centerCrop()
                            .into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addToCartList() {

        String CurrentTime, CurrentDate;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        CurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(callForDate.getTime());

        //creating another table
        final DatabaseReference cartListReference = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMapData = new HashMap<>();
        cartMapData.put("pID", productID);
        cartMapData.put("size", sizeSpinner.getSelectedItem().toString());
        cartMapData.put("productName", productName.getText().toString());
        cartMapData.put("price", productPrice.getText().toString());
        cartMapData.put("date", CurrentDate);
        cartMapData.put("time", CurrentTime);
        cartMapData.put("quantity", numberButton.getNumber());
        cartMapData.put("discount", "");

        //creating a new a table called User view and using the phone numbers saving the products under each and every user
        cartListReference.child("User View").child(Prevalent.currentUser
                .getPhone()).child("Products").child(productID)
                .updateChildren(cartMapData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            cartListReference.child("Admin View").child(Prevalent.currentUser
                                    .getPhone()).child("Products").child(productID)
                                    .updateChildren(cartMapData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Toast.makeText(ProductDetails.this, "Product has been added to Cart List", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetails.this, Home.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                        }

                    }
                });
    }


    private void checkOrderStatus() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String shipmentStatus = dataSnapshot.child("status").getValue().toString();


                    if (shipmentStatus.equals("shipped")) {

                        status = "order_shipped";


                    } else if (shipmentStatus.equals("Pending Shipment")) {

                        status = "order_placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderStatus();
    }

}
