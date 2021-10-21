package com.example.eauction.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eauction.Adapters.TelemetryAdapter;
import com.example.eauction.InsertActivity;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.R;
import com.royrodriguez.transitionbutton.TransitionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPropertiesFragment extends Fragment {

    @BindView(R.id.RvTelemetry)
    public RecyclerView RvTelemetry;

    private TelemetryAdapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.BtnAddTelemetryInsertActivity)
    TransitionButton BtnAddTelemetryInsertActivity;

    private User UserObj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myproperties,container,false);
        ButterKnife.bind(this,view);
        layoutManager = new LinearLayoutManager(getContext());
        ArrayList<Telemetry> userTelemertries = Merge(UserObj.getOwnedCarPlateTelemetry(), UserObj.getOwnedCarTelemetry(), UserObj.getOwnedLandmarkTelemetry(), UserObj.getOwnedVipPhoneTelemetry(), UserObj.getOwnedGeneralTelemetry());
        rvAdapter = new TelemetryAdapter(userTelemertries);
        RvTelemetry.setLayoutManager(layoutManager);
        RvTelemetry.setAdapter(rvAdapter);

        rvAdapter.setOnItemClickListener(new TelemetryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Telemetry selectedTelemery = userTelemertries.get(position); //Item Selected //Todo Sabi Update this telemetery based on the logic you need

//                if(selectedTelemery instanceof CarPlate){
//                    //Do stuff
//                }

                //region Dialog Settings
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.auction_dialog);
                EditText EtBasePrice = dialog.findViewById(R.id.EtBasePrice);
                TransitionButton BtnSubmitAuction = dialog.findViewById(R.id.BtnSubmitAuction);
                //endregion

                dialog.show();
                BtnSubmitAuction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        double basePrice = Double.parseDouble(EtBasePrice.getText().toString()); //TODO Sabi Apply the logic you need for the baseprice then update object and set it as auctioned
                        Toast.makeText(getContext(), basePrice+"", Toast.LENGTH_SHORT).show();
                        selectedTelemery.setBasePrice(basePrice);
                        dialog.hide();
                    }
                });

            }
        });

        //region EventListener
        BtnAddTelemetryInsertActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),InsertActivity.class));
            }
        });
        //endregion

        return view;
    }
    @SafeVarargs
    private ArrayList<Telemetry> Merge(@NonNull ArrayList<? extends Telemetry>...args)
    {
        ArrayList<Telemetry> returnTelemetries = new ArrayList<>();
        for (ArrayList<? extends Telemetry> arg: args)
        {
            if(arg != null && !arg.isEmpty())
                returnTelemetries.addAll(arg);
        }
        return returnTelemetries;
    }

    public void SetUser(User UserObj)
    {
        this.UserObj = UserObj;
    }

}
