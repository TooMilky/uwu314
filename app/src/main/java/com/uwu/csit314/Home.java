package com.uwu.csit314;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uwu.csit314.Common.Common;
import com.uwu.csit314.Interface.ItemClickListener;
import com.uwu.csit314.Model.Banner;
import com.uwu.csit314.Model.Category;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.uwu.csit314.ViewHolder.MenuViewHolder;
import com.squareup.picasso.Picasso;
//import io.paperdb.Paper;


import java.util.HashMap;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;

    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    CounterFab fab;

    //Slider
    HashMap<String, String> image_list;
    SliderLayout mSlider;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

//    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
//        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if(Common.isConnectedToInternet(getBaseContext()))

                    loadMenu();
//                else {
                    Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
//                }
            }
        });


        //Init FIrebase

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");


        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category, Category.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, final int position, @NonNull Category model) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition, boolean isLongClick) {
//                        Intent foodList = new Intent(Home.this, FoodList.class);
//                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
//                        startActivity(foodList);
                    }

                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent ,false);
                return new MenuViewHolder(itemView);
            }
        };

//        Paper.init(this);

//        fab = (CounterFab) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent cartIntent = new Intent(Home.this, Cart.class);
//                startActivity(cartIntent);
//            }
//        });
//
//        fab.setCount(new Database(this).getCountCart());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name for User
//        View headerView = navigationView.getHeaderView(0);
//        txtFullName = headerView.findViewById(R.id.txtFullName);
//        txtFullName.setText(Common.currentUser.getName());

        //Load Menu
        recycler_menu = findViewById(R.id.recycler_menu);

         layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        recycler_menu.setLayoutManager(new GridLayoutManager(this,2));
//        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recycler_menu.getContext(),
//                R.anim.layout_fall_down);
//        recycler_menu.setLayoutAnimation(controller);

        loadMenu();

//        updateToken(FirebaseInstanceId.getInstance().getToken());

        setupSlider();

    }

    private void setupSlider() {
        mSlider= (SliderLayout)findViewById(R.id.slider);
        image_list = new HashMap<>();

        final DatabaseReference banners = database.getReference("Banner");
        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Banner banner = postSnapShot.getValue(Banner.class);
                    image_list.put(banner.getName()+"@@@"+banner.getId(), banner.getImage());
                }
                for (String key : image_list.keySet())
                {
                    String[] keySplit = key.split("@@@");
                    String nameofFood = keySplit[0];
                    String idOfFood = keySplit[1];

                    //Create Slider
                    final TextSliderView textSliderView = new TextSliderView(getBaseContext());
                    textSliderView
                            .description(nameofFood)
                            .image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit);
//                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                @Override
//                                public void onSliderClick(BaseSliderView slider) {
//                                    Intent intent = new Intent(Home.this, FoodDetail.class);
//                                    intent.putExtras(textSliderView.getBundle());
//                                    startActivity(intent);
//                                }
//                            });
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodId", idOfFood);

                    mSlider.addSlider(textSliderView);
                    banners.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);

    }

