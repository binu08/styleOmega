package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategory extends AppCompatActivity {

    ImageView shirt,tShirt,top,trouser,jean,shortTrouser,dress,swimwear,wearable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        shirt=findViewById(R.id.imageView_Shirts);
        tShirt=findViewById(R.id.imageView_Tshirts);
        top=findViewById(R.id.imageView_Tops);
        trouser=findViewById(R.id.imageView_Trousers);
        jean=findViewById(R.id.imageView_Jeans);
        shortTrouser=findViewById(R.id.imageView_Shorts);
        dress=findViewById(R.id.imageView_Dresses);
        swimwear=findViewById(R.id.imageView_Swimwear);
        wearable=findViewById(R.id.imageView_Wearables);

        shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","Shirts");
                startActivity(intent);
            }
        });

        tShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","T-shirts");
                startActivity(intent);
            }
        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","Tops");
                startActivity(intent);
            }
        });

        trouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","Trousers");
                startActivity(intent);
            }
        });

        jean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","Jeans");
                startActivity(intent);
            }
        });

        shortTrouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","Shorts");
                startActivity(intent);
            }
        });

        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","Dresses");
                startActivity(intent);
            }
        });

        swimwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","Swimwear");
                startActivity(intent);
            }
        });

        wearable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminAddNewItem.class);
                intent.putExtra("Category","Wearables");
                startActivity(intent);
            }
        });
    }
}
