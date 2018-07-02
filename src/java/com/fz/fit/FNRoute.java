/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZDeliveryAgent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FNRoute {

    FNSolution sol = null;
    FZDeliveryAgent agent = null;
    List<FNJob> jobs = new ArrayList<FNJob>();
    FNTripDist lastTripDist = null;
    
    double transportCost = 0;
    double activityCost = 0;
    double totalKg = 0;
    double totalVol = 0;
    double totalKm = 0;
    double endTripTransportCost = 0;
    int asgJobCount = 0;
    int endTime = 0;
}
