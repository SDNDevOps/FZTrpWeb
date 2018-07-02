/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZAlgoContext;
import com.fz.tms.service.algo.FZDeliveryAgent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class FNAlgoVars {
    FZAlgoContext cx;
    //List<FNTripDist> tripDists = new ArrayList<FNTripDist>();
    List<FZDeliveryAgent> sortedAgents = new ArrayList<FZDeliveryAgent>();
}
