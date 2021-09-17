package com.example.eauction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eauction.Fragments.AboutUsFragment;
import com.example.eauction.Fragments.AuctionsFragment;
import com.example.eauction.Fragments.CarFragment;
import com.example.eauction.Fragments.CarPlateFragment;
import com.example.eauction.Fragments.ContactUsFragment;
import com.example.eauction.Fragments.GeneralItemFragment;
import com.example.eauction.Fragments.HomeFragment;
import com.example.eauction.Fragments.LandmarkFragment;
import com.example.eauction.Fragments.MyPropertiesFragment;
import com.example.eauction.Fragments.VIPNumberFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

public class InsertActivity extends AppCompatActivity {

    @BindView(R.id.spinner)
    MaterialSpinner spinner;

    @BindView(R.id.BtnUploadImg)
    ImageView BtnUploadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ButterKnife.bind(this);

        String[] ITEMS = {"Car Plate","Car", "Landmark","VIP Phone Number","General Item"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0); //Default Position

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                //Option numbers has the same index of selection as strings
                switch (position){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, new CarPlateFragment()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, new CarFragment()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, new LandmarkFragment()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, new VIPNumberFragment()).commit();
                        break;
                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, new GeneralItemFragment()).commit();
                        break;
                        
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        BtnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakeImageFromGallery();
            }
        });
    }

    private void TakeImageFromGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    BtnUploadImg.setImageURI(selectedImageUri);
                }
                break;
        }
    }
}