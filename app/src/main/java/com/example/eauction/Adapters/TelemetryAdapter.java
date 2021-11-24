package com.example.eauction.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TelemetryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CAR_TEL = 1;
    public static final int CARPLATE_TEL = 2;
    public static final int VIPNUMBER_TEL = 3;
    public static final int GENERALITEM_TEL = 4;
    public static final int LANDMARK_TEL = 5;
    public static final int SERVICE_TEL = 6;
    private static final int CURRENT_BID_ID = 123123;

    private final ArrayList<Telemetry> Telemetries;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public TelemetryAdapter(ArrayList<Telemetry> Telemetries)
    {
        this.Telemetries = Telemetries;
        Log.d("TelemetryAdapter","Telemetries Size: " + Telemetries.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        View v;
        Context context = parent.getContext();
        if(viewType == CAR_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_car, parent, false);
            holder = new CarViewHolder(v, mListener);
        }
        else if(viewType == CARPLATE_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_carplate, parent, false);
            holder = new CarPlateViewHolder(v, mListener);
        }
        else if(viewType == VIPNUMBER_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_vipphonenumber, parent, false);
            holder = new PhoneNumberViewHolder(v, mListener);
        }
        else if(viewType == LANDMARK_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_landmark, parent, false);
            holder = new LandmarkViewHolder(v, mListener);
        }
        else if(viewType == GENERALITEM_TEL){
            v = LayoutInflater.from(context).inflate(R.layout.card_general, parent, false);
            holder = new GeneralViewHolder(v, mListener);
        }
        else{
            v = LayoutInflater.from(context).inflate(R.layout.card_service, parent, false);
            holder = new ServiceViewHolder(v, mListener);
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
            addCurrent_addBasePrice((LinearLayout) carHolder.cvCarDetails.getParent(),car, false);
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
            addCurrent_addBasePrice((LinearLayout) carPlateHolder.cvCarPlate.getParent(),carPlate, false);
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
            addCurrent_addBasePrice((LinearLayout) vipPhoneHolder.cvPhoneNumberImg.getParent(),vipPhoneNumber, false);
        }
        else if(Telemetries.get(position) instanceof  Landmark)
        {
            LandmarkViewHolder landMarkHolder = (LandmarkViewHolder)holder;
            Landmark landmark = (Landmark) Telemetries.get(position);

            String cvLandmarkNameLabel = "<b><u>" + "Landmark name:" + "</u></b> ";
            String cvLandmarkTypeLabel = "<b><u>" + "Type:" + "</u></b> ";
            String cvLandmarkLocationLabel = "<b><u>" + "Location:" + "</u></b> ";
            String cvLandmarkAreaLabel = "<b><u>" + "Area:" + "</u></b> ";
            String cvLandmarkDetails = "<b><u>" + "Details:" + "</u></b> ";

            landMarkHolder.cvLandmarkName.setText(Html.fromHtml(cvLandmarkNameLabel+landmark.getName()));
            landMarkHolder.cvLandmarkType.setText(Html.fromHtml(cvLandmarkTypeLabel+landmark.getType()));
            landMarkHolder.cvLandmarkLocation.setText(Html.fromHtml(cvLandmarkLocationLabel+landmark.getLocation()));
            landMarkHolder.cvLandmarkArea.setText(Html.fromHtml(cvLandmarkAreaLabel+landmark.getArea()));
            landMarkHolder.cvLandmarkDetails.setText(Html.fromHtml(cvLandmarkDetails+landmark.getDetails()));
            landMarkHolder.cvLandmarkImg.setImageBitmap(TelemetryHelper.Base64ToImage(landmark.getImage()));
            addCurrent_addBasePrice((LinearLayout) landMarkHolder.cvLandmarkArea.getParent(),landmark, false);
        }
        else if(Telemetries.get(position) instanceof  General)
        {
            GeneralViewHolder generalViewHolder = (GeneralViewHolder)holder;
            General general = (General) Telemetries.get(position);

            String cvItemNameLabel = "<b><u>" + "Name:" + "</u></b> ";
            String cvDetailsLabel = "<b><u>" + "Details:" + "</u></b> ";

            generalViewHolder.cvItemName.setText(Html.fromHtml(cvItemNameLabel+general.getName()));
            generalViewHolder.cvDetails.setText(Html.fromHtml(cvDetailsLabel+general.getDetails()));
            generalViewHolder.cvItemGeneral.setImageBitmap(TelemetryHelper.Base64ToImage(general.getImage()));
            addCurrent_addBasePrice((LinearLayout) generalViewHolder.cvItemGeneral.getParent(),general, false);
        }
        else{
            ServiceViewHolder serviceViewHolder = (ServiceViewHolder)holder;
            Service service = (Service) Telemetries.get(position);

            String cvItemNameLabel = "<b><u>" + "Service Name:" + "</u></b> ";
            String cvDetailsLabel = "<b><u>" + "Service Details:" + "</u></b> ";

            serviceViewHolder.cvServiceName.setText(Html.fromHtml(cvItemNameLabel+service.getName()));
            serviceViewHolder.cvServiceDetails.setText(Html.fromHtml(cvDetailsLabel+service.getDetails()));
            serviceViewHolder.cvServicePic.setImageBitmap(TelemetryHelper.Base64ToImage(service.getImage()));
            addCurrent_addBasePrice((LinearLayout) serviceViewHolder.cvServiceName.getParent(),service, true);


        }
    }

    @Override
    public int getItemCount()
    {
        return Telemetries.size();
    }

    @Override
    public int getItemViewType (int position)
    {
        Log.d("TelemetryAdapter", "Adapter Position: " + position);
        if(Telemetries.get(position) instanceof Car)
            return CAR_TEL;
        else if(Telemetries.get(position) instanceof CarPlate)
            return CARPLATE_TEL;
        else if(Telemetries.get(position) instanceof VipPhoneNumber)
            return VIPNUMBER_TEL;
        else if(Telemetries.get(position) instanceof Landmark)
            return LANDMARK_TEL;
        else if(Telemetries.get(position) instanceof General)//Item is general
            return GENERALITEM_TEL;
        else
            return SERVICE_TEL;

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

        public CarViewHolder(@NonNull View itemView, OnItemClickListener listener)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(view ->
            {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
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

        public CarPlateViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view ->
            {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
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

        @BindView(R.id.cvLandmarkDetails)
        public TextView cvLandmarkDetails;

        public LandmarkViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view ->
            {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public static class PhoneNumberViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.cvPhoneNumberImg)
        public ImageView cvPhoneNumberImg;

        @BindView(R.id.cvPhoneNumber)
        public TextView cvPhoneNumber;

        @BindView(R.id.cvCompanyName)
        public TextView cvCompanyName;

        @BindView(R.id.cvCompanyDetails)
        public TextView cvCompanyDetails;

        public PhoneNumberViewHolder(@NonNull View itemView, OnItemClickListener listener)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view ->
            {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public static class GeneralViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvItemGeneral)
        public ImageView cvItemGeneral;

        @BindView(R.id.cvItemName)
        public TextView cvItemName;

        @BindView(R.id.cvDetails)
        public TextView cvDetails;

        public GeneralViewHolder(@NonNull View itemView, OnItemClickListener listener)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view ->
            {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
    public static class ServiceViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvServicePic)
        public ImageView cvServicePic;

        @BindView(R.id.cvServiceName)
        public TextView cvServiceName;

        @BindView(R.id.cvServiceDetails)
        public TextView cvServiceDetails;

        public ServiceViewHolder(@NonNull View itemView, OnItemClickListener listener)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view ->
            {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    private void addCurrent_addBasePrice(LinearLayout linearLayout, Telemetry telemetry, boolean isService)
    {
        String currentBid = "<b><u>" + "Current Bid:" + "</u></b> "+telemetry.getCurrentBid();
        String basePrice = "<b><u>" + "Base Price:" + "</u></b> "+telemetry.getBasePrice();

            TextView currentBidTV = new TextView(linearLayout.getContext());
            TextView basePriceTV = new TextView(linearLayout.getContext());
            currentBidTV.setPadding(8,0,0,0);
            basePriceTV.setPadding(8,0,0,0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
            (
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
        params.setMargins(0, 0, 0, 16);

        currentBidTV.setText(Html.fromHtml(currentBid));
        currentBidTV.setId(CURRENT_BID_ID);
        String actName = ((Activity)linearLayout.getContext()).getLocalClassName();
        String fragmentName = ((AppCompatActivity)linearLayout.getContext())
                .getSupportFragmentManager()
                .getFragments()
                .get(0)
                .getClass()
                .getName();
        fragmentName = fragmentName.substring(fragmentName.lastIndexOf('.') + 1);
        Log.i("fragment",fragmentName);

        if(actName.equals("MainActivity") && isService && fragmentName.equals("GlobalAuctionsFragment")) {
            currentBidTV.setVisibility(View.GONE);
        }


        basePriceTV.setText(Html.fromHtml(basePrice));
        currentBidTV.setLayoutParams(params);
        basePriceTV.setLayoutParams(params);

        linearLayout.addView(currentBidTV);
        linearLayout.addView(basePriceTV);

        String auctionStart = "<b><u>" + "Auction Start:" + "</u></b> " + telemetry.getAuctionStart();
        TextView auctionStartTV = new TextView(linearLayout.getContext());
        auctionStartTV.setPadding(8, 0, 0, 0);
        auctionStartTV.setText(Html.fromHtml(auctionStart));
        auctionStartTV.setLayoutParams(params);
        linearLayout.addView(auctionStartTV);

        String auctionEnd = "<b><u>" + "Auction End:" + "</u></b> " + telemetry.getAuctionEnd();
        TextView auctionEndTV = new TextView(linearLayout.getContext());
        auctionEndTV.setPadding(8, 0, 0, 0);
        auctionEndTV.setText(Html.fromHtml(auctionEnd));
        auctionEndTV.setLayoutParams(params);
        linearLayout.addView(auctionEndTV);
    }
}
