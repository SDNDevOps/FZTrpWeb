/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZDeliveryAgent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class FNFittestZoneFinder {
    
//    int maxToSmack = 3;
//    int maxToRefit = 4;
//    int maxToNeighbor = 5;
//    double minDistMtr = 1000;
//        
//    public FNSolution go(FNSolution origSol) throws Exception {
//        
//        // for algo iter
//        FNSolution best = origSol;
//        for (int iter = 1; iter < origSol.vr.cx.maxIteration; iter++){
//            
//            System.out.println("Iter " + iter);
//            FNSolution smackedSol = smack(best);
//            best = refit(smackedSol, best);
//            
//        }
//        
//        return best;
//
//    }
//
//    private FNSolution smack(FNSolution best) {
//        
//        // clone sol
//        FNSolution smackedSol = FNCloner.cloneSolution(best);
//            
//        // shuffle asgList
//        Collections.shuffle(smackedSol.asgJobs);
//            
//        // remove top x, from asgList and routes
//        int smackedCount = 0;
//        for (Iterator<FNJob> it = smackedSol.asgJobs.iterator(); it.hasNext();) {
//            
//            FNJob j = it.next();
//            boolean meet = false;
//            
//            // if 1st in route, meet
//            if (j.route.jobs.indexOf(j) == 0){
//                meet = true;
//            } 
//            // if dist to reach > minDistToReach, meet
//            else if (j.tripDist.distMtr > minDistMtr) {
//                meet = true;
//            }
//            
//            // remove x from asgList to unasgList
//            if (meet) {
//                it.remove();
//                smackedSol.unAsgJobs.add(j);
//                if (++smackedCount >= maxToSmack){
//                    break;
//                }
//            }
//        }
//            
//        return smackedSol;
//    }
//
//    private FNSolution refit(FNSolution smackedSol, FNSolution origBest) throws Exception {
//        
//        List<FNJob> toRefitJobs = pickToRefitJobs(smackedSol);
//        FNSolution curBest = origBest;
//        
//        // for jobs to refit
//        FNSolution bestPosSol = null;
//        FNSolution toInsertSol = null;
//        for (FNJob j : toRefitJobs){
//            
//            // try insertion
//            bestPosSol = neighborItNCompare(j, smackedSol, toInsertSol);
//            bestPosSol = startNewRouteNCompare(j, smackedSol, toInsertSol);
//            
//        }
//        if (firstOneIsBetter(bestPosSol, curBest)){
//            curBest = bestPosSol;
//        }
//        return curBest;
//    }
//
//    private List<FNJob> pickToRefitJobs(FNSolution smackedSol) {
//        
//        // shuffle unasgList
//        Collections.shuffle(smackedSol.unAsgJobs);
//        
//        // sort unasgList by priority
//        Collections.sort(smackedSol.unAsgJobs, new Comparator() {
//
//            public int compare(Object o1, Object o2) {
//
//                // sort by priority 
//                Integer x1 = ((FNJob) o1).custDlv.priority;
//                Integer x2 = ((FNJob) o2).custDlv.priority;
//                
//                // sort low to high?
//                int sComp = x1.compareTo(x2);
//                return sComp;
//        }});
//        
//        // pick top y
//        List<FNJob> toRefitJobs = new ArrayList<FNJob>();
//        int toRefitCount = 0;
//        for (FNJob j : smackedSol.unAsgJobs){
//            toRefitJobs.add(j);
//            if (++toRefitCount >= maxToRefit){
//                break;
//            }
//        }
//        
//        return toRefitJobs;
//    }
//
//    private FNSolution neighborItNCompare(FNJob toInsertJob
//            , FNSolution smackedSol, FNSolution bestPosSol) throws Exception {
//        
//        // pick z point to neigbor in asgList
//        List<FNJob> toNeighborJobs = pickToNeighborJobs(toInsertJob, smackedSol);
//        
//        // for each z
//        for (FNJob toNeighborJob : toNeighborJobs){
//
//            // insert y after z, compare with best
//            FNSolution insertedSol = 
//                    FNJobAddRemover.insertToExistingRoute(
//                        toInsertJob
//                        , toNeighborJob.route.jobs.indexOf(toNeighborJob)+1
//                        , toNeighborJob.route);
//    
//            if (firstOneIsBetter(insertedSol, bestPosSol)){
//                bestPosSol = insertedSol;
//            }
//        }
//        return bestPosSol;
//    }
//
//    private FNSolution startNewRouteNCompare(FNJob toInsertJob
//            , FNSolution smackedSol, FNSolution bestPosSol) throws Exception {
//        
//        FNSolution insertedSol = null;
//        
//        // find un assigned cheapest agent, accessible
//        FZDeliveryAgent a = FNAgentUtil.findPriorityUnAsgAgent(smackedSol
//                , toInsertJob);
//        
//        // if any
//        if (a != null){
//            
//            // insert new job at 0
//            insertedSol = FNJobAddRemover.insertToNewRoute(toInsertJob
//                    , a, smackedSol);
//            if (firstOneIsBetter(insertedSol, bestPosSol)){
//                bestPosSol = insertedSol;
//            }
//        }
//        return bestPosSol;
//    }
//
//    private List<FNJob> pickToNeighborJobs(FNJob toInsertJob, FNSolution sol) throws Exception {
//        
//        List<FNJob> toNeighborJobs = new ArrayList<FNJob>();
//        Collections.shuffle(sol.asgJobs);
//        
//        // for all asg job
//        for (FNJob asgJob : sol.asgJobs){
//            
//            // find tripDist to unAsgJob
//            FNTripDist td = FNUtil.findTripDist(
//                    asgJob.custDlv.custID
//                    , toInsertJob.custDlv.custID
//                    , sol.vr);
//
//            // if distance constraint ok
//            if (td.distMtr / 1000 <= 
//                    sol.vr.cx.params.getDouble("DefaultDistance")){
//            
//                // pick as neighbor candidate
//                toNeighborJobs.add(asgJob);
//                
//                // if max pick, break
//                if (toNeighborJobs.size() >= maxToNeighbor) 
//                    break;
//            }
//        }
//        return toNeighborJobs;
//    }
//
//    // returns true if sol1 better than sol2
//    private boolean firstOneIsBetter(FNSolution sol1, FNSolution sol2) {
//
//        // handle null
//        if (sol1 == null) 
//            return false;
//        if (sol2 == null) 
//            return true;
//        
//        boolean b = false;
//        
//        // if both feasibility same, check cost
//        if (sol1.isFeasible == sol2.isFeasible){
//            if (sol1.totalCost < sol2.totalCost){
//                b = true;
//            }
//        }
//        // else, not same, check sol1 only
//        else if (sol1.isFeasible) {
//            b = true;
//        }
//        
//        // print
//        if (b) 
//            System.out.println("Better: " + sol1.totalCost);
//        
//        return b;
//    }
}
