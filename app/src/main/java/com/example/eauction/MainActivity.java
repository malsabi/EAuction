package com.example.eauction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.eauction.Fragments.AboutUsFragment;
import com.example.eauction.Fragments.AuctionsFragment;
import com.example.eauction.Fragments.ContactUsFragment;
import com.example.eauction.Fragments.HomeFragment;
import com.example.eauction.Fragments.MyPropertiesFragment;
import com.example.eauction.Fragments.TermsCondFragment;
import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Models.User;
import com.example.eauction.Utilities.PreferenceUtils;
import com.google.android.material.navigation.NavigationView;
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

    TextView UsernameEditText;
    ImageView UserProfileImageView;



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

        View headerLayout = navigationView.getHeaderView(0);
        UsernameEditText = (TextView) headerLayout.findViewById(R.id.tvNameDrawerMenu);
        UserProfileImageView = (ImageView) headerLayout.findViewById(R.id.profile_image);

        UserProfileImageView.setOnClickListener(view -> TakeImageFromGallery());


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
        if (PreferenceUtils.getEmail(this) != null && PreferenceUtils.getPassword(this) != null)
        {
            Log.d("UserObj", "MSG: " + PreferenceUtils.getEmail(this));
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                this.UserObj = UserObj;
                Log.d("UserObj", UserObj.getProfilePicture() + "");
                String FullUserName = UserObj.getFirstName() + " " + UserObj.getLastName();
                UsernameEditText.setText(FullUserName);
                UserProfileImageView.setImageBitmap(TelemetryHelper.Base64ToImage(UserObj.getProfilePicture()));
                Log.d("UserObj", "Successfully set the text and image");
                MyProperties.SetUser(UserObj);
                Log.d("UserObj", "Successfully set the MyProperties");
            }, PreferenceUtils.getEmail(this));
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

    private void TakeImageFromGallery()
    {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                assert data != null;
                Uri selectedImageUri = data.getData();
                UserProfileImageView.setImageURI(selectedImageUri);
                //TODO Sabi Update image in the server
            }
        }
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