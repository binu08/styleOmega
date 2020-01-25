package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.styleomega.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderDetails extends AppCompatActivity {

    private EditText nameOfOrder, phoneNoOfOrder, addressOfOrder, cityOfOrder;
    private Button confirmOrderBtn;
    private String totalPrice = "";
    TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order_details);

        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameOfOrder = (EditText) findViewById(R.id.finalconfirm_name);
        phoneNoOfOrder = (EditText) findViewById(R.id.finalconfirm_phone_number);
        addressOfOrder = (EditText) findViewById(R.id.finalconfirm_shipment_address);
        cityOfOrder = (EditText) findViewById(R.id.shipment_city);
        price=findViewById(R.id.confrim_final_order_textView_price);
        totalPrice = getIntent().getStringExtra("TotalPrice");
        price.setText("Toatl Price of Order: $ "+totalPrice);



        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(nameOfOrder.getText().toString())){
                    nameOfOrder.setError("Enter the name of the receiver");
                }
                else if(TextUtils.isEmpty(phoneNoOfOrder.getText().toString())){
                    phoneNoOfOrder.setError("Enter the phone no of the receiver");
                }
                else if(TextUtils.isEmpty(addressOfOrder.getText().toString())){
                    addressOfOrder.setError("Enter the address of the receiver");
                }
                else if(TextUtils.isEmpty(cityOfOrder.getText().toString())){
                    cityOfOrder.setError("Enter the city of the receiver");

                }
                else{

                    finalConfirm();
                }

            }
        });
    }

    private void finalConfirm() {

        final String saveCurrentDate,saveCurrentTime;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());


        final DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().
                child("Orders")
                .child(Prevalent.currentUser.getPhone());

        final HashMap<String,Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount",totalPrice);
        orderMap.put("name",nameOfOrder.getText().toString());
        orderMap.put("phone",phoneNoOfOrder.getText().toString());
        orderMap.put("address",addressOfOrder.getText().toString());
        orderMap.put("city",cityOfOrder.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("status","Pending Shipment");
        orderReference.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentUser.getPhone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ConfirmFinalOrderDetails.this, "Order has been placed successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmFinalOrderDetails.this,Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK); //this is added so cant go back
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }

            }
        });

    }
}
