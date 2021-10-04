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
import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.Validations.TelemetryValidation;
import com.royrodriguez.transitionbutton.TransitionButton;
import java.util.ArrayList;
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

    private final CarPlateFragment CarPlateObj = new CarPlateFragment();           //Fragment
    private final CarFragment CarObj = new CarFragment();                          //Fragment
    private final LandmarkFragment LandmarkObj = new LandmarkFragment();           //Fragment
    private final VIPNumberFragment VIPNumberObj = new VIPNumberFragment();        //Fragment
    private final GeneralItemFragment GeneralObj = new GeneralItemFragment();      //Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ButterKnife.bind(this);


        AppInstance = (App)getApplication();

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, CarObj).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, LandmarkObj).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, VIPNumberObj).commit();
                        break;
                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, GeneralObj).commit();
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
        else if (Spinner.getSelectedItemPosition() == 1)
        {
            HandleCar();
        }
        else if (Spinner.getSelectedItemPosition() == 2)
        {
            HandleLandmark();
        }
        else if (Spinner.getSelectedItemPosition() == 3)
        {
            HandleVIPPhoneNumber();
        }
        else
        {
            HandleGeneral();
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
        CarPlateModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));

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
                    if (UserObj.getOwnedCarPlateTelemetry() == null)
                    {
                        UserObj.setOwnedCarPlateTelemetry(new ArrayList<CarPlate>());
                    }
                    //Get the owned telemetries that the user have
                    ArrayList<CarPlate> TempList = UserObj.getOwnedCarPlateTelemetry();
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(CarPlateModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedCarPlateTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetCarPlateTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Log.d("InsertActivity", "Successfully Updated");
                        }
                        else
                        {
                            Log.d("InsertActivity", SetResult.getMessage());
                        }
                    },  UserObj.getOwnedCarPlateTelemetry(), UserObj.getEmail());
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

    private void HandleCar()
    {
        Car CarModel = new Car(); //Model
        TelemetryValidation TelemetryValidator = new TelemetryValidation(); //Validator

        CarModel.setModel(CarObj.ModelEditText.getText().toString());
        CarModel.setMileage(Integer.parseInt(CarObj.MileageEditText.getText().toString()));
        CarModel.setName(CarObj.NameEditText.getText().toString());
        CarModel.setHorsePower(Integer.parseInt(CarObj.HorsePowerEditText.getText().toString()));
        CarModel.setDetails(DetailsEditText.getText().toString());
        CarModel.setAuctionStart(DateHelper.GetCurrentDateTime());
        CarModel.setStatus(StatusEnum.UnAuctioned);
        CarModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));

        ValidationResult Result = TelemetryValidator.CarValidation(CarModel);

        if (Result.isSuccess())
        {
            Log.d("InsertActivity", "Validation Succeeded");
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                if (UserObj != null)
                {
                    Log.d("InsertActivity", UserObj.getSsn());

                    CarModel.setID(UserObj.getEmail()); //Set the Telemetry ID by the user ID which is the email.

                    //If its empty create a new list with empty items.
                    if (UserObj.getOwnedCarPlateTelemetry() == null)
                    {
                        UserObj.setOwnedCarPlateTelemetry(new ArrayList<CarPlate>());
                    }
                    //Get the owned telemetries that the user have
                    ArrayList<Car> TempList = UserObj.getOwnedCarTelemetry();
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(CarModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedCarTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetCarTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Log.d("InsertActivity", "Successfully Updated");
                        }
                        else
                        {
                            Log.d("InsertActivity", SetResult.getMessage());
                        }
                    },  UserObj.getOwnedCarTelemetry(), UserObj.getEmail());
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

    private void HandleLandmark()
    {
        Landmark LandmarkModel = new Landmark(); //Model
        TelemetryValidation TelemetryValidator = new TelemetryValidation(); //Validator

        LandmarkModel.setType(LandmarkObj.TypeEditText.getText().toString());
        LandmarkModel.setLocation(LandmarkObj.LocationEditText.getText().toString());
        LandmarkModel.setName(LandmarkObj.NameEditText.getText().toString());
        LandmarkModel.setArea(Integer.parseInt(LandmarkObj.AreaEditText.getText().toString()));
        LandmarkModel.setDetails(DetailsEditText.getText().toString());
        LandmarkModel.setAuctionStart(DateHelper.GetCurrentDateTime());
        LandmarkModel.setStatus(StatusEnum.UnAuctioned);
        LandmarkModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));

        ValidationResult Result = TelemetryValidator.LandmarkValidation(LandmarkModel);

        if (Result.isSuccess())
        {
            Log.d("InsertActivity", "Validation Succeeded");
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                if (UserObj != null)
                {
                    Log.d("InsertActivity", UserObj.getSsn());

                    LandmarkModel.setID(UserObj.getEmail()); //Set the Telemetry ID by the user ID which is the email.

                    //If its empty create a new list with empty items.
                    if (UserObj.getOwnedCarPlateTelemetry() == null)
                    {
                        UserObj.setOwnedCarPlateTelemetry(new ArrayList<CarPlate>());
                    }
                    //Get the owned telemetries that the user have
                    ArrayList<Landmark> TempList = UserObj.getOwnedLandmarkTelemetry();
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(LandmarkModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedLandmarkTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetLandmarkTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Log.d("InsertActivity", "Successfully Updated");
                        }
                        else
                        {
                            Log.d("InsertActivity", SetResult.getMessage());
                        }
                    },  UserObj.getOwnedLandmarkTelemetry(), UserObj.getEmail());
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

    private void HandleVIPPhoneNumber()
    {
        VipPhoneNumber VIPPhoneModel = new VipPhoneNumber(); //Model
        TelemetryValidation TelemetryValidator = new TelemetryValidation(); //Validator

        VIPPhoneModel.setPhoneNumber(VIPNumberObj.PhoneNumber.getText().toString());
        VIPPhoneModel.setCompany(((TextView)VIPNumberObj.SpinnerPhoneCompany.getSelectedView()).getText().toString());
        VIPPhoneModel.setDetails(DetailsEditText.getText().toString());
        VIPPhoneModel.setAuctionStart(DateHelper.GetCurrentDateTime());
        VIPPhoneModel.setStatus(StatusEnum.UnAuctioned);
        VIPPhoneModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));


        ValidationResult Result = TelemetryValidator.VipPhoneNumberValidation(VIPPhoneModel);

        if (Result.isSuccess())
        {
            Log.d("InsertActivity", "Validation Succeeded");
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                if (UserObj != null)
                {
                    Log.d("InsertActivity", UserObj.getSsn());

                    VIPPhoneModel.setID(UserObj.getEmail()); //Set the Telemetry ID by the user ID which is the email.

                    //If its empty create a new list with empty items.
                    if (UserObj.getOwnedCarPlateTelemetry() == null)
                    {
                        UserObj.setOwnedCarPlateTelemetry(new ArrayList<CarPlate>());
                    }
                    //Get the owned telemetries that the user have
                    ArrayList<VipPhoneNumber> TempList = UserObj.getOwnedVipPhoneTelemetry();
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(VIPPhoneModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedVipPhoneTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetVipPhoneTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Log.d("InsertActivity", "Successfully Updated");
                        }
                        else
                        {
                            Log.d("InsertActivity", SetResult.getMessage());
                        }
                    },  UserObj.getOwnedVipPhoneTelemetry(), UserObj.getEmail());
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

    private void HandleGeneral()
    {
        General GeneralModel = new General(); //Model
        TelemetryValidation TelemetryValidator = new TelemetryValidation(); //Validator

        GeneralModel.setName(GeneralObj.NameEditText.getText().toString());
        GeneralModel.setDetails(DetailsEditText.getText().toString());
        GeneralModel.setAuctionStart(DateHelper.GetCurrentDateTime());
        GeneralModel.setStatus(StatusEnum.UnAuctioned);
        GeneralModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));

        ValidationResult Result = TelemetryValidator.GeneralValidation(GeneralModel);

        if (Result.isSuccess())
        {
            Log.d("InsertActivity", "Validation Succeeded");
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                if (UserObj != null)
                {
                    Log.d("InsertActivity", UserObj.getSsn());

                    GeneralModel.setID(UserObj.getEmail()); //Set the Telemetry ID by the user ID which is the email.

                    //If its empty create a new list with empty items.
                    if (UserObj.getOwnedCarPlateTelemetry() == null)
                    {
                        UserObj.setOwnedCarPlateTelemetry(new ArrayList<CarPlate>());
                    }
                    //Get the owned telemetries that the user have
                    ArrayList<General> TempList = UserObj.getOwnedGeneralTelemetry();
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(GeneralModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedGeneralTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetGeneralTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Log.d("InsertActivity", "Successfully Updated");
                        }
                        else
                        {
                            Log.d("InsertActivity", SetResult.getMessage());
                        }
                    },  UserObj.getOwnedGeneralTelemetry(), UserObj.getEmail());
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