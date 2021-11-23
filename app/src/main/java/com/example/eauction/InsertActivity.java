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
import com.example.eauction.Fragments.MyPropertiesFragment;
import com.example.eauction.Fragments.ServiceFragment;
import com.example.eauction.Fragments.VIPNumberFragment;
import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.Utilities.PreferenceUtils;
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
    private User UserObj;

    private final CarPlateFragment CarPlateObj = new CarPlateFragment();           //Fragment
    private final CarFragment CarObj = new CarFragment();                          //Fragment
    private final LandmarkFragment LandmarkObj = new LandmarkFragment();           //Fragment
    private final VIPNumberFragment VIPNumberObj = new VIPNumberFragment();        //Fragment
    private final GeneralItemFragment GeneralObj = new GeneralItemFragment();      //Fragment
    private final ServiceFragment ServiceObj = new ServiceFragment();              //Fragment


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ButterKnife.bind(this);


        AppInstance = (App)getApplication();
        GetUserInformation();

        String[] ITEMS = {"Car Plate", "Car", "Landmark", "VIP Phone Number", "General Item", "Service"};
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
                Log.d("InsertActivity","ComboBox Selected: " + position);
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
                    case 5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.TeleFragmentContainer, ServiceObj).commit();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
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
        else if (Spinner.getSelectedItemPosition() == 4)
        {
            HandleGeneral();
        }
        else
        {
            HandleService();
        }

    }

    private void GetUserInformation()
    {
        if (PreferenceUtils.getEmail(this) != null && PreferenceUtils.getPassword(this) != null)
        {
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                this.UserObj = UserObj;
            }, PreferenceUtils.getEmail(this));
        }
        else
        {
            HandleService();
        }
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
        CarPlateModel.setStatus(StatusEnum.UnAuctioned);
        CarPlateModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));
        CarPlateModel.setId(TelemetryHelper.GetTelemetryHash(CarPlateModel.toString()));

        //Validate the model
        ValidationResult Result = TelemetryValidator.CarPlateValidation(CarPlateModel);

        if (Result.isSuccess()) //Data is validated and ready to be submitted to fire store
        {
            Log.d("InsertActivity", "HandleCarPlate Validation Succeeded");

            if (UserObj != null)
            {
                Log.d("InsertActivity", "HandleCarPlate User Id: " + UserObj.getId());
                //Set the Telemetry ID by the user ID which is the email.
                CarPlateModel.setOwnerId(UserObj.getEmail());
                //If its empty create a new list with empty items.
                if (UserObj.getOwnedCarPlateTelemetry() == null)
                {
                    UserObj.setOwnedCarPlateTelemetry(new ArrayList<CarPlate>());
                }
                //Get the owned telemetries that the user have
                ArrayList<CarPlate> TempList = UserObj.getOwnedCarPlateTelemetry();

                boolean IsTelemetryAdded = TelemetryHelper.IsTelemetryAdded(TempList, CarPlateModel);

                if (IsTelemetryAdded)
                {
                    Toast.makeText(InsertActivity.this, "You cannot add the same telemetry twice", Toast.LENGTH_SHORT).show();
                    AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                }
                else
                {
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(CarPlateModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedCarPlateTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetCarPlateTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Toast.makeText(InsertActivity.this, "Telemetry Item added successfully.", Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleCarPlate Successfully Updated");
                            finish();
                        }
                        else
                        {
                            Toast.makeText(InsertActivity.this, "Failed to add, " + SetResult.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleCarPlate Failed: " + SetResult.getMessage());
                        }
                        AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                        //....
                    }, UserObj.getOwnedCarPlateTelemetry(), UserObj.getEmail());
                }
            }
            else
            {
                Log.d("InsertActivity", "HandleCarPlate No user found");
            }
        }
        else
        {
            Log.d("InsertActivity", "HandleCarPlate Validation Failed");
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
            AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
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
        CarModel.setStatus(StatusEnum.UnAuctioned);
        CarModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));
        CarModel.setId(TelemetryHelper.GetTelemetryHash(CarModel.toString()));

        ValidationResult Result = TelemetryValidator.CarValidation(CarModel);

        if (Result.isSuccess()) {
            Log.d("InsertActivity", "HandleCar Validation Succeeded");

            if (UserObj != null) {
                Log.d("InsertActivity", "HandleCar User Id: " + UserObj.getId());

                CarModel.setOwnerId(UserObj.getEmail()); //Set the Telemetry ID by the user ID which is the email.

                //If its empty create a new list with empty items.
                if (UserObj.getOwnedCarTelemetry() == null)
                {
                    UserObj.setOwnedCarTelemetry(new ArrayList<Car>());
                }
                //Get the owned telemetries that the user have
                ArrayList<Car> TempList = UserObj.getOwnedCarTelemetry();

                boolean IsTelemetryAdded = TelemetryHelper.IsTelemetryAdded(TempList, CarModel);

                if (IsTelemetryAdded)
                {
                    Toast.makeText(InsertActivity.this, "You cannot add the same telemetry twice", Toast.LENGTH_SHORT).show();
                    AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                }
                else
                {
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(CarModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedCarTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetCarTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Toast.makeText(InsertActivity.this, "Telemetry Item added successfully.", Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleCar Successfully Updated");
                            finish();
                        }
                        else
                        {
                            Toast.makeText(InsertActivity.this, "Failed to add, " + SetResult.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleCar Failed: " + SetResult.getMessage());
                        }
                        AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    }, UserObj.getOwnedCarTelemetry(), UserObj.getEmail());
                }
            }
            else
            {
                Log.d("InsertActivity", "HandleCar No user found");
            }
        }
        else
        {
            Log.d("InsertActivity", "HandleCar Validation Failed");
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
            AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
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
        LandmarkModel.setStatus(StatusEnum.UnAuctioned);
        LandmarkModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));
        LandmarkModel.setId(TelemetryHelper.GetTelemetryHash(LandmarkModel.toString()));

        ValidationResult Result = TelemetryValidator.LandmarkValidation(LandmarkModel);

        if (Result.isSuccess())
        {
            Log.d("InsertActivity", "HandleLandmark Validation Succeeded");

            if (UserObj != null)
            {
                Log.d("InsertActivity", "HandleLandmark User Id: " + UserObj.getId());

                //Set the Telemetry ID by the user ID which is the email.
                LandmarkModel.setOwnerId(UserObj.getEmail());

                //If its empty create a new list with empty items.
                if (UserObj.getOwnedLandmarkTelemetry() == null)
                {
                    UserObj.setOwnedLandmarkTelemetry(new ArrayList<Landmark>());
                }
                //Get the owned telemetries that the user have
                ArrayList<Landmark> TempList = UserObj.getOwnedLandmarkTelemetry();

                boolean IsTelemetryAdded = TelemetryHelper.IsTelemetryAdded(TempList, LandmarkModel);

                if (IsTelemetryAdded)
                {
                    Toast.makeText(InsertActivity.this, "You cannot add the same telemetry twice", Toast.LENGTH_SHORT).show();
                    AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                }
                else
                {
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(LandmarkModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedLandmarkTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetLandmarkTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Toast.makeText(InsertActivity.this, "Telemetry Item added successfully.", Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleLandmark Successfully Updated");
                            finish();
                        }
                        else
                        {
                            Toast.makeText(InsertActivity.this, "Failed to add, " + SetResult.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleLandmark Failed: " + SetResult.getMessage());
                        }
                        AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    }, UserObj.getOwnedLandmarkTelemetry(), UserObj.getEmail());
                }
            }
            else
            {
                Log.d("InsertActivity", "HandleLandmark No User Found");
            }
        }
        else
        {
            Log.d("InsertActivity", "HandleLandmark Validation Failed");
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
            AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }
    }

    private void HandleVIPPhoneNumber()
    {
        VipPhoneNumber VIPPhoneModel = new VipPhoneNumber(); //Model
        TelemetryValidation TelemetryValidator = new TelemetryValidation(); //Validator

        VIPPhoneModel.setPhoneNumber(VIPNumberObj.PhoneNumber.getText().toString());
        VIPPhoneModel.setCompany(((TextView)VIPNumberObj.SpinnerPhoneCompany.getSelectedView()).getText().toString());
        VIPPhoneModel.setDetails(DetailsEditText.getText().toString());
        VIPPhoneModel.setStatus(StatusEnum.UnAuctioned);
        VIPPhoneModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));
        VIPPhoneModel.setId(TelemetryHelper.GetTelemetryHash(VIPPhoneModel.toString()));

        ValidationResult Result = TelemetryValidator.VipPhoneNumberValidation(VIPPhoneModel);

        if (Result.isSuccess())
        {
            Log.d("InsertActivity", "HandleVIPPhoneNumber Validation Succeeded");
            if (UserObj != null)
            {
                Log.d("InsertActivity", "HandleVIPPhoneNumber User Id: " + UserObj.getId());

                //Set the Telemetry ID by the user ID which is the email.
                VIPPhoneModel.setOwnerId(UserObj.getEmail());

                //If its empty create a new list with empty items.
                if (UserObj.getOwnedVipPhoneTelemetry() == null)
                {
                    UserObj.setOwnedVipPhoneTelemetry(new ArrayList<VipPhoneNumber>());
                }
                //Get the owned telemetries that the user have
                ArrayList<VipPhoneNumber> TempList = UserObj.getOwnedVipPhoneTelemetry();

                boolean IsTelemetryAdded = TelemetryHelper.IsTelemetryAdded(TempList, VIPPhoneModel);

                if (IsTelemetryAdded)
                {
                    Toast.makeText(InsertActivity.this, "You cannot add the same telemetry twice", Toast.LENGTH_SHORT).show();
                    AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                }
                else
                {
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(VIPPhoneModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedVipPhoneTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetVipPhoneTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Toast.makeText(InsertActivity.this, "Telemetry Item added successfully.", Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleVIPPhoneNumber Successfully Updated");
                            finish();
                        }
                        else
                        {
                            Toast.makeText(InsertActivity.this, "Failed to add, " + SetResult.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleVIPPhoneNumber Failed: " + SetResult.getMessage());
                        }
                        AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    }, UserObj.getOwnedVipPhoneTelemetry(), UserObj.getEmail());
                }
            }
            else
            {
                Log.d("InsertActivity", "HandleVIPPhoneNumber No User Found");
            }
        }
        else
        {
            Log.d("InsertActivity", "HandleVIPPhoneNumber Validation Failed");
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
            AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }
    }

    private void HandleGeneral()
    {
        General GeneralModel = new General(); //Model
        TelemetryValidation TelemetryValidator = new TelemetryValidation(); //Validator

        GeneralModel.setName(GeneralObj.NameEditText.getText().toString());
        GeneralModel.setDetails(DetailsEditText.getText().toString());
        GeneralModel.setStatus(StatusEnum.UnAuctioned);
        GeneralModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));
        GeneralModel.setId(TelemetryHelper.GetTelemetryHash(GeneralModel.toString()));

        ValidationResult Result = TelemetryValidator.GeneralValidation(GeneralModel);

        if (Result.isSuccess())
        {
            Log.d("InsertActivity", "HandleGeneral Validation Succeeded");
            if (UserObj != null)
            {
                Log.d("InsertActivity", "HandleGeneral User ID: " + UserObj.getId());

                //Set the Telemetry ID by the user ID which is the email.
                GeneralModel.setOwnerId(UserObj.getEmail());

                //If its empty create a new list with empty items.
                if (UserObj.getOwnedGeneralTelemetry() == null)
                {
                    UserObj.setOwnedGeneralTelemetry(new ArrayList<General>());
                }
                //Get the owned telemetries that the user have
                ArrayList<General> TempList = UserObj.getOwnedGeneralTelemetry();

                boolean IsTelemetryAdded = TelemetryHelper.IsTelemetryAdded(TempList, GeneralModel);

                if (IsTelemetryAdded)
                {
                    Toast.makeText(InsertActivity.this, "You cannot add the same telemetry twice", Toast.LENGTH_SHORT).show();
                    AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                }
                else
                {
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(GeneralModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedGeneralTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetGeneralTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Toast.makeText(InsertActivity.this, "Telemetry Item added successfully.", Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleGeneral Successfully Updated");
                            finish();
                        }
                        else
                        {
                            Toast.makeText(InsertActivity.this, "Failed to add, " + SetResult.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleGeneral Failed: " + SetResult.getMessage());
                        }
                        AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    }, UserObj.getOwnedGeneralTelemetry(), UserObj.getEmail());
                }
            }
            else
            {
                Log.d("InsertActivity", "HandleGeneral No User Found");
            }
        }
        else
        {
            Log.d("InsertActivity", "HandleGeneral Validation Failed");
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
            AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }
    }

    public void HandleService()
    {
        Service ServiceModel = new Service(); //Model
        TelemetryValidation TelemetryValidator = new TelemetryValidation(); //Validator

        ServiceModel.setName(ServiceObj.ServiceNameEditText.getText().toString());
        ServiceModel.setDetails(DetailsEditText.getText().toString());
        ServiceModel.setStatus(StatusEnum.UnAuctioned);
        ServiceModel.setImage(TelemetryHelper.ImageToBase64(UploadImageButton.getDrawable()));
        ServiceModel.setId(TelemetryHelper.GetTelemetryHash(ServiceModel.toString()));

        ValidationResult Result = TelemetryValidator.ServiceValidation(ServiceModel);

        if (Result.isSuccess())
        {
            Log.d("InsertActivity", "HandleService Validation Succeeded");
            if (UserObj != null)
            {
                Log.d("InsertActivity", "HandleService User ID: " + UserObj.getId());

                //Set the Telemetry ID by the user ID which is the email.
                ServiceModel.setOwnerId(UserObj.getEmail());

                //If its empty create a new list with empty items.
                if (UserObj.getOwnedServiceTelemetry() == null)
                {
                    UserObj.setOwnedServiceTelemetry(new ArrayList<Service>());
                }
                //Get the owned telemetries that the user have
                ArrayList<Service> TempList = UserObj.getOwnedServiceTelemetry();

                boolean IsTelemetryAdded = TelemetryHelper.IsTelemetryAdded(TempList, ServiceModel);

                if (IsTelemetryAdded)
                {
                    Toast.makeText(InsertActivity.this, "You cannot add the same telemetry twice", Toast.LENGTH_SHORT).show();
                    AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                }
                else
                {
                    //Add the Telemetry Object to the Owned Telemetry List.
                    TempList.add(ServiceModel);
                    //Update the Owned Telemetry List.
                    UserObj.setOwnedServiceTelemetry(TempList);
                    //Update the Owned Telemetry in the fire store data base.
                    AppInstance.GetFireStoreInstance().SetServiceTelemetry(SetResult ->
                    {
                        if (SetResult.isSuccess())
                        {
                            Toast.makeText(InsertActivity.this, "Telemetry Item added successfully.", Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleService Successfully Updated");
                            finish();
                        }
                        else
                        {
                            Toast.makeText(InsertActivity.this, "Failed to add, " + SetResult.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("InsertActivity", "HandleService Failed: " + SetResult.getMessage());
                        }
                        AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    }, UserObj.getOwnedServiceTelemetry(), UserObj.getEmail());
                }
            }
            else
            {
                Log.d("InsertActivity", "HandleService No User Found");
            }
        }
        else
        {
            Log.d("InsertActivity", "HandleService Validation Failed");
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
            AddTelemetryButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }
    }
    //endregion
}