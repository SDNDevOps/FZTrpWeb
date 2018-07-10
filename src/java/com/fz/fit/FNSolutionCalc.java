/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.util.FZUtil;

/**
 *
 */
class FNSolutionCalc {

    static void calcSol(FNSolution sol) throws Exception {
        
        initSolCalc(sol);
            
        // for all route
        for (FNRoute r : sol.routes){
            
            initRouteCalc(r);
            
            FNJob prevJob = null;
            
            // for all job
            for (FNJob j : r.jobs){
                
                // add to asgList
                sol.asgJobs.add(j);
                
                // calcJob
                calcJobImpact(j, prevJob);
                if (!r.sol.isFeasible) return;
                prevJob = j;
            }
        }
        applyPenalties(sol);
            
    }

    private static void initRouteCalc(FNRoute r) {
        r.transportCost = r.agent.fixedCost;
        r.activityCost = 0;
        r.totalKg = 0;
        r.totalVol = 0;
        r.totalKm = 0;
        r.endTime = FZUtil.clockToMin(r.agent.earliestDepartTime);
    }

    private static void initSolCalc(FNSolution sol) {
        sol.transportCost  = 0;
        sol.activityCost  = 0;
        sol.totalCost  = 0;
        sol.totalKg  = 0;
        sol.totalVol  = 0;
        sol.totalKm  = 0;
    }

    static boolean calcJobImpact(FNJob curJob
            , FNJob prevJob) 
        throws Exception {

            // init calc
            FNRoute r = curJob.route;

            // calc time, costs, capacity etc
            calcTripDist(curJob, prevJob);
            calcArriveTime(curJob, prevJob, r);
            calcJobCost(curJob, r);
            calcRunningTotals(curJob, r);
            
            // calc constraints
            boolean custTimeWindowOK = checkCustTimeWindow(curJob);
            boolean tripDistOK = checkTripDist(curJob, r); 
            boolean agentEndTimeOK = calcAgentEndTrip(curJob, r);
            boolean kgOK = r.totalKg < r.agent.maxKg;
            boolean volOK = r.totalVol < r.agent.maxVolume;
            boolean numOfCustOK = r.jobs.size() < r.agent.maxCust;
            
            // is feasible?
            r.sol.isFeasible = 
                    custTimeWindowOK
                    && tripDistOK
                    && agentEndTimeOK
                    && kgOK
                    && volOK
                    && numOfCustOK
                    ;
    
            FNUtil.logSol(r.sol);
            
            return r.sol.isFeasible;
    }

    private static boolean checkCustTimeWindow(FNJob curJob) {
        boolean b = (FZUtil.clockToMin(curJob.custDlv.timeWindowStart)
                <= curJob.arriveTime)
                && 
                (curJob.arriveTime 
                <= FZUtil.clockToMin(curJob.custDlv.timeWindowEnd))
                ;
        return b;
    }

    private static boolean checkTripDist(FNJob curJob, FNRoute r) throws Exception {
        
        boolean b = true;
        
        // if not first trip, check max distance
        if (r.jobs.indexOf(curJob) == 0){
            if(r.sol.vr.cx.params.getInt("DefaultDistance") == 0)
                //if Unconstrain
                b = true;
            else if (r.jobs.indexOf(curJob) > 0)
                b = curJob.beforeTripDist.distMtr <=
                        r.sol.vr.cx.params.getInt("DefaultDistance") 
                        * 1000;
            System.out.println("DefaultDistance()"+r.sol.vr.cx.params.getInt("DefaultDistance"));
        }
        return b;
    }

