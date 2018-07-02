/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import static com.fz.fit.FNAgentUtil.isAssigned;
import com.fz.tms.service.algo.FZDeliveryAgent;

/**
 *
 */
class FNNearestOrNewRouteFinder {

    private static final int MAX_TRY_FIND_NEAREST = 20;
    
    static FNSolution assignToNearestOrNewRoute(FNJob j
            , FNSolution origSol) throws Exception {
    
        // find nearest assigned cust
        FNSolution iterSol = assignToNearestRoute(j, origSol);
        
        // if not found
        if (iterSol == null){
            
            // start new route
            iterSol = startNewRoute(j, origSol);
        }
            
        return iterSol;
    }

    static FNSolution startNewRoute(FNJob j
            , FNSolution sol) throws Exception {
        
        FNSolution resultSol = null;
        
        // for all sorted agent
        for (FZDeliveryAgent a : sol.vr.sortedAgents){
                
            // if not asg
            if (!isAssigned(a, sol)){
                
                // if access type ok
                if (FNAgentUtil.accessTypeMatch(j, a)) {
                    
                    // try insert
                    resultSol = FNJobAddRemover.insertToNewRoute(j, a, sol);
                    if (resultSol != null){
                        break;
                    }
                }
            }
        }
        return resultSol;
    }

    private static FNSolution assignToNearestRoute(FNJob j
            , FNSolution origSol) throws Exception {
        
        FNSolution resultSol = null;
        
        // for cost dist
        int foundNearestCount = 0;
        for (FNTripDist td : j.beforeTripDists){

            String nearestID = td.fromSiteID;
            
            // find route of nearestJob, if not found, continue
            FNJob nearestJob = FNUtil.findJobInRoute(nearestID, origSol);
            if (nearestJob == null) 
                continue;

            // found, increment found counter
            foundNearestCount++;
            
            // try insert after nearest job
            int nearestJobIndex = nearestJob.route.jobs.indexOf(nearestJob);
            resultSol = FNJobAddRemover.insertToExistingRoute(
                    j
                    , nearestJobIndex + 1
                    , nearestJob.route);

            // if feasbile solution exist, return the new solution
            if (resultSol != null){
                break;
            }
            
            // try max X times, if more, quit
            if (foundNearestCount == MAX_TRY_FIND_NEAREST){
                break;
            }
            
        }
        
        // reach here, none feasible, return null
        return resultSol;
    }

}
