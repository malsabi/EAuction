package com.example.eauction.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        Log.i("TelCount",Telemetries.size()+"");
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(Telemetries.get(position) instanceof  Car){
            CarViewHolder carHolder = (CarViewHolder)holder;
            Car car = (Car)Telemetries.get(position);

            carHolder.cvCarName.setText(car.getName());
            carHolder.cvCarModel.setText(car.getModel());
            carHolder.cvCarMilage.setText(String.valueOf(car.getMileage()));
            carHolder.cvHorsePower.setText(String.valueOf(car.getHorsePower()));
            carHolder.cvCarDetails.setText(car.getDetails());

            //TODO SetImage
        }
        else if(Telemetries.get(position) instanceof  CarPlate){
            CarPlateViewHolder carPlateHolder = (CarPlateViewHolder)holder;
            CarPlate carPlate = (CarPlate) Telemetries.get(position);

            carPlateHolder.cvPlateCode.setText(carPlate.getPlateCode());
            carPlateHolder.cvPlateDetails.setText(carPlate.getDetails());
            carPlateHolder.cvPlateNumber.setText(carPlate.getPlateNumber());
            carPlateHolder.cvEmirate.setText(carPlate.getEmirate());

            //TODO SetImage
        }
        else if(Telemetries.get(position) instanceof  VipPhoneNumber){
            PhoneNumberViewHolder vipPhoneHolder = (PhoneNumberViewHolder) holder;
            VipPhoneNumber vipPhoneNumber = (VipPhoneNumber) Telemetries.get(position);

            vipPhoneHolder.cvPhoneNumber.setText(vipPhoneNumber.getPhoneNumber());
            vipPhoneHolder.cvCompanyName.setText(vipPhoneNumber.getCompany());
            vipPhoneHolder.cvCompanyDetails.setText(vipPhoneNumber.getDetails());

            //TODO SetImage
        }
        else if(Telemetries.get(position) instanceof  Landmark){
            LandmarkViewHolder landMarkHolder = (LandmarkViewHolder)holder;
            Landmark landmark = (Landmark) Telemetries.get(position);

            landMarkHolder.cvLandmarkName.setText(landmark.getName());
            landMarkHolder.cvLandmarkType.setText(landmark.getType());
            landMarkHolder.cvLandmarkLocation.setText(landmark.getLocation());
            landMarkHolder.cvLandmarkArea.setText(String.valueOf(landmark.getArea()));

            //TODO SetImage
        }
        else{
            GeneralViewHolder carPlateHolder = (GeneralViewHolder)holder;
            General general = (General) Telemetries.get(position);

            carPlateHolder.cvItemName.setText(general.getName());
            carPlateHolder.cvDetails.setText(general.getDetails());

            //TODO SetImage
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
