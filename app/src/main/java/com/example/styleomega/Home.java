package com.example.styleomega;

import android.content.Intent;
import android.os.Bundle;

import com.example.styleomega.Model.Products;
import com.example.styleomega.Prevalent.Prevalent;
import com.example.styleomega.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference ProductsReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(this);
        ProductsReference= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);






        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cart, R.id.nav_orders, R.id.nav_categories,
                R.id.nav_settings, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();


//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);


        //getting the navigation bar and setting the username and profile picture
        View header=navigationView.getHeaderView(0);
        TextView userNameTextView=header.findViewById(R.id.User_profile_name);
        CircleImageView profileImageView=header.findViewById(R.id.User_profile_image);
        userNameTextView.setText(Prevalent.currentUser.getName());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if(id==R.id.action_settings)
//        {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //1)used for adding the querey that retrieves the data from database
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsReference,Products.class).build();
        //2) creating the recycler adapter
       FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

               holder.txtproductName.setText(model.getProductName());
               holder.txtProductDescription.setText(model.getDescription());
               holder.txtProductPrice.setText("$"+model.getPrice());
               Picasso.get().load(model.getImage()).into(holder.imageView); //converting the link to image
           }

           @NonNull
           @Override
           public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items,parent,false);
               ProductViewHolder holder=new ProductViewHolder(view);
               return holder;
           }
       };

       recyclerView.setAdapter(adapter);
       adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
         int id = menuItem.getItemId();

        if (id == R.id.nav_cart) {
//            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
//            startActivity(intent);

//        } else if (id == R.id.nav_search) {

//            Intent intent = new Intent(HomeActivity.this, SearchProductActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_categories) {

//            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(Home.this, Settings.class);
            startActivity(intent);

//        } else if (id == R.id.contact_us) {

//            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            Paper.book().destroy();
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //this will make sure you cant back out again
            startActivity(intent);
            Toast.makeText(Home.this,"Logged out Successfully",Toast.LENGTH_SHORT).show();
            finish();

        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
