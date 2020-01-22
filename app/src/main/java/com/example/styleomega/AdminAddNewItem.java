package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminAddNewItem extends AppCompatActivity {

    private String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_item);
        categoryName=getIntent().getExtras().get("Category").toString();
        Toast.makeText(AdminAddNewItem.this,"Category you selected is "+categoryName,Toast.LENGTH_LONG).show();

    }
}
