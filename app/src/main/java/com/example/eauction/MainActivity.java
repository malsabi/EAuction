package com.example.eauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.eauction.Fragments.AboutUsFragment;
import com.example.eauction.Fragments.AuctionsFragment;
import com.example.eauction.Fragments.ContactUsFragment;
import com.example.eauction.Fragments.HomeFragment;
import com.example.eauction.Fragments.MyPropertiesFragment;
import com.example.eauction.Fragments.TermsCondFragment;
import com.google.android.material.navigation.NavigationView;
import com.royrodriguez.transitionbutton.TransitionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.BtnSignoutDrawer)
    TransitionButton TBtnSignout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view) //Placeholder for fragments
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); //Butterknife binding Method
        setSupportActionBar(toolbar); //Toolbar binding Method(For custom side menu ;D )

        navigationView.setNavigationItemSelectedListener(this); //Attaching Listener to Selected Item


        //region Burger Button Animation
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //endregion


        if(savedInstanceState == null){ //Prevents fragment from getting destroyed
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit(); //Default Fragment
            navigationView.setCheckedItem(R.id.dm_home);
        }

    }

    @Override
    public void onBackPressed() {

        //Tiny Handle If the user makes a back while the menu is open
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    //Handles menu selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dm_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.dm_auctions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AuctionsFragment()).commit();
                break;
            case R.id.dm_myproperties:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyPropertiesFragment()).commit();
                break;
            case R.id.dm_aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
                break;
            case R.id.dm_terms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TermsCondFragment()).commit();
                break;
            case R.id.dm_contactus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactUsFragment()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}