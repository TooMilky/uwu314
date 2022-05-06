package com.uwu.csit314.ViewHolder;

import android.content.ClipData;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uwu.csit314.Interface.ItemClickListener;
import com.uwu.csit314.R;


public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView food_name, food_price;
    public ImageView food_image, fav_image, share_image, quick_cart;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_name = (TextView)itemView.findViewById(R.id.food_name);
        food_image = (ImageView)itemView.findViewById(R.id.food_image);
        fav_image = (ImageView)itemView.findViewById(R.id.fav);
        share_image= (ImageView)itemView.findViewById(R.id.btnShare);
        food_price = (TextView)itemView.findViewById(R.id.food_price);
        quick_cart = (ImageView)itemView.findViewById(R.id.btn_quick_cart);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAbsoluteAdapterPosition(), false);

    }


}