package com.example.eauction.Utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import com.example.eauction.DataBase.FireStoreManager;
import com.example.eauction.Enums.StatusEnum;
import com.example.eauction.Helpers.DateHelper;
import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Interfaces.SetUserTelemetryCallback;
import com.example.eauction.Models.Bid;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.ServiceComment;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.Models.VipPhoneNumber;

import java.time.LocalDateTime;
import java.util.ArrayList;

// class name: TelemetryMonitor
// Features::
//  1. Process the finished auctions and get the highest bid to send the auctioned item.
//  2. Delete finished auctions from global-auctions and update the user-auctions.
// How it works::
//  1. Grab the user ID from login activity.
//  2. Grab the auctions list owned by the user and monitor them.
//  3. Grab the services list owned by the user and monitor them.
public class TelemetryMonitor
{
    //region Constants
    //1000ms = 1 second
    private final int MonitorIntervalTime = 1000 * 5;
    //endregion

    //region Fields
    private final FireStoreManager FireStore;
    private boolean IsMonitorRunning;
    private Thread ThreadMonitor;
    //endregion

    //region Constructors
    public TelemetryMonitor(FireStoreManager FireStore)
    {
        this.FireStore = FireStore;
        this.IsMonitorRunning = false;
        this.ThreadMonitor = null;
    }
    //endregion

    //region Private Methods
    @SafeVarargs
    private ArrayList<Telemetry> Merge(@NonNull ArrayList<? extends Telemetry>...args)
    {
        ArrayList<Telemetry> returnTelemetries = new ArrayList<>();
        for (ArrayList<? extends Telemetry> arg: args)
        {
            if(arg != null && !arg.isEmpty())
            {
                returnTelemetries.addAll(arg);
            }
        }
        return returnTelemetries;
    }

    private void DeleteGlobalAuction(String TelemetryType, Telemetry TelemetryAuctionItem)
    {
        switch (TelemetryType)
        {
            case "CarPlate":
            {
                CarPlate CarPlateAuction = (CarPlate)TelemetryAuctionItem;

                CarPlateAuction.setAuctionEnd("Finished");

                FireStore.DeleteCarPlateAuction(Result ->
                {
                    if (Result)
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteCarPlateAuction: Successfully deleted.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteCarPlateAuction: failed to delete.");
                    }
                }, CarPlateAuction);
            }
            break;
            case "Car":
            {
                Car CarAuction = (Car)TelemetryAuctionItem;
                FireStore.DeleteCarAuction(Result ->
                {
                    if (Result)
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteCarAuction: Successfully deleted.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteCarAuction: failed to delete.");
                    }
                }, CarAuction);
            }
            break;
            case "Landmark":
            {
                Landmark LandmarkAuction = (Landmark)TelemetryAuctionItem;
                FireStore.DeleteLandmarkAuction(Result ->
                {
                    if (Result)
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteLandmarkAuction: Successfully deleted.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteLandmarkAuction: failed to delete.");
                    }
                }, LandmarkAuction);
            }
            break;
            case "VipPhoneNumber":
            {
                VipPhoneNumber VipPhoneNumberAuction = (VipPhoneNumber)TelemetryAuctionItem;
                FireStore.DeleteVIPPhoneNumberAuction(Result ->
                {
                    if (Result)
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteVIPPhoneNumberAuction: Successfully deleted.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteVIPPhoneNumberAuction: failed to delete.");
                    }
                }, VipPhoneNumberAuction);
            }
            break;
            case "General":
            {
                General GeneralAuction = (General)TelemetryAuctionItem;
                FireStore.DeleteGeneralAuction(Result ->
                {
                    if (Result)
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteGeneralAuction: Successfully deleted.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteGeneralAuction: failed to delete.");
                    }
                }, GeneralAuction);
            }
            break;
            case "Service":
            {
                Service ServiceAuction = (Service)TelemetryAuctionItem;
                FireStore.DeleteServiceAuction(Result ->
                {
                    if (Result)
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteServiceAuction: Successfully deleted.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "DeleteGlobalAuction:: DeleteServiceAuction: failed to delete.");
                    }
                }, ServiceAuction);
            }
            break;
        }
    }
    private void UpdateUserAuction(String TelemetryType, Telemetry TelemetryAuctionItem, User UserObj, boolean DeleteTelemetry)
    {
        switch (TelemetryType)
        {
            case "CarPlate":
            {
                CarPlate CarPlateAuction = (CarPlate) TelemetryAuctionItem;
                int Index = TelemetryHelper.GetTelemetryIndex(UserObj.getOwnedCarPlateTelemetry(), CarPlateAuction.getID());
                if (DeleteTelemetry)
                {
                    UserObj.getOwnedCarPlateTelemetry().remove(Index);
                }
                else
                {
                    UserObj.getOwnedCarPlateTelemetry().set(Index, CarPlateAuction);
                }
                FireStore.SetCarPlateTelemetry(Result ->
                {
                    if (Result.isSuccess())
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetCarPlateTelemetry: Successfully updated.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetCarPlateTelemetry: Failed to be update, " + Result.getMessage());
                    }
                }, UserObj.getOwnedCarPlateTelemetry(), UserObj.getEmail());
            }
            break;
            case "Car":
            {
                Car CarAuction = (Car) TelemetryAuctionItem;
                int Index = TelemetryHelper.GetTelemetryIndex(UserObj.getOwnedCarTelemetry(), CarAuction.getID());
                if (DeleteTelemetry)
                {
                    UserObj.getOwnedCarTelemetry().remove(Index);
                }
                else
                {
                    UserObj.getOwnedCarTelemetry().set(Index, CarAuction);
                }
                FireStore.SetCarTelemetry(Result ->
                {
                    if (Result.isSuccess())
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetCarTelemetry: Successfully updated.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetCarTelemetry: Failed to be update, " + Result.getMessage());
                    }
                }, UserObj.getOwnedCarTelemetry(), UserObj.getEmail());
            }
            break;
            case "Landmark":
            {
                Landmark LandmarkAuction = (Landmark) TelemetryAuctionItem;
                int Index = TelemetryHelper.GetTelemetryIndex(UserObj.getOwnedLandmarkTelemetry(), LandmarkAuction.getID());
                if (DeleteTelemetry)
                {
                    UserObj.getOwnedLandmarkTelemetry().remove(Index);
                }
                else
                {
                    UserObj.getOwnedLandmarkTelemetry().set(Index, LandmarkAuction);
                }
                FireStore.SetLandmarkTelemetry(Result ->
                {
                    if (Result.isSuccess())
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetLandmarkTelemetry: Successfully updated.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetLandmarkTelemetry: Failed to be update, " + Result.getMessage());
                    }
                }, UserObj.getOwnedLandmarkTelemetry(), UserObj.getEmail());
            }
            break;
            case "VipPhoneNumber":
            {
                VipPhoneNumber VipPhoneNumberAuction = (VipPhoneNumber) TelemetryAuctionItem;
                int Index = TelemetryHelper.GetTelemetryIndex(UserObj.getOwnedVipPhoneTelemetry(), VipPhoneNumberAuction.getID());
                if (DeleteTelemetry)
                {
                    UserObj.getOwnedVipPhoneTelemetry().remove(Index);
                }
                else
                {
                    UserObj.getOwnedVipPhoneTelemetry().set(Index, VipPhoneNumberAuction);
                }
                FireStore.SetVipPhoneTelemetry(Result ->
                {
                    if (Result.isSuccess())
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetVipPhoneTelemetry: Successfully updated.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetVipPhoneTelemetry: Failed to be update, " + Result.getMessage());
                    }
                }, UserObj.getOwnedVipPhoneTelemetry(), UserObj.getEmail());
            }
            break;
            case "General":
            {
                General GeneralAuction = (General) TelemetryAuctionItem;
                int Index = TelemetryHelper.GetTelemetryIndex(UserObj.getOwnedGeneralTelemetry(), GeneralAuction.getID());
                if (DeleteTelemetry)
                {
                    UserObj.getOwnedGeneralTelemetry().remove(Index);
                }
                else
                {
                    UserObj.getOwnedGeneralTelemetry().set(Index, GeneralAuction);
                }
                FireStore.SetGeneralTelemetry(Result ->
                {
                    if (Result.isSuccess())
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetGeneralTelemetry: Successfully updated.");
                    }
                    else
                    {
                        Log.d("TelemetryMonitor", "UpdateUserAuction:: SetGeneralTelemetry: Failed to be update, " + Result.getMessage());
                    }
                }, UserObj.getOwnedGeneralTelemetry(), UserObj.getEmail());
            }
            break;
        }
    }
    private void ProcessHighestBid(Bid HighestBid, String TelemetryType, Telemetry TelemetryAuctionItem)
    {
        FireStore.GetUserInformation(UserObj ->
        {
            if (UserObj != null)
            {
                Log.d("TelemetryMonitor", "ProcessHighestBid:: GetUserInformation: id: " + UserObj.getEmail());
                switch (TelemetryType)
                {
                    case "CarPlate":
                    {
                        CarPlate CarPlateAuction = (CarPlate) TelemetryAuctionItem;
                        CarPlateAuction.setStatus(StatusEnum.UnAuctioned);
                        UserObj.getOwnedCarPlateTelemetry().add(CarPlateAuction);
                        FireStore.SetCarPlateTelemetry(Result ->
                        {
                            if (Result.isSuccess())
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetCarPlateTelemetry: Successfully");
                            }
                            else
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetCarPlateTelemetry: Failed, " + Result.getMessage());
                            }
                        },  UserObj.getOwnedCarPlateTelemetry(), HighestBid.getUserId());
                    }
                    break;
                    case "Car":
                    {
                        Car CarAuction = (Car) TelemetryAuctionItem;
                        CarAuction.setStatus(StatusEnum.UnAuctioned);
                        UserObj.getOwnedCarTelemetry().add(CarAuction);
                        FireStore.SetCarTelemetry(Result ->
                        {
                            if (Result.isSuccess())
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetCarTelemetry: Successfully");
                            }
                            else
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetCarTelemetry: Failed, " + Result.getMessage());
                            }
                        },  UserObj.getOwnedCarTelemetry(), HighestBid.getUserId());
                    }
                    break;
                    case "Landmark":
                    {
                        Landmark LandmarkAuction = (Landmark) TelemetryAuctionItem;
                        LandmarkAuction.setStatus(StatusEnum.UnAuctioned);
                        UserObj.getOwnedLandmarkTelemetry().add(LandmarkAuction);
                        FireStore.SetLandmarkTelemetry(Result ->
                        {
                            if (Result.isSuccess())
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetLandmarkTelemetry: Successfully");
                            }
                            else
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetLandmarkTelemetry: Failed, " + Result.getMessage());
                            }
                        },  UserObj.getOwnedLandmarkTelemetry(), HighestBid.getUserId());
                    }
                    break;
                    case "VipPhoneNumber":
                    {
                        VipPhoneNumber VipPhoneNumberAuction = (VipPhoneNumber) TelemetryAuctionItem;
                        VipPhoneNumberAuction.setStatus(StatusEnum.UnAuctioned);
                        UserObj.getOwnedVipPhoneTelemetry().add(VipPhoneNumberAuction);
                        FireStore.SetVipPhoneTelemetry(Result ->
                        {
                            if (Result.isSuccess())
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetVipPhoneTelemetry: Successfully");
                            }
                            else
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetVipPhoneTelemetry: Failed, " + Result.getMessage());
                            }
                        },  UserObj.getOwnedVipPhoneTelemetry(), HighestBid.getUserId());
                    }
                    break;
                    case "General":
                    {
                        General GeneralAuction = (General) TelemetryAuctionItem;
                        GeneralAuction.setStatus(StatusEnum.UnAuctioned);
                        UserObj.getOwnedGeneralTelemetry().add(GeneralAuction);
                        FireStore.SetGeneralTelemetry(Result ->
                        {
                            if (Result.isSuccess())
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetGeneralTelemetry: Successfully");
                            }
                            else
                            {
                                Log.d("TelemetryMonitor", "ProcessHighestBid:: SetGeneralTelemetry: Failed, " + Result.getMessage());
                            }
                        },  UserObj.getOwnedGeneralTelemetry(), HighestBid.getUserId());
                    }
                    break;
                }
            }
            else
            {
                Log.d("TelemetryMonitor", "ProcessHighestBid:: GetUserInformation: null");
            }
        }, HighestBid.getUserId());
    }
    public void UpdateUserService(Service ServerAuction, User UserObj)
    {
        int Index = TelemetryHelper.GetTelemetryIndex(UserObj.getOwnedServiceTelemetry(), ServerAuction.getID());
        UserObj.getOwnedServiceTelemetry().set(Index, ServerAuction);
        FireStore.SetServiceTelemetry(Result ->
        {
            if (Result.isSuccess())
            {
                Log.d("TelemetryMonitor", "UpdateUserService:: SetGeneralTelemetry: Successfully updated.");
            }
            else
            {
                Log.d("TelemetryMonitor", "UpdateUserService:: SetGeneralTelemetry: Failed to be update, " + Result.getMessage());
            }
        }, UserObj.getOwnedServiceTelemetry(), UserObj.getEmail());
    }
    public void ProcessLowestBid(Service ServiceAuction, ServiceComment LowestCostService, User UserObj)
    {
        Log.d("TelemetryMonitor", "ProcessLowestBid:: LowestCostService Cost: " + LowestCostService.getCost());
        String Email = FireStore.DecryptField(UserObj.getEmail());
        Log.d("TelemetryMonitor", "ProcessLowestBid:: Owner Service Email Address: " + Email);
        String Title = "Service Provider for [" + ServiceAuction.getName() + "]";
        String MessageInformation = String.format("Service Provider Information\n\nName: %s\nPhone Number: %s\nEmail Address: %s\nCost: %s\nComment: %s", LowestCostService.getName(), LowestCostService.getPhoneNumber(), LowestCostService.getEmail(), LowestCostService.getCost(), LowestCostService.getComment());
        Log.d("TelemetryMonitor", "ProcessLowestBid:: \n Title: " + Title + "\nMessageInformation: " + MessageInformation);
        TelemetryHelper.SendServiceInformation(FireStore.GetCompanyAccount(), FireStore.GetCompanyPassword(), Email, Title, MessageInformation);
        Log.d("TelemetryMonitor", "ProcessLowestBid:: Finished");
    }
    //endregion

    //region Public Methods
    //Starts monitoring all of the auctions and processes them.
    public void StartMonitor(final String UserId)
    {
        if (!IsMonitorRunning)
        {
            Log.d("TelemetryMonitor", "StartMonitor:: Starting Telemetry Monitor, Interval Time is: " + MonitorIntervalTime);
            IsMonitorRunning = true;
            ThreadMonitor = new Thread(() ->
            {
                try
                {
                    while (IsMonitorRunning)
                    {
                        Log.d("TelemetryMonitor", "StartMonitor:: Running in each " + MonitorIntervalTime + " Seconds.");

                        FireStore.GetUserInformation(UserObj ->
                        {
                            ArrayList<Telemetry> UserTelemetries = Merge(UserObj.getOwnedCarPlateTelemetry(), UserObj.getOwnedCarTelemetry(), UserObj.getOwnedLandmarkTelemetry(), UserObj.getOwnedVipPhoneTelemetry(), UserObj.getOwnedGeneralTelemetry(), UserObj.getOwnedServiceTelemetry());
                            Log.d("TelemetryMonitor", "StartMonitor:: Received User Telemetries: " + UserTelemetries.size());
                            LocalDateTime CurrentTime = LocalDateTime.now();
                            for (Telemetry T : UserTelemetries)
                            {
                                if (DateHelper.IsDateValid(T.getAuctionEnd()))
                                {
                                    LocalDateTime TelemetryEndTime = DateHelper.ParseDateTime(T.getAuctionEnd());
                                    if (CurrentTime.isEqual(TelemetryEndTime) || CurrentTime.isBefore(TelemetryEndTime))
                                    {
                                        Log.d("TelemetryMonitor", "Telemetry Owner Id: " + T.getOwnerId() + " due date has been reached");
                                        //Finished
                                        T.setAuctionEnd("Finished");
                                        //Process
                                        if (T instanceof Service)
                                        {
                                            Log.d("TelemetryMonitor", "Telemetry is Service Auction");
                                            Service ServiceAuction = (Service)T;
                                            //Pass the lowest cost service to the user owner.
                                            if (ServiceAuction.getServiceComments().size() > 0)
                                            {
                                                ServiceComment LowestCostService = ServiceAuction.getServiceComments().get(0);
                                                for (ServiceComment SC : ServiceAuction.getServiceComments())
                                                {
                                                    if (Integer.parseInt(LowestCostService.getCost()) < Integer.parseInt(SC.getCost()))
                                                    {
                                                        LowestCostService.Set(SC);
                                                    }
                                                }
                                                //Update the user that the service had comments and send him the lowest cost service provider in email.
                                                ProcessLowestBid(ServiceAuction, LowestCostService, UserObj);
                                            }
                                            //Update the service to be finished.
                                            UpdateUserService(ServiceAuction, UserObj);
                                        }
                                        else
                                        {
                                            Log.d("TelemetryMonitor", "Telemetry is Normal Auction");
                                            if (T.getCurrentBid() > 0 && T.getBids().size() > 0)
                                            {
                                                Bid HighestBid = T.getBids().get(0);
                                                //Pass the telemetry to the highest bid winner.
                                                for (Bid B : T.getBids())
                                                {
                                                    if (HighestBid.getCurrentBid() < B.getCurrentBid())
                                                    {
                                                        HighestBid.setCurrentBid(B.getCurrentBid());
                                                        HighestBid.setUserId(B.getUserId());
                                                    }
                                                }
                                                ProcessHighestBid(HighestBid, TelemetryHelper.GetTelemetryType(T), T);
                                                //Remove telemetry from the owner.
                                                UpdateUserAuction(TelemetryHelper.GetTelemetryType(T), T, UserObj, true);
                                            }
                                            else
                                            {
                                                //Update the telemetry to be finished.
                                                UpdateUserAuction(TelemetryHelper.GetTelemetryType(T), T, UserObj, false);
                                            }
                                        }
                                        //Delete the auction from the global auctions.
                                        DeleteGlobalAuction(TelemetryHelper.GetTelemetryType(T), T);
                                    }
                                }
                            }
                        }, UserId);
                        Thread.sleep(MonitorIntervalTime);
                    }
                }
                catch (Exception e)
                {
                    Log.d("TelemetryMonitor", "StartMonitor:: Exception: " + e.getMessage());
                    StopMonitor();
                }
            });
            ThreadMonitor.start();
        }
        else
        {
            Log.d("TelemetryMonitor", "StartMonitor:: Telemetry Monitor is already running");
        }
    }

    //Stops monitoring and terminates all processes.
    public void StopMonitor()
    {
        if (IsMonitorRunning)
        {
            Log.d("TelemetryMonitor", "StopMonitor:: Telemetry Monitor is running and will be closed.");
            IsMonitorRunning = false;
            if (ThreadMonitor != null)
            {
                ThreadMonitor.interrupt();
                ThreadMonitor = null;
                Log.d("TelemetryMonitor", "StopMonitor:: Telemetry Monitor thread is disposed.");
            }
            Log.d("TelemetryMonitor", "StopMonitor:: Telemetry Monitor is closed successfully.");
        }
        else
        {
            Log.d("TelemetryMonitor", "StopMonitor:: Telemetry Monitor is not running to be closed.");
        }
    }
    //endregion
}