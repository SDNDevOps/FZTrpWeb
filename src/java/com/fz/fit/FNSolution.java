/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FNSolution {

    public FNAlgoVars vr = null;
    public List<FNRoute> routes = new ArrayList<FNRoute>();
    public List<FNJob> unAsgJobs = new ArrayList<FNJob>();
    public List<FNJob> asgJobs = new ArrayList<FNJob>();
    boolean isFeasible;
    double transportCost = 0;
    double activityCost = 0;
    double totalCost = 0;
    double totalKg = 0;
    double totalVol = 0;
    double totalKm = 0;

}