//    private void updateToken(String token) {
//        FirebaseDatabase db =FirebaseDatabase.getInstance();
//        DatabaseReference tokens = db.getReference("Tokens");
//        Token data = new Token(token, false);
//        tokens.child(Common.currentUser.getPhone()).setValue(data);
//    }

    private void loadMenu() {
//        adapter.startListening();
//        recycler_menu.setAdapter(adapter);
//        swipeRefreshLayout.setRefreshing(false);
//
//        recycler_menu.getAdapter().notifyDataSetChanged();
//        recycler_menu.scheduleLayoutAnimation();

        final FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder()
                .setQuery(category, Category.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder( MenuViewHolder viewHolder, final int position,  Category model) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition, boolean isLongClick) {
                        //TODO: INDEX BY categoryId
                        Intent serviceList = new Intent(Home.this, FoodList.class);
                        int xd = viewHolder.getAbsoluteAdapterPosition();
//                        Log.d("TAG", "Check category " +adapter.getRef(xd).getKey());
//                        serviceList.putExtra("CategoryId", adapter.getRef(xd).getKey());
                        serviceList.putExtra("CategoryId", "01");
//                        items.get(position)
//                        serviceList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(serviceList);
                    }
                });
            }
            @Override
            public MenuViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(itemView);
            }
        };
        adapter.startListening();
        recycler_menu.setAdapter(adapter);
    }



    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        mSlider.stopAutoCycle();
    }
        /*FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder()
                .setQuery(category, Category.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder( MenuViewHolder viewHolder, final int position,  Category model) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition, boolean isLongClick) {
                        Intent serviceList = new Intent(Home.this, FoodList.class);
                        serviceList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(serviceList);
                    }
                });
            }
            @Override
            public MenuViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(itemView);
            }
        };
        adapter.startListening();
        recycler_menu.setAdapter(adapter);*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.refresh)
            loadMenu();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);
        }
        else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(Home.this, OrderStatus.class);
            startActivity(orderIntent);
        }
        else if (id == R.id.nav_log_out) {
            //Delete Remember user
//            Paper.book().destroy();

            Intent signIn = new Intent (Home.this, SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }
        else if(id == R.id.nav_change_pwd)
        {
//            showChangePasswordDialog();
        }

        else if(id == R.id.nav_home_address)
        {

//            showHomeAddressDialog();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//
//    private void showHomeAddressDialog() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
//        alertDialog.setTitle("Change Home Address");
//        alertDialog.setMessage("Please fill all information ");
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View layout_home = inflater.inflate(R.layout.home_adress_layout, null);
//
//        final MaterialEditText edtHomeAddress = (MaterialEditText)layout_home.findViewById(R.id.edtHomeAddress  );
//
//        alertDialog.setView(layout_home);
//
//        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//                Common.currentUser.setHomeAddress(edtHomeAddress.getText().toString());
//                FirebaseDatabase.getInstance().getReference("User")
//                        .child(Common.currentUser.getPhone())
//                        .setValue(Common.currentUser)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(Home.this, "Update Home Successfull", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });
//        alertDialog.show();
//    }
//
//    private void showChangePasswordDialog() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
//        alertDialog.setTitle("Change Password");
//        alertDialog.setMessage("Please fill all information ");
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View layout_pwd = inflater.inflate(R.layout.change_password_layout, null);
//
//        final MaterialEditText edtPassword = (MaterialEditText)layout_pwd.findViewById(R.id.edtPassword);
//        final MaterialEditText edtNewPassword = (MaterialEditText)layout_pwd.findViewById(R.id.edtNewPassword);
//        final MaterialEditText edtRepeatPassword = (MaterialEditText)layout_pwd.findViewById(R.id.edtRepeatPassword);
//
//        alertDialog.setView(layout_pwd);
//        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                final android.app.AlertDialog waitingDialog = new SpotsDialog(Home.this);
//                waitingDialog.show();
//
//                if(edtPassword.getText().toString().equals(Common.currentUser.getPassword()))
//                {
//                    if(edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString()))
//                    {
//                        Map<String, Object> passwordUpdate = new HashMap<>();
//                        passwordUpdate.put("password",edtNewPassword.getText().toString());
//
//                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
//                        user.child(Common.currentUser.getPhone())
//                                .updateChildren(passwordUpdate)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        waitingDialog.dismiss();
//                                        Toast.makeText(Home.this, "Password was update", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    }
//                    else
//                    {
//                        waitingDialog.dismiss();
//                        Toast.makeText(Home.this, "New Password doesnt match", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else {
//                    waitingDialog.dismiss();
//                    Toast.makeText(Home.this, "Wrong old password", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                dialogInterface.dismiss();
//            }
//        });
//        alertDialog.show();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (adapter != null){
//            adapter.startListening();
//        }
//        fab.setCount(new Database(this).getCountCart());
//
//    }
}