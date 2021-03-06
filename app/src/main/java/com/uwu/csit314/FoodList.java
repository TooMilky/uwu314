package com.uwu.csit314;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.uwu.csit314.Interface.ItemClickListener;
import com.uwu.csit314.Model.Food;
import com.uwu.csit314.ViewHolder.FoodViewHolder;

import java.util.ArrayList;
import java.util.List;


public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList , categoryList;

//    Database localDB;
    String categoryId = "";

//    CallbackManager callbackManager;
//    ShareDialog shareDialog;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;

//    Target target = new Target() {
//        @Override
//        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//            SharePhoto photo = new SharePhoto.Builder()
//                    .setBitmap(bitmap)
//                    .build();
//            if (ShareDialog.canShow(SharePhotoContent.class)) {
//                SharePhotoContent content = new SharePhotoContent.Builder()
//                        .addPhoto(photo)
//                        .build();
//                shareDialog.show(content);
//            }
//        }
//
//        @Override
//        public void onBitmapFailed(Drawable errorDrawable) {
//
//        }
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//        }
//    };


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_cart);

        setContentView(R.layout.activity_food_list);

//        callbackManager = CallbackManager.Factory.create();
//        shareDialog = new ShareDialog(this);


        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        categoryList = database.getReference("Category");

//        localDB = new Database(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (getIntent() != null) {
//                    categoryId = categoryList.getKey();
//                    categoryId = getIntent().getDataString("CategoryId");
                    categoryId = getIntent().getStringExtra("CategoryId");
                }
                if (!categoryId.isEmpty() && categoryId != null) {
//                    if (Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoryId);
//                    else {
//                        Toast.makeText(FoodList.this, "Please check your connection", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                }
                else{
                    Toast.makeText(FoodList.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                if (getIntent() != null) {
//                    categoryId = categoryList.getKey();
                    categoryId = getIntent().getStringExtra("CategoryId");
                }
                if (!categoryId.isEmpty() && categoryId != null) {
//                    if (Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoryId);
//                    else {
//                        Toast.makeText(FoodList.this, "Please check your connection", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null){
            adapter.startListening();
        }
    }

    private void loadListFood(final String categoryId) {
        Query searchByName = foodList.orderByChild("menuId").equalTo(categoryId);

        FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByName, Food.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodOptions) {

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder viewHolder, final int position, @NonNull final Food model) {
                viewHolder.food_name.setText(model.getName());
                viewHolder.food_price.setText(String.format("$ %s", model.getPrice().toString()));
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);

//                viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new Database(getBaseContext()).addToCart(new Order(
//                                adapter.getRef(position).getKey(),
//                                model.getName(),
//                                "1",
//                                model.getPrice(),
//                                model.getDiscount(),
//                                model.getImage()
//                        ));
//                        Toast.makeText(FoodList.this, "Added to Cart", Toast.LENGTH_SHORT).show();
//                    }
//                });

//                if (localDB.isFavorite(adapter.getRef(position).getKey()))
//                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

//                viewHolder.share_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Picasso.with(getApplicationContext())
//                                .load(model.getImage())
//                                .into(target);
//
//                    }
//                });
//                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!localDB.isFavorite(adapter.getRef(position).getKey())) {
//                            localDB.addToFavorites(adapter.getRef(position).getKey());
//                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
//                            Toast.makeText(FoodList.this, "" + model.getName() + " was added to Favorites", Toast.LENGTH_SHORT).show();
//                        } else {
//                            localDB.removeFromFavorites(adapter.getRef(position).getKey());
//                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                            Toast.makeText(FoodList.this, "" + model.getName() + " was removed from Favorites", Toast.LENGTH_SHORT)
//                                    .show();
//
//                        }
//                    }
//                });

                final Food local = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodList = new Intent(FoodList.this, FoodDetail.class);
                        //Because CategoryId is key, so we just get key of this item
                        foodList.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodList);

                    }
                });

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

    /*private void loadListFood(String categoryId) {
        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("category");
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(foodList, Food.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,
                FoodViewHolder.class,foodList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void onBindViewHolder(FoodViewHolder holder, int position, Food model) {
                holder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(holder.food_image);
                final Food clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        //Get CategoryId and send to new Activity
                        Intent foodList = new Intent(FoodList.this,FoodDetail.class);
                        //Because CategoryId is key, so we just get key of this item
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
            }
            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);
                return new FoodViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }*/
