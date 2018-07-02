/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZCustDelivery;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FNJob {

    FZCustDelivery custDlv = null;
    FNRoute route = null;
    int arriveTime = 0;
    double activityCost = 0;
    double transportCost = 0;
    
    FNTripDist beforeTripDist = null;
    List<FNTripDist> beforeTripDists = new ArrayList<FNTripDist>();
    List<FNTripDist> afterTripDists = new ArrayList<FNTripDist>();
}
