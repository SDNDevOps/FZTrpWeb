/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZRouteJob;
import com.fz.util.FZVrpUtil;

/**
 *
 */
public class FNAlgoOutputer {
    
    String prevSiteID = "";
    int routeNb = 0;
    
    // write to sol.vr.cx.routeJobs
    public void writeIt(FNSolution sol) throws Exception{
        
        for (FNRoute r : sol.routes){

            routeNb++;
            int jobNb = 1;
            
            printFirstRow(r);
            
            for (FNJob algoJob : r.jobs){
                printBody(algoJob, ++jobNb);
            }
            
            printLastRow(r, ++jobNb);
        }
        printUnAsg(sol);
    }

    private void printFirstRow(FNRoute r) {
        
        FZRouteJob j = new FZRouteJob();
        
        j.siteID = "DEPO";
        prevSiteID = j.siteID;
        
        j.custID = "";
        j.DONum = "";
        j.activity = "DEPO";
        j.vehicleCode = r.agent.agentID;
        j.arrive = "";
        j.depart = r.agent.earliestDepartTime;
        j.branch = r.sol.vr.cx.branchCode;
        j.shift = r.sol.vr.cx.shift;
        j.jobNb = 1;
        j.routeNb = routeNb;
        j.runID = r.sol.vr.cx.runID;
        
        j.lon = String.valueOf(r.agent.startLon);
        j.lat = String.valueOf(r.agent.startLat);
        
        r.sol.vr.cx.routeJobs.add(j);
    }

    private void printBody(FNJob algoJob, int jobNb) throws Exception {
        String jobId = algoJob.custDlv.custID;
        
        FZRouteJob j = new FZRouteJob();
        
        j.siteID = jobId;
        
        j.custID = algoJob.custDlv.custID;
        j.DONum = algoJob.custDlv.DONum;
        j.activity = j.custID;
        j.vehicleCode = algoJob.route.agent.agentID;

        // calc arr & depart due to break
        double arrTime = algoJob.arriveTime;
        double departTime = algoJob.arriveTime + algoJob.custDlv.serviceTime;
//        if (arrTime > 720){ // 12 o'clock * 60 min, due to break
//            arrTime += 60;
//            departTime += 60;
//        }
        j.arrive = FZVrpUtil.toClock(
                (int) Math.round(arrTime)
        );
        j.depart = FZVrpUtil.toClock(
                (int) Math.round(departTime)
        );
        
        j.branch = algoJob.route.sol.vr.cx.branchCode;
        j.shift = algoJob.route.sol.vr.cx.shift;
        j.jobNb = jobNb;
        j.routeNb = routeNb;
        j.runID = algoJob.route.sol.vr.cx.runID;
        
        j.lon = String.valueOf(algoJob.custDlv.lon);
        j.lat = String.valueOf(algoJob.custDlv.lat);
//        prevLon = Double.parseDouble(j.lon);
//        prevLat = Double.parseDouble(j.lat);
        
        j.weight = algoJob.custDlv.totalKg;
        j.volume = algoJob.custDlv.totalVolume;

        // get tour cost
        // output km
        j.transportCost = algoJob.transportCost;
        j.activityCost = algoJob.activityCost;
        
        algoJob.route.sol.vr.cx.routeJobs.add(j);
        prevSiteID = j.siteID;
    }

    private void printLastRow(FNRoute r, int jobNb) {
        FZRouteJob j = new FZRouteJob();
        
        j.siteID = "DEPO";
        j.custID = "";
        j.DONum = "";
        j.activity = "DEPO";
        j.vehicleCode = r.agent.agentID;
        
        // calc arr due to break
        double arrTime = r.endTime;
//        if (arrTime > 720){ // 12 o'clock * 60 min, due to break
//            arrTime += 60;
//        }
        j.arrive = FZVrpUtil.toClock(
                (int) Math.round(arrTime)
        );
        
        j.depart = "";
        j.branch = r.sol.vr.cx.branchCode;
        j.shift = r.sol.vr.cx.shift;
        j.jobNb = jobNb;
        j.routeNb = routeNb;
        j.runID = r.sol.vr.cx.runID;
        j.lon = String.valueOf(r.agent.endLon);
        j.lat = String.valueOf(r.agent.endLat);

        j.transportCost = r.endTripTransportCost;
        j.activityCost = 0;
        
        r.sol.vr.cx.routeJobs.add(j);
    }
    
    private void printUnAsg(FNSolution sol) throws Exception {
        
        for (FNJob algoJob : sol.unAsgJobs){
            FNJob asgJob = FNUtil.findJobInRoute(algoJob.custDlv.custID, sol);
            if (asgJob == null){
                
                String siteID = algoJob.custDlv.custID;
                FZRouteJob j = new FZRouteJob();
                j.siteID = algoJob.custDlv.custID;
                j.custID = algoJob.custDlv.custID;
                j.DONum = algoJob.custDlv.DONum;
                j.vehicleCode = "NA";
                j.runID = sol.vr.cx.runID;
                j.branch = sol.vr.cx.branchCode;
                j.shift = sol.vr.cx.shift;
                j.lon = algoJob.custDlv.lonStr;
                j.lat = algoJob.custDlv.latStr;
                j.weight = algoJob.custDlv.totalKg;
                j.volume = algoJob.custDlv.totalVolume;
                
                sol.vr.cx.unAsgJobs.add(j);
                
            }
        }
    }
}
