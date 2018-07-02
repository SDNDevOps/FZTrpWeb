/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZCustDelivery;
import com.fz.tms.service.algo.FZDeliveryAgent;
import com.fz.util.FZUtil;
import java.util.List;

/**
 *
 */
class FNUtil {

//    static FNTripDist findTripDist(String custID, String custID2
//            , FNAlgoVars vr) throws Exception {
//
//        String custSiteID = toSiteID(custID);
//        String custSiteID2 = toSiteID(custID2);
//
//        // if same cust, create leg
//        if (custSiteID.equals(custSiteID2)){
//            FNTripDist td = new FNTripDist();
//            td.distMtr = 0;
//            td.durMin = 0;
//            td.fromSiteID = custID;
//            td.toSiteID = custID2;
//            
//            // find the cust to get lon lat
//            for (FZCustDelivery cd : vr.cx.custDeliveries){
//                if (cd.custID.equals(custSiteID)){
//                    td.lon1 = cd.lonStr;
//                    td.lat1 = cd.latStr;
//                    td.lon2 = cd.lonStr;
//                    td.lat2 = cd.latStr;
//                    return td;
//                }
//            }
//            
//            // reach here, no cust
//            throw new Exception("Cannot find " 
//                    + custSiteID 
//                    + " in cust deliv list");
//        }
//        
//        // reach here, cust not same
//        // look for the tripDist
//        for (FNTripDist td : vr.tripDists){
//            
//            if (matchTripDist(td.fromSiteID, custSiteID, td)
//                && matchTripDist(td.toSiteID, custSiteID2, td)
//                ){
//                
//                return td;
//            }
//        }
//        return null;
//    }
//    
    static FNRoute findRoute(String agentID, FNSolution sol) {
        for (FNRoute r : sol.routes){
            if (r.agent.agentID.equals(agentID)){
                return r;
            }
        }
        return null;
    }

    static FNJob findJobInRoute(String custID, FNSolution sol) {
        
        FNJob job = null;
        for (FNRoute r : sol.routes){
            for (FNJob j : r.jobs){
                if (j.custDlv.custID.equals(custID)){
                    job = j;
                    break;
                }
            }
        }
        return job;
    }

    static FNJob findJobInUnAsgList(String custID, FNSolution sol) {
        
        FNJob job = null;
        for (FNJob j : sol.unAsgJobs){
            if (j.custDlv.custID.equals(custID)){
                job = j;
                break;
            }
        }
        return job;
    }

    public static String toSiteID(String id){
        // get before "_" or "-" if any
        
        String id2 = id;
        id2 = id2.replaceAll("-", "_");
        
        if (!id2.contains("_")) return id2;
        
        String[] a = id2.split("_");
        
        return a[0];
        
    }
    
    public static boolean matchTripDist(String tripDistID, String custID
            , FNTripDist td){
        
        return toSiteID(tripDistID)
                .equals(toSiteID(custID));
    }

    static void logSol(FNSolution sol) {
        
        String m = "\n\n==================================";
        m += "\nSol Total Cost = " + sol.totalCost;
        m += "\nSol Transport Cost = " + sol.transportCost;
        m += "\nSol Activity Cost = " + sol.activityCost;
        m += "\nSol Total Km = " + sol.totalKm;
        m += "\nSol Total Kg = " + sol.totalKg;
        m += "\nSol Total Vol = " + sol.totalVol;
        m += "\nSol Total Route = " + sol.routes.size();
        m += "\nSol Assigned = " + sol.asgJobs.size();
        m += "\nSol Unassigned = " + sol.unAsgJobs.size();
        
        for (FNRoute r : sol.routes){
        
            m += "\n\nRoute agent = " + r.agent.agentID;
            m += "\nPriority = " + r.agent.agentPriority;
            m += " | FixedCost = " + r.agent.fixedCost;
            m += " | MaxKg = " + r.agent.maxKg;
            m += " | MaxVol = " + r.agent.maxVolume;
            m += " | Type = " + r.agent.agentType;
            m += " | Start = " + r.agent.earliestDepartTime;
            m += " | LatestEnd = " + r.agent.latestArrivalTime;
            m += " | PlanEnd = " + FZUtil.toClock(r.endTime);
            
            for (FNJob j : r.jobs){
                
            }
        }
    }

    static FNTripDist findAfterTripDist(FNJob j, String siteID) {
        FNTripDist foundTD = null;
        for (FNTripDist td : j.afterTripDists){
            if (td.toSiteID.equals(siteID)){
                foundTD = td;
                break;
            }
        }
        return foundTD;
    }

    static FNTripDist findBeforeTripDist(FNJob j, String siteID) {
        FNTripDist foundTD = null;
        for (FNTripDist td : j.beforeTripDists){
            if (td.fromSiteID.equals(siteID)){
                foundTD = td;
                break;
            }
        }
        return foundTD;
    }

}
