package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminHome extends AppCompatActivity {
    Button logoutbtn, addnewItembtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_1);
        logoutbtn = findViewById(R.id.button_AdminLogout);
        addnewItembtn = findViewById(R.id.button_add_new_item);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        addnewItembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewitem();
            }
        });
    }

    private void addnewitem() {
        startActivity(new Intent(AdminHome.this,AdminCategory.class));

    }

    private void logout() {
        Paper.book().destroy();
        Toast.makeText(AdminHome.this,"Logged out Successfully",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AdminHome.this,MainActivity.class));
        finish();
    }
}
