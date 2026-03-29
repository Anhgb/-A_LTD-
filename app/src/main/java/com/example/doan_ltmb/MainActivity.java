package com.example.doan_ltmb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.doan_ltmb.databinding.ActivityMainBinding;
import com.example.doan_ltmb.ui.adapter.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Làm thanh trạng thái màu trắng đồng nhất với Toolbar (Tràn viền)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Sử dụng Glide để load logo sắc nét và bo tròn tràn viền
        Glide.with(this)
                .load(R.drawable.logo)
                .circleCrop() // Bo tròn hoàn hảo
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivLogo);
        
        setupViewPager();
        setupBottomNavigation();

        binding.ivCartTop.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
        });

        if (getIntent().getBooleanExtra("open_cart", false)) {
            binding.viewPager.setCurrentItem(1);
        }
    }

    private void setupViewPager() {
        MainPagerAdapter adapter = new MainPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);
        
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    binding.appBarLayout.setVisibility(View.GONE);
                } else {
                    binding.appBarLayout.setVisibility(View.VISIBLE);
                }

                switch (position) {
                    case 0: binding.bottomNavigation.setSelectedItemId(R.id.nav_home); break;
                    case 1: binding.bottomNavigation.setSelectedItemId(R.id.nav_cart); break;
                    case 2: binding.bottomNavigation.setSelectedItemId(R.id.nav_about); break;
                }
            }
        });
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                binding.viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.nav_cart) {
                binding.viewPager.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.nav_about) {
                binding.viewPager.setCurrentItem(2);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("open_cart", false)) {
            binding.viewPager.setCurrentItem(1);
        }
    }
}
