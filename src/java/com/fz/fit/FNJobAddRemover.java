/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZDeliveryAgent;

/**
 *
 */
public class FNJobAddRemover {

    static FNSolution insertToExistingRoute(
            FNJob origJob
            , int jobIndex
            , FNRoute origRoute) throws Exception {
        
        return insertJob(origJob, jobIndex, origRoute, null, origRoute.sol);
    }
    
    static FNSolution insertToNewRoute(
            FNJob origJob
            , FZDeliveryAgent a
            , FNSolution origSol) throws Exception {
        
        return insertJob(origJob, 0, null, a, origSol);
    }
    
    // insert job to route, returns new solution
    // to start new route, set r = null
    private static FNSolution insertJob(
            FNJob origJob
            , int jobIndex
            , FNRoute origRoute
            , FZDeliveryAgent agent
            , FNSolution origSol) throws Exception {
        
        // clone solution
        FNSolution clonedSol = FNCloner.cloneSolution(origSol);
        
        // if new route
        FNRoute routeInClonedSol = null;
        if (origRoute == null){
            
            // create new route in cloned sol
            routeInClonedSol = new FNRoute();
            routeInClonedSol.sol = clonedSol;
            routeInClonedSol.agent = agent;
            clonedSol.routes.add(routeInClonedSol);
        }
        else {
            // get clonedRoute in clonedSol
            routeInClonedSol = FNUtil.findRoute(origRoute.agent.agentID
                    , clonedSol);
        }

        // get cloned job in cloned sol
        FNJob clonedJob = FNUtil.findJobInUnAsgList(
                origJob.custDlv.custID, clonedSol);
        
        // get route in cloned solution
        clonedJob.route = routeInClonedSol;
        
        // insert newJob to route
        clonedJob.route.jobs.add(jobIndex, clonedJob);
        
        // remove from unasg
        clonedSol.unAsgJobs.remove(clonedJob);
        //clonedSol.asgJobs.add(clonedJob);
        
//        // invalidate tripDist of previous job if any, 
//        // so it will be recalc to point to clonedJob
//        int clonedJobIndexInRoute = clonedJob.route.jobs.indexOf(clonedJob);
//        if (clonedJobIndexInRoute < routeInClonedSol.jobs.size() - 1) {
//            clonedJob.route.jobs.get(clonedJobIndexInRoute + 1).tripDist = null;
//        }
        
        // calc clonedSolution
        FNSolutionCalc.calcSol(clonedSol);

        if (clonedSol.isFeasible) return clonedSol;
        return null;
        
    }
    
}
