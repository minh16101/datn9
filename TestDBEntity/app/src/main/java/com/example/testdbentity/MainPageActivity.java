package com.example.testdbentity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testdbentity.accountfragment.AccountFragment;
import com.example.testdbentity.discoverfragment.DiscoveryFragment;
import com.example.testdbentity.homefragment.HomeFragment;
import com.example.testdbentity.homefragment.bookinghotel.database.BookingHotelViewModel;
import com.example.testdbentity.login.database.LoginEntity;
import com.example.testdbentity.login.database.LoginViewModel;
import com.example.testdbentity.shoppingfragment.ShoppingFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager;

    private NavigationBarView.OnItemSelectedListener onSelectItem = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.item_home:
                    fragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
                    return true;

                case R.id.item_bill:
                    fragment = new ShoppingFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
                    return true;

                case R.id.item_discover:
                    fragment = new DiscoveryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
                    return true;

                case R.id.item_account:
                    fragment = new AccountFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //get checklogin
        bottomNavigationView = findViewById(R.id.bottom_nav_main_page);

        bottomNavigationView.setOnItemSelectedListener(onSelectItem);
        bottomNavigationView.setSelectedItemId(R.id.item_home);
        Fragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();

    }


    @Override
    public void onBackPressed() {

    }
}