package com.example.eauction.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TelemetryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int CAR_TEL = 1;
    public static final int CARPLATE_TEL = 2;
    public static final int VIPNUMBER_TEL = 3;
    public static final int GENERALITEM_TEL = 4;
    public static final int LANDMARK_TEL = 5;

    private ArrayList<Telemetry> Telemetries;

    public TelemetryAdapter(ArrayList<Telemetry> Telemetries){
        this.Telemetries = Telemetries;
        Log.i("TelCount",Telemetries.size() + "");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        View v;
        Context context = parent.getContext();
        if(viewType == CAR_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_car, parent, false);
            holder = new CarViewHolder(v);
        }
        else if(viewType == CARPLATE_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_carplate, parent, false);
            holder = new CarPlateViewHolder(v);
        }
        else if(viewType == VIPNUMBER_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_vipphonenumber, parent, false);
            holder = new PhoneNumberViewHolder(v);
        }
        else if(viewType == LANDMARK_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_landmark, parent, false);
            holder = new LandmarkViewHolder(v);
        }
        else{
            v = LayoutInflater.from(context).inflate(R.layout.card_general, parent, false);
            holder = new GeneralViewHolder(v);
        }

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        if(Telemetries.get(position) instanceof  Car)
        {
            CarViewHolder carHolder = (CarViewHolder)holder;
            Car car = (Car)Telemetries.get(position);

            String carNameLabel = "<b><u>" + "Car name:" + "</u></b> ";
            String carModelLabel = "<b><u>" + "Car model:" + "</u></b> ";
            String carMillageLabel = "<b><u>" + "Millage:" + "</u></b> ";
            String carHorsePowerLabel = "<b><u>" + "Horse power:" + "</u></b> ";
            String carDetailsLabel = "<b><u>" + "Details:" + "</u></b> ";


            carHolder.cvCarName.setText(Html.fromHtml(carNameLabel+car.getName()));
            carHolder.cvCarModel.setText(Html.fromHtml(carModelLabel+car.getModel()));
            carHolder.cvCarMilage.setText(Html.fromHtml(carMillageLabel+car.getMileage()));
            carHolder.cvHorsePower.setText(Html.fromHtml(carHorsePowerLabel+car.getHorsePower()));
            carHolder.cvCarDetails.setText(Html.fromHtml(carDetailsLabel+car.getDetails()));
            carHolder.cvCarImage.setImageBitmap(TelemetryHelper.Base64ToImage(car.getImage()));
        }
        else if(Telemetries.get(position) instanceof  CarPlate)
        {
            CarPlateViewHolder carPlateHolder = (CarPlateViewHolder)holder;
            CarPlate carPlate = (CarPlate) Telemetries.get(position);

            String carPlateCodeLabel = "<b><u>" + "Plate code:" + "</u></b> ";
            String carDetailsLabel = "<b><u>" + "Details:" + "</u></b> ";
            String plateNumberLabel = "<b><u>" + "Plate number:" + "</u></b> ";
            String emirateLabel = "<b><u>" + "Emirate:" + "</u></b> ";


            carPlateHolder.cvPlateCode.setText(Html.fromHtml(carPlateCodeLabel+carPlate.getPlateCode()));
            carPlateHolder.cvPlateDetails.setText(Html.fromHtml(carDetailsLabel+carPlate.getDetails()));
            carPlateHolder.cvPlateNumber.setText(Html.fromHtml(plateNumberLabel+carPlate.getPlateNumber()));
            carPlateHolder.cvEmirate.setText(Html.fromHtml(emirateLabel+carPlate.getEmirate()));
            carPlateHolder.cvCarPlate.setImageBitmap(TelemetryHelper.Base64ToImage(carPlate.getImage()));
        }
        else if(Telemetries.get(position) instanceof  VipPhoneNumber)
        {
            PhoneNumberViewHolder vipPhoneHolder = (PhoneNumberViewHolder) holder;
            VipPhoneNumber vipPhoneNumber = (VipPhoneNumber) Telemetries.get(position);

            String cvPhoneNumberLabel = "<b><u>" + "Phone number:" + "</u></b> ";
            String cvCompanyNameLabel = "<b><u>" + "Company name:" + "</u></b> ";
            String cvCompanyDetailsLabel = "<b><u>" + "Details:" + "</u></b> ";


            vipPhoneHolder.cvPhoneNumber.setText(Html.fromHtml(cvPhoneNumberLabel+vipPhoneNumber.getPhoneNumber()));
            vipPhoneHolder.cvCompanyName.setText(Html.fromHtml(cvCompanyNameLabel+vipPhoneNumber.getCompany()));
            vipPhoneHolder.cvCompanyDetails.setText(Html.fromHtml(cvCompanyDetailsLabel+vipPhoneNumber.getDetails()));
            vipPhoneHolder.cvPhoneNumberImg.setImageBitmap(TelemetryHelper.Base64ToImage(vipPhoneNumber.getImage()));
        }
        else if(Telemetries.get(position) instanceof  Landmark)
        {
            LandmarkViewHolder landMarkHolder = (LandmarkViewHolder)holder;
            Landmark landmark = (Landmark) Telemetries.get(position);

            String cvLandmarkNameLabel = "<b><u>" + "Landmark name:" + "</u></b> ";
            String cvLandmarkTypeLabel = "<b><u>" + "Type:" + "</u></b> ";
            String cvLandmarkLocationLabel = "<b><u>" + "Location:" + "</u></b> ";
            String cvLandmarkAreaLabel = "<b><u>" + "Area:" + "</u></b> ";

            landMarkHolder.cvLandmarkName.setText(Html.fromHtml(cvLandmarkNameLabel+landmark.getName()));
            landMarkHolder.cvLandmarkType.setText(Html.fromHtml(cvLandmarkTypeLabel+landmark.getType()));
            landMarkHolder.cvLandmarkLocation.setText(Html.fromHtml(cvLandmarkLocationLabel+landmark.getLocation()));
            landMarkHolder.cvLandmarkArea.setText(Html.fromHtml(cvLandmarkAreaLabel+landmark.getArea()));
            landMarkHolder.cvLandmarkImg.setImageBitmap(TelemetryHelper.Base64ToImage(landmark.getImage()));
        }
        else
        {
            GeneralViewHolder generalViewHolder = (GeneralViewHolder)holder;
            General general = (General) Telemetries.get(position);

            String cvItemNameLabel = "<b><u>" + "Name:" + "</u></b> ";
            String cvDetailsLabel = "<b><u>" + "Details:" + "</u></b> ";

            generalViewHolder.cvItemName.setText(Html.fromHtml(cvItemNameLabel+general.getName()));
            generalViewHolder.cvDetails.setText(Html.fromHtml(cvDetailsLabel+general.getDetails()));
            generalViewHolder.cvItemGeneral.setImageBitmap(TelemetryHelper.Base64ToImage(general.getImage()));
        }
    }

    @Override
    public int getItemCount() {
        return Telemetries.size();
    }

    @Override
    public int getItemViewType (int position) {

        Log.i("position",position+"");
        if(Telemetries.get(position) instanceof Car)
            return CAR_TEL;
        else if(Telemetries.get(position) instanceof CarPlate)
            return CARPLATE_TEL;
        else if(Telemetries.get(position) instanceof VipPhoneNumber)
            return VIPNUMBER_TEL;
        else if(Telemetries.get(position) instanceof Landmark)
            return LANDMARK_TEL;
        else //Item is general
            return GENERALITEM_TEL;
    }
    public static class CarViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvCarImage)
        public ImageView cvCarImage;

        @BindView(R.id.cvCarName)
        public TextView cvCarName;

        @BindView(R.id.cvCarMilage)
        public TextView cvCarMilage;

        @BindView(R.id.cvCarModel)
        public TextView cvCarModel;

        @BindView(R.id.cvHorsePower)
        public TextView cvHorsePower;

        @BindView(R.id.cvCarDetails)
        public TextView cvCarDetails;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class CarPlateViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvCarPlate)
        public ImageView cvCarPlate;

        @BindView(R.id.cvPlateNumber)
        public TextView cvPlateNumber;

        @BindView(R.id.cvPlateCode)
        public TextView cvPlateCode;

        @BindView(R.id.cvEmirate)
        public TextView cvEmirate;

        @BindView(R.id.cvPlateDetails)
        public TextView cvPlateDetails;

        public CarPlateViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class LandmarkViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvLandmarkImg)
        public ImageView cvLandmarkImg;

        @BindView(R.id.cvLandmarkName)
        public TextView cvLandmarkName;

        @BindView(R.id.cvLandmarkType)
        public TextView cvLandmarkType;

        @BindView(R.id.cvLandmarkLocation)
        public TextView cvLandmarkLocation;

        @BindView(R.id.cvLandmarkArea)
        public TextView cvLandmarkArea;

        public LandmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class PhoneNumberViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvPhoneNumberImg)
        public ImageView cvPhoneNumberImg;

        @BindView(R.id.cvPhoneNumber)
        public TextView cvPhoneNumber;

        @BindView(R.id.cvCompanyName)
        public TextView cvCompanyName;

        @BindView(R.id.cvCompanyDetails)
        public TextView cvCompanyDetails;

        public PhoneNumberViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class GeneralViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvItemGeneral)
        public ImageView cvItemGeneral;

        @BindView(R.id.cvItemName)
        public TextView cvItemName;

        @BindView(R.id.cvDetails)
        public TextView cvDetails;

        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
