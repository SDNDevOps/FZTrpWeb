/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

/**
 *
 */
public class FNCloner {

    public static FNRoute cloneRouteAndAddToSol(
            FNRoute origRoute
            , FNSolution clonedSol) {
        
        FNRoute clonedRoute = new FNRoute();
        clonedRoute.agent = origRoute.agent;
        clonedRoute.sol = clonedSol;
        
        for (FNJob j : origRoute.jobs){
            cloneJobAndAddToRoute(j, clonedRoute);
        }
        
        clonedSol.routes.add(clonedRoute);
        
        return clonedRoute;
    }

    public static FNJob cloneJobAndAddToRoute(FNJob j, FNRoute newRoute) {
        FNJob j2 = cloneJob(j);
        j2.route = newRoute;
        newRoute.jobs.add(j2);
        newRoute.sol.asgJobs.add(j2);
        return j2;
    }

    public static FNJob cloneJob(FNJob j) {
        FNJob j2 = new FNJob();
        j2.custDlv = j.custDlv;
        
        j2.beforeTripDist = j.beforeTripDist;
        j2.beforeTripDists = j.beforeTripDists;
        j2.afterTripDists = j.afterTripDists;
        
        j2.route = j.route;
        return j2;
    }

    static FNSolution cloneSolution(FNSolution sol) {
        
        FNSolution sol2 = new FNSolution();
        sol2.vr = sol.vr;
        
        // copy unAsg
        for (FNJob j : sol.unAsgJobs){
            FNJob j2 = FNCloner.cloneJob(j);
            sol2.unAsgJobs.add(j2);
        }
        
        // copy routes
        for (FNRoute r : sol.routes){
            FNRoute r2 = FNCloner.cloneRouteAndAddToSol(r, sol2);
        }
        
        return sol2;
    }

}
