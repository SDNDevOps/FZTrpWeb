/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZCustDelivery;
import com.fz.tms.service.algo.FZDeliveryAgent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class FNRunInitializer {

    static FNSolution initializeRun(FNAlgoVars vr) throws Exception {
        
        FNSolution sol = new FNSolution();
        sol.vr = vr;

        initJobList(vr, sol);
        initJobTripDistList(vr, sol);
        initSortedJobList(vr, sol);
        initSortedAgentList(vr);
        
        return sol;
    }

    private static void initJobList(FNAlgoVars vr, FNSolution sol) {

        // create job list
        for (FZCustDelivery cd : vr.cx.custDeliveries){
            FNJob j = new FNJob();
            j.custDlv = cd;
            j.route = null;
            sol.unAsgJobs.add(j);
        }
        
    }

    private static void initSortedJobList(FNAlgoVars vr, FNSolution sol) throws Exception {
    
        // pick seed, highest priority job nearest from depo
            FNJob seedJ = sol.unAsgJobs.get(0);
            double minDurFromDepo = 999999;
            
            // for all unAsg job
            for (FNJob j : sol.unAsgJobs){
                
                // if priority better than seed
                if (j.custDlv.priority < seedJ.custDlv.priority){
                    seedJ = j;
                }
                // if equal priority
                else if (j.custDlv.priority == seedJ.custDlv.priority){
                    
                    // for all before trips
                    for (FNTripDist td : j.beforeTripDists){
                        // if from depo
                        if (td.fromSiteID.startsWith("DEPO")){
                            // if dur better than current best dur
                            if (td.durMin < minDurFromDepo){
                                // assign as seed
                                seedJ = j;
                                minDurFromDepo = td.durMin;
                            }
                            break;
                        }
                    }
                }
            }
            
        // sort, by priority, then areaID, then durMin
            int totalJobs = sol.unAsgJobs.size();
            int curPriority = 1;

            // move seedJ to sorted
            List<FNJob> sortedJobs = new ArrayList<FNJob>();
            sortedJobs.add(seedJ);
            sol.unAsgJobs.remove(seedJ);
            FNJob curJ = seedJ;

            // while there are unsorted cust 
            while (sortedJobs.size() < totalJobs){

                // pick next same priority, nearest to cur cust
                    FNJob neighborJ = null;
                    double minDurMin = 99999;

                    // for all after trip dist
                    for (FNTripDist td : curJ.afterTripDists){
                        
                        if (td.toSiteID.startsWith("DEPO")){
                            continue;
                        }
                        
                        FNJob nextJ = FNUtil.findJobInUnAsgList(td.toSiteID
                                , sol);

                        if (nextJ == null){
                            continue;
                        }
                        
                        // if to priority <> cur job priority, skip
                        if (curPriority != nextJ.custDlv.priority){ 
                            continue;
                        }
                            
                        // if areaID not same, skip
                        if (!curJ.custDlv.areaID.equals(nextJ.custDlv.areaID)){
                            continue;
                        }

                        // if dur is minimum
                        if (td.durMin < minDurMin){

                            // set as neighbor so far
                            minDurMin = td.durMin;
                            neighborJ = nextJ;
                        }
                    }
                    
                // if neighbor not found
                if (neighborJ == null){

                    // priority++
                    curPriority++;

                    // if max priority reached, throw error
                    if (curPriority >= 11){
                        throw new Exception("Priority must be 1 - 10");
                        //break;
                    }
                    // else, continue loop with new priority
                }
                // else, found
                else {
                    // move neighbor to sorted list
                    sortedJobs.add(neighborJ);
                    sol.unAsgJobs.remove(neighborJ);
                    curJ = neighborJ;
                }
            }
        
        // replace unAsgJob list with the sorted one
            sol.unAsgJobs = sortedJobs;
    }
    
//    private static void initJobList_ORIG(FNAlgoVars vr, FNSolution sol) {
//        
//        // create job list
//        for (FZCustDelivery cd : vr.cx.custDeliveries){
//            FNJob j = new FNJob();
//            j.custDlv = cd;
//            j.route = null;
//            sol.unAsgJobs.add(j);
//        }
//        
//        // sort cust deliv list
//        Collections.sort(sol.unAsgJobs, new Comparator() {
//
//            public int compare(Object o1, Object o2) {
//
//                // sort by priority 
//                Integer x1 = ((FNJob) o1).custDlv.priority;
//                Integer x2 = ((FNJob) o2).custDlv.priority;
//                
//                // sort low to high?
//                int sComp = x1.compareTo(x2);
//                //if (sComp != 0){
//                    return sComp;
//                //}
//                
////                // .. then by amount
////                Double y1 = ((FNJob) o1).custDlv.totalAmount;
////                Double y2 = ((FNJob) o2).custDlv.totalAmount;
////                
////                // sort high to low
////                int sComp2 = y2.compareTo(y1);
////                return sComp2;
//        }});
//        
//    }

    private static void initSortedAgentList(FNAlgoVars vr) {
        
        // copy agent list
        vr.sortedAgents.addAll(vr.cx.deliveryAgents);
        for (FZDeliveryAgent a : vr.sortedAgents){
            a.costForPrioritySorting = 
                    (a.fixedCost + (a.costPerDist * 20000)) / a.maxKg;
        }
        
        // sort cust deliv list
        Collections.sort(vr.sortedAgents, new Comparator() {

            public int compare(Object o1, Object o2) {

                FZDeliveryAgent a1 = ((FZDeliveryAgent) o1);
                FZDeliveryAgent a2 = ((FZDeliveryAgent) o2);
                
                // sort by priority 
                Integer x1 = a1.agentPriority;
                Integer x2 = a2.agentPriority;
                
                // sort low to high?
                int sComp = x1.compareTo(x2);
                if (sComp != 0){
                    return sComp;
                }
                
                // then by cost per kg
                // sort low to high?
                Double y1 = a1.costForPrioritySorting;
                Double y2 = a2.costForPrioritySorting;
                
                int sComp2 = y1.compareTo(y2);
                return sComp2;
                
        }});
        
    }

    private static void initJobTripDistList(FNAlgoVars vr, FNSolution sol) 
            throws Exception {
        
        // for each cost dist
        for (JSONObject o : vr.cx.costDists){
            
            // create the associated tripDist
            FNTripDist td = new FNTripDist();
            
            td.fromSiteID = FNUtil.toSiteID(o.getString("from"));
            td.toSiteID = FNUtil.toSiteID(o.getString("to"));
            
            // include original tripDists params
            td.lon1 = o.getString("lon1");
            td.lat1 = o.getString("lat1");
            td.lon2 = o.getString("lon2");
            td.lat2 = o.getString("lat2");
            td.distMtr = o.getDouble("dist");
            td.durMin = o.getDouble("dur");
            
            // assign this trip dist to job's priorTripDist
            // find the job
            for (FNJob j : sol.unAsgJobs){
                
                // if this job is destination of tripDist
                if (j.custDlv.custID.equals(td.toSiteID)){

                    j.beforeTripDists.add(td);
                    
                }
                // if this is origin
                else if (j.custDlv.custID.equals(td.fromSiteID)){
                    
                    j.afterTripDists.add(td);
                }
            }
        }
        
        // sort tripDists
        for (FNJob j : sol.unAsgJobs){
            
            // sort before trip Dist
            Collections.sort(j.beforeTripDists, new Comparator() {

                public int compare(Object o1, Object o2) {

                    FNTripDist a1 = ((FNTripDist) o1);
                    FNTripDist a2 = ((FNTripDist) o2);

                    // sort by dur 
                    Double x1 = a1.durMin;
                    Double x2 = a2.durMin;

                    // sort low to high?
                    int sComp = x1.compareTo(x2);
                    return sComp;
            }});

            
            // sort after trip Dist
            Collections.sort(j.afterTripDists, new Comparator() {

                public int compare(Object o1, Object o2) {

                    FNTripDist a1 = ((FNTripDist) o1);
                    FNTripDist a2 = ((FNTripDist) o2);

                    // sort by dur 
                    Double x1 = a1.durMin;
                    Double x2 = a2.durMin;

                    // sort low to high?
                    int sComp = x1.compareTo(x2);
                    return sComp;
            }});

        }
    }

}
