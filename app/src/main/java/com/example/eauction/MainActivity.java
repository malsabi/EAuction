package com.example.eauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.eauction.Fragments.AboutUsFragment;
import com.example.eauction.Fragments.AuctionsFragment;
import com.example.eauction.Fragments.ContactUsFragment;
import com.example.eauction.Fragments.HomeFragment;
import com.example.eauction.Fragments.MyPropertiesFragment;
import com.example.eauction.Fragments.TermsCondFragment;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Models.User;
import com.example.eauction.Utilities.PreferenceUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.royrodriguez.transitionbutton.TransitionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    @BindView(R.id.BtnSignoutDrawer)
    TransitionButton SignOutButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private App AppInstance;
    private User UserObj;

    private final MyPropertiesFragment MyProperties = new MyPropertiesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        AppInstance = (App)getApplication();
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.dm_home);
        }
        InitializeBurgerButtonAnimation();
        GetUserInformation();
        SignOutButton.setOnClickListener(v ->
        {
            OnSignOutClick();
        });
    }

    private void InitializeBurgerButtonAnimation()
    {
        //region Burger Button Animation
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //endregion
    }

    private void GetUserInformation()
    {
        PreferenceUtils.ClearPreferences(this);
        if (PreferenceUtils.getEmail(this) != null && PreferenceUtils.getPassword(this) != null)
        {
            Log.d("UserObj", "MSG: " + PreferenceUtils.getEmail(this));
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                this.UserObj = UserObj;
                MyProperties.SetUser(UserObj);
            }, PreferenceUtils.getEmail(this));
        }
        else
        {
            Gson gson = new Gson();
            UserObj = gson.fromJson(getIntent().getStringExtra("UserObject"), User.class);
            MyProperties.SetUser(UserObj);
        }
    }

    private void OnSignOutClick()
    {
        AppInstance.GetFireStoreInstance().SignOut(Result ->
        {
            Toast.makeText(MainActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();
            if (Result.isSuccess())
            {
                PreferenceUtils.saveEmail(null, this);
                PreferenceUtils.savePassword(null, this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }, UserObj);
    }


    //region NavigationView Methods
    @Override
    public void onBackPressed()
    {
        //Tiny Handle If the user makes a back while the menu is open
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.dm_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.dm_auctions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AuctionsFragment()).commit();
                break;
            case R.id.dm_myproperties:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MyProperties).commit();
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
    //endregion
}