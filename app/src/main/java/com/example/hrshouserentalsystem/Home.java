package com.example.hrshouserentalsystem;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.example.hrshouserentalsystem.Adapters.ViewPagerAdapter;
import com.example.hrshouserentalsystem.Fragments.MyAds;
import com.example.hrshouserentalsystem.Fragments.NewsFeed;
import com.example.hrshouserentalsystem.Fragments.SellingBuying;
import com.example.hrshouserentalsystem.Fragments.UserProfile;
import com.example.hrshouserentalsystem.databinding.ActivityHomeBinding;

//************************************************************
public class Home extends AppCompatActivity
//************************************************************
{
    ActivityHomeBinding mBinding;
    MenuItem prevMenuItem;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            { case R.id.action_home:
                mBinding.viewPager.setCurrentItem(0);
                break;
                case R.id.action_selling:
                    mBinding.viewPager.setCurrentItem(1);
                    break;
                case R.id.action_favorite:
                    mBinding.viewPager.setCurrentItem(2);
                    break;
                    case R.id.action_profile:
                    mBinding.viewPager.setCurrentItem(3);
                    break; }
            return false;
        });

        mBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    mBinding.bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                mBinding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mBinding.bottomNavigation.getMenu().getItem(position);
                super.onPageSelected(position);
            }
        });


        setupViewPager(mBinding.viewPager);
    }

    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new NewsFeed());
        adapter.addFragment(new SellingBuying());
        adapter.addFragment(new MyAds());
        adapter.addFragment(new UserProfile());
        viewPager.setAdapter(adapter);

    }

}