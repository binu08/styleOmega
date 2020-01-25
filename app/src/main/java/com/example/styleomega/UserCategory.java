package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class UserCategory extends AppCompatActivity {
    ImageView shirt,tShirt,top,trouser,jean,shortTrouser,dress,swimwear,wearable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);

        shirt=findViewById(R.id.imageView_Shirts_user_catergory);
        tShirt=findViewById(R.id.imageView_Tshirts_user_catergory);
        top=findViewById(R.id.imageView_Tops_user_catergory);
        trouser=findViewById(R.id.imageView_Trousers_user_catergory);
        jean=findViewById(R.id.imageView_Jeans_user_catergory);
        shortTrouser=findViewById(R.id.imageView_Shorts_user_catergory);
        dress=findViewById(R.id.imageView_Dresses_user_catergory);
        swimwear=findViewById(R.id.imageView_Swimwear_user_catergory);
        wearable=findViewById(R.id.imageView_Wearables_user_catergory);

        shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","Shirts");
                startActivity(intent);
            }
        });

        tShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","T-shirts");
                startActivity(intent);
            }
        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","Tops");
                startActivity(intent);
            }
        });

        trouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","Trousers");
                startActivity(intent);
            }
        });

        jean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","Jeans");
                startActivity(intent);
            }
        });

        shortTrouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","Shorts");
                startActivity(intent);
            }
        });

        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","Dresses");
                startActivity(intent);
            }
        });

        swimwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","Swimwear");
                startActivity(intent);
            }
        });

        wearable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserCategory.this,ViewCategory.class);
                intent.putExtra("Category","Wearables");
                startActivity(intent);
            }
        });
    }
}
