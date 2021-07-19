package com.something.groceryapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.something.groceryapp.R;
import com.something.groceryapp.activity.ItemActivity;
import com.something.groceryapp.adapter.CategoriesAdapter;
import com.something.groceryapp.adapter.SliderAdapter;
import com.something.groceryapp.model.Categories;
import com.something.groceryapp.model.SliderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    RecyclerView categoriesRecyclerView;
    List<Categories> categoriesList;
    List<SliderItem> sliderList;
    ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    DatabaseReference databaseReference;

    public HomeFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager2 = root.findViewById(R.id.viewPager);
        viewPager2.setAdapter(new SliderAdapter(sliderList,viewPager2));
        categoriesRecyclerView = root.findViewById(R.id.categoriesListRecyclerView);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,2000);
            }
        });
        return root;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoriesList = new ArrayList<>();
        sliderList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("categories");
        populateSliderList();
        populateList();

    }

    public void populateSliderList() {
        sliderList.add(new SliderItem(R.drawable.slider1));
        sliderList.add(new SliderItem(R.drawable.slider2));
        sliderList.add(new SliderItem(R.drawable.slider3));
    }

    public void populateList() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    Categories categories = ds.getValue(Categories.class);
                    categoriesList.add(categories);
                }
                categoriesRecyclerView.setAdapter(new CategoriesAdapter(getContext(),categoriesList,this::OnItemClick));
            }

            public void OnItemClick(int position) {
                Categories categories = categoriesList.get(position);
                String category_key = categories.getCategoryKey();
                String category_name = categories.getCategoryName();
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra("categoryKey",category_key);
                intent.putExtra("categoryName",category_name);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

}