    private static boolean calcAgentEndTrip(FNJob curJob, FNRoute r) throws Exception {
        
        boolean b = true;
        
        // if last job only
        if (r.jobs.indexOf(curJob) == r.jobs.size() - 1){
            
            // get last tripDist
            r.lastTripDist = FNUtil.findAfterTripDist(
                    curJob, "DEPO");
            
            // calc end time
            r.endTime = 
                    (int) (curJob.arriveTime 
                    + curJob.custDlv.serviceTime 
                    + r.lastTripDist.durMin);
            
            // calc end trip cost
            r.endTripTransportCost  =
                    r.lastTripDist.distMtr 
                    * r.agent.costPerDist
                    +
                    r.lastTripDist.durMin 
                    * r.agent.costPerTravelTime
                    ;

            // add route cost
            r.transportCost += r.endTripTransportCost;

            // add sol totals
            r.sol.transportCost += r.endTripTransportCost;
            r.sol.totalCost += r.endTripTransportCost;
            r.sol.totalKm += r.lastTripDist.distMtr / 1000;
            
            // check if ok
            b = r.endTime < FZUtil.clockToMin(r.agent.latestArrivalTime);
        }
        return b;
    }

    private static void calcJobCost(FNJob curJob, FNRoute r) {
        
        curJob.transportCost  =
                curJob.beforeTripDist.distMtr 
                * r.agent.costPerDist
                +
                curJob.beforeTripDist.durMin 
                * r.agent.costPerTravelTime
                ;
        
        curJob.activityCost =
                curJob.custDlv.serviceTime
                * r.agent.costPerServiceTime
                ;
    }

    private static void calcArriveTime(FNJob curJob, FNJob prevJob, FNRoute r) {
        int startTime = 0;

        // if first job, start from depo
        if (prevJob == null){
            startTime = FZUtil.clockToMin(
                    r.agent.earliestDepartTime);
        }
        else { // subsequent unAsgJobs, start from prev Job
            startTime = prevJob.arriveTime 
                    + prevJob.custDlv.serviceTime;

        }

        // cacl arrive
        curJob.arriveTime = (int) (startTime + curJob.beforeTripDist.durMin);
    }

    private static void calcRunningTotals(FNJob curJob, FNRoute r) {
        
        r.transportCost += curJob.transportCost;
        r.activityCost += curJob.activityCost;
        r.totalKg += Double.parseDouble(curJob.custDlv.totalKg);
        r.totalVol += Double.parseDouble(curJob.custDlv.totalVolume);
        r.totalKm += curJob.beforeTripDist.distMtr / 1000;
        
        r.sol.transportCost += curJob.transportCost;
        r.sol.activityCost += curJob.activityCost;
        r.sol.totalCost += curJob.transportCost + curJob.activityCost;
        r.sol.totalKg += Double.parseDouble(curJob.custDlv.totalKg);
        r.sol.totalVol += Double.parseDouble(curJob.custDlv.totalVolume);
        r.sol.totalKm += curJob.beforeTripDist.distMtr / 1000;
        
    }

    private static void applyPenalties(FNSolution sol) throws Exception {
        
        // for all job
        for (FNJob j : sol.unAsgJobs){
            
            // if not asg
            if (j.route == null){
                
                // if priority 1
                if (j.custDlv.priority == 1){
                    sol.totalCost += sol.vr.cx.params
                            .getDouble("firstPriorityUnassignedPenalty");
                }
                // else if priority 2
                else if (j.custDlv.priority == 2){
                    sol.totalCost += sol.vr.cx.params
                            .getDouble("secondPriorityUnassignedPenalty");
                }
                else {
                    // 3rd onward
                    sol.totalCost += sol.vr.cx.params
                            .getDouble("thirdOnwardPriorityUnassignedPenalty");
                }
            }
        }
    }

    private static void calcTripDist(FNJob curJob, FNJob prevJob) throws Exception {
        
//        // if tripDist not created
//        if (curJob.tripDist == null){
            
            // if 1st job
            String siteIDBefore = "";
            if (prevJob == null){
                siteIDBefore = "DEPO";
            }
            else { // not first
                siteIDBefore = prevJob.custDlv.custID;
            }
            
            // assign
            curJob.beforeTripDist = FNUtil.findBeforeTripDist(
                    curJob, siteIDBefore);
            
            if (curJob.beforeTripDist == null)
                throw new Exception("Invalid Trip Distance, from " 
                        + siteIDBefore + " to " + curJob.custDlv.custID);
    }
}
