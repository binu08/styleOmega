package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.styleomega.Model.Products;
import com.example.styleomega.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ViewCategory extends AppCompatActivity {
    private String categoryName;
    private RecyclerView recyclerViewCategory;
    RecyclerView.LayoutManager layoutManagerCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        categoryName = getIntent().getExtras().get("Category").toString();
        recyclerViewCategory = findViewById(R.id.recycler_menu_user_catergory);
        recyclerViewCategory.setHasFixedSize(true);
        layoutManagerCategory = new LinearLayoutManager(this);
        recyclerViewCategory.setLayoutManager(layoutManagerCategory);
    }


    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("category").equalTo(categoryName),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                        holder.txtproductName.setText(model.getProductName());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).fit().centerCrop().into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ViewCategory.this,ProductDetails.class);
                                intent.putExtra("pID",model.getpID());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerViewCategory.setAdapter(adapter);
        adapter.startListening();
    }
}
