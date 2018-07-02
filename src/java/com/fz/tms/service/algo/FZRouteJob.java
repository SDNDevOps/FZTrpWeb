/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.tms.service.algo;

/**
 *
 */
public class FZRouteJob {
//    site_id  varchar(50)
//    ,customer_id  varchar(50)
//    , do_number varchar(50)
//    , vehicle_code varchar(50)
//    , activity varchar(50)
//    , routeNm int
//    , jobNm int
//    , arrive varchar(5)
//    , depart varchar(5)
//    , runID varchar(50)
//    , create_dtm datetime
//    , branch varchar(50)
//    , shift varchar(50)
//    , lon varchar(50)
//    , lat varchar(50)
    
    public String siteID = "";
    public String custID = "";
    public String DONum = "";
    public String vehicleCode = "";
    public String activity = "";
    public int routeNb = 0;
    public int jobNb = 0;
    public String arrive = "";
    public String depart = "";
    public String runID = "";
    public String branch = "";
    public String shift = "";
    public String lon = "";
    public String lat = "";
    public String weight = "";
    public String volume = "";
    public double transportCost = 0;
    public double activityCost = 0;
    public double dist;
    
}
