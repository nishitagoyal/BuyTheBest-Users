package com.something.groceryapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.something.groceryapp.R;
import com.something.groceryapp.model.Categories;
import com.something.groceryapp.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    Context context;
    List<Categories> categoriesList;
    private OnViewClickListener mOnViewClickListener;

    public CategoriesAdapter(Context context, List<Categories> categoriesList, OnViewClickListener mOnViewClickListener) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.mOnViewClickListener = mOnViewClickListener;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;
        root = LayoutInflater.from(context).inflate(R.layout.categories_list_item,parent,false);
        CategoriesAdapter.CategoriesViewHolder holder = new CategoriesAdapter.CategoriesViewHolder(root,mOnViewClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {

        Categories categories = categoriesList.get(position);
        Uri categoryUri = Uri.parse(categories.getCategoryImage());
        Picasso.get().load(categoryUri).into(holder.iv_category_image);
        holder.tv_category_title.setText(categories.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }


    public static class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView iv_category_image;
        private TextView tv_category_title;
        OnViewClickListener onViewClickListener;

        public CategoriesViewHolder(@NonNull View itemView, OnViewClickListener onViewClickListener) {
            super(itemView);
            iv_category_image = itemView.findViewById(R.id.categoriesImage);
            tv_category_title = itemView.findViewById(R.id.categoriesTitle);
            this.onViewClickListener = onViewClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onViewClickListener.OnItemClick(getAdapterPosition());
        }
    }

    public interface OnViewClickListener {
        void OnItemClick(int position);
    }
}
