package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminAddNewProduct extends AppCompatActivity {
    Button logoutbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        logoutbtn=findViewById(R.id.button_AdminLogout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    private void logout() {
        Paper.book().destroy(); //to logout from admin
        startActivity(new Intent(AdminAddNewProduct.this,MainActivity.class));
        Toast.makeText(AdminAddNewProduct.this,"LogOut Successful",Toast.LENGTH_SHORT).show();
        finish();
    }
}
