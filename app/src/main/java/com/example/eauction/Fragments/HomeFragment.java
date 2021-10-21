package com.example.eauction.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eauction.Interfaces.FragmentChangeListener;
import com.example.eauction.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment{

    @BindView(R.id.BtnCarAuctions)
    public ImageButton BtnCarAuctions;

    @BindView(R.id.BtnLandmarkAuctions)
    public ImageButton BtnLandmarkAuctions;

    @BindView(R.id.BtnVipAuctions)
    public ImageButton BtnVipAuctions;

    @BindView(R.id.BtnPlateCodeAuctions)
    public ImageButton BtnPlateCodeAuctions;

    @BindView(R.id.BtnGeneralItemAuctions)
    public ImageButton BtnGeneralItemAuctions;

    @BindView(R.id.BtnServiceAuctions)
    public ImageButton BtnServiceAuctions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this,view);

        //region Switching to Auctioned Properties Fragment
        BtnCarAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment globalAuctionsFragment = new GlobalAuctionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AuctionNeeded","CarAuction");
                globalAuctionsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, globalAuctionsFragment).commit();
            }
        });
        BtnLandmarkAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment globalAuctionsFragment = new GlobalAuctionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AuctionNeeded","LandmarkAuction");
                globalAuctionsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, globalAuctionsFragment).commit();
            }
        });
        BtnVipAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment globalAuctionsFragment = new GlobalAuctionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AuctionNeeded","VipAuction");
                globalAuctionsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, globalAuctionsFragment).commit();
            }
        });
        BtnPlateCodeAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment globalAuctionsFragment = new GlobalAuctionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AuctionNeeded","PlateCodeAuction");
                globalAuctionsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, globalAuctionsFragment).commit();
            }
        });
        BtnGeneralItemAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment globalAuctionsFragment = new GlobalAuctionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AuctionNeeded","GeneralAuction");
                globalAuctionsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, globalAuctionsFragment).commit();
            }
        });
        BtnServiceAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment globalAuctionsFragment = new GlobalAuctionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AuctionNeeded","ServiceAuction");
                globalAuctionsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, globalAuctionsFragment).commit();
            }
        });
        //endregion
        return view;

    }
}
