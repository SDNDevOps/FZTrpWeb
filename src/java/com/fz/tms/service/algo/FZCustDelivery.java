/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.tms.service.algo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FZCustDelivery {
    
    public String custID = "";
    public double lon = 0;
    public double lat = 0;
    public String lonStr = "0";
    public String latStr = "0";
    public int serviceTime = 0;
    public String DONum = ""; 
    public String totalVolume ;
    public String totalKg ;
    public double totalCapacity = 0; // either totalKg or totalVolume
    public int priority = 0;
    public String timeWindowStart = "07:00";
    public String timeWindowEnd = "17:00";
    public List<String> agentTypes = new ArrayList<String>();
    public int adminManMinuteSaved = 0;
    public String distChannelID = "";
    public String custCategory = "";
    public String branchCode = "";
    public String subDistrict = "";
    public String district = "";
    public String zipCode = "";
    public String address = "";
    public String custName = "";
    public double totalAmount = 0;
    
    public String areaID = "";
    
}
