/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZCustDelivery;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class FNInitialSolutionMaker {

    List<FNTripDist> sortedCostDist = new ArrayList<FNTripDist>();
    List<FZCustDelivery> sortedCustDelivs = new ArrayList<FZCustDelivery>();
    
    FNSolution makeInitialSolution(FNSolution origSol) throws Exception {
    
        // copy orig sol for iteration
        FNSolution iterSol = FNCloner.cloneSolution(origSol);
        FNSolution bestSol = iterSol;

        // for all sorted cust in origSol
        for (FNJob j : origSol.unAsgJobs){

//            if (j.custDlv.custID.equals("5820001082")){
//                System.out.print("");
//            }
            
            // find best route
            iterSol = FNNearestOrNewRouteFinder
                    .assignToNearestOrNewRoute(j, bestSol);
            
            // if iterSol null (assigned), update best 
            if (iterSol != null){
                
                bestSol = iterSol;
            }
        }
        
        return bestSol;
    }

}
