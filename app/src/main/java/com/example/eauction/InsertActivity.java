package com.example.eauction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.eauction.Enums.StatusEnum;
import com.example.eauction.Fragments.CarFragment;
import com.example.eauction.Fragments.CarPlateFragment;
import com.example.eauction.Fragments.GeneralItemFragment;
import com.example.eauction.Fragments.LandmarkFragment;
import com.example.eauction.Fragments.VIPNumberFragment;
import com.example.eauction.Helpers.DateHelper;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Validations.TelemetryValidation;
import com.royrodriguez.transitionbutton.TransitionButton;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

public class InsertActivity extends AppCompatActivity
{

    @BindView(R.id.spinner)
    MaterialSpinner Spinner;

    @BindView(R.id.BtnUploadImg)
    ImageView UploadImageButton;

    @BindView(R.id.EtExtraDetails)
    EditText DetailsEditText;

    @BindView(R.id.BtnAddTelemetry)
    TransitionButton AddTelemetryButton;

    private App AppInstance;
    private final CarPlateFragment CarPlateObj = new CarPlateFragment(); //Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ButterKnife.bind(this);


        AppInstance = (App)getApplication();
        AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
        {
            Log.d("Hady", "Telemetries Count" + UserObj.getOwnedTelemetry().size());
        }, "h5JGBrQTGorO7q6IaFMfu5cSqqB6XTp1aybOD11spnQ=");


        String[] ITEMS = {"Car Plate","Car", "Landmark","VIP Phone Number","General Item"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(adapter);
        Spinner.setSelection(0); //Default Position
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                //Option numbers has the same index of selection as strings
                Log.d("ComboBox","Selected: " + position);
                switch (position)
                {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, CarPlateObj).commit();
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
            public void onNothingSelected(AdapterView<?> parentView)
            {
                // your code here
            }
        });
        UploadImageButton.setOnClickListener(view -> TakeImageFromGallery());
        AddTelemetryButton.setOnClickListener(view -> AddTelemetryHandler());
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
                UploadImageButton.setImageURI(selectedImageUri);
            }
        }
    }

    private void AddTelemetryHandler()
    {
        AddTelemetryButton.startAnimation();
        if (Spinner.getSelectedItemPosition() == 0)
        {
            HandleCarPlate();
        }
        AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
    }


    //region "Fragment Handlers"
    public void HandleCarPlate()
    {
        CarPlate CarPlateModel = new CarPlate(); //Model
        TelemetryValidation TelemetryValidator = new TelemetryValidation(); //Validator

        //Fill the model from the fragment
        CarPlateModel.setPlateNumber(CarPlateObj.CarPlateNumber.getText().toString());
        CarPlateModel.setPlateCode(CarPlateObj.CarPlateCode.getText().toString());
        CarPlateModel.setEmirate(((TextView)CarPlateObj.SpinnerCountry.getSelectedView()).getText().toString());
        CarPlateModel.setDetails(DetailsEditText.getText().toString());
        CarPlateModel.setAuctionStart(DateHelper.GetCurrentDateTime());
        CarPlateModel.setStatus(StatusEnum.UnAuctioned);

        //Validate the model
        ValidationResult Result = TelemetryValidator.CarPlateValidation(CarPlateModel);

        if (Result.isSuccess()) //Data is validated and ready to be submitted to fire store
        {
            Log.d("InsertActivity", "Validation Succeeded");
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                if (UserObj != null)
                {
                    Log.d("InsertActivity", UserObj.getSsn());

                    CarPlateModel.setID(UserObj.getEmail()); //Set the Telemetry ID by the user ID which is the email.

                    //If its empty create a new list with empty items.
                    if (UserObj.getOwnedTelemetry() == null)
                    {
                        UserObj.setOwnedTelemetry(new ArrayList<Telemetry>());
                    }
                    //Get the owned telemetries that the user have
                    List<Telemetry> OwnedTelemetry = UserObj.getOwnedTelemetry();
                    //Add the Telemetry Object to the Owned Telemetry List.
                    OwnedTelemetry.add(CarPlateModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedTelemetry(OwnedTelemetry);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetUserOwnedTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Log.d("InsertActivity", "Successfully Updated");
                        }
                        else
                        {
                            Log.d("InsertActivity", SetResult.getMessage());
                        }
                    }, UserObj.getOwnedTelemetry(), UserObj.getEmail());
                }
                else
                {
                    Log.d("InsertActivity","NULL USER");
                }
            }, "h5JGBrQTGorO7q6IaFMfu5cSqqB6XTp1aybOD11spnQ=");
        }
        else
        {
            Log.d("InsertActivity", "Validation Failed");
            int resID = getResources().getIdentifier(Result.getTitle(), "id", getPackageName());
            if (resID != 0)
            {
                EditText InvalidControl = findViewById(resID);
                InvalidControl.setError(Result.getMessage());
            }
            else
            {
                Toast.makeText(InsertActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    //endregion
}