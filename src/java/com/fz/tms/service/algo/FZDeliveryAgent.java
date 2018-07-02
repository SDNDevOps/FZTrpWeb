/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.tms.service.algo;

/**
 *
 */
public class FZDeliveryAgent {
    
//VehicleID	MaxKG	VehicleCateg	MinStartTime	MaxEndTime	StartLon	StartLat	EndLon	EndLat	MaxCubication
        
    public String agentID = "";
    public double maxKg = 0;
    public double maxVolume = 0;
    public double maxCapacity = 0; // either use maxVolume or maxKg
    public String earliestDepartTime = "06:00";
    public String latestArrivalTime = "17:00";
    public String agentType = "";
    public double startLon = 0;
    public double startLat = 0;
    public String branchCode = "";
    public double endLon = 0;
    public double endLat = 0;
    public String source;
    
    public double fixedCost = 0;
    public double costPerDist = 0;
    public double costPerServiceTime = 0;
    public double costPerTravelTime = 0;
    
    public int maxCust = 999999;
    public int agentPriority = 1;
    public double costForPrioritySorting = 0;
    
    public String areaID = "";
}
