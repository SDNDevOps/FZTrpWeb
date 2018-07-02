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
public class FNTwistRndmSwpInRte extends FNTwister {

    @Override
    public FNSolution twistIt(FNSolution sol) throws Exception {
        
        // find 2 random job, in routes, to swap
        int totalRoutedJobs = sol.vr.cx.custDeliveries.size() - sol.unAsgJobs.size();
        int totalId1 = FZUtil.randBetween(0, totalRoutedJobs-1);
        int totalId2 = FZUtil.randBetween(0, totalRoutedJobs-1);
        if (totalId1 == totalId2) return sol;
        FNJob j1 = findJobByTotalIndex(sol, totalId1);
        FNJob j2 = findJobByTotalIndex(sol, totalId2);
        System.out.println("FNTwistRndmSwpInRte " 
                + j1.custDlv.custID 
                + " with " 
                + j2.custDlv.custID);
        
        // swap position
        FNRoute r1 = j1.route;
        FNRoute r2 = j2.route;
        int id1 = r1.jobs.indexOf(j1);
        int id2 = r2.jobs.indexOf(j2);
        
        r1.jobs.remove(j1);
        r2.jobs.remove(j2);
        
        r1.jobs.add(id1, j2);
        r2.jobs.add(id2, j1);
        
        j1.route = r2;
        j2.route = r1;
        
        return sol;
    }

    private FNJob findJobByTotalIndex(FNSolution sol, int id1) throws Exception {
        int cid = -1;
        for (FNRoute r : sol.routes){
            for (FNJob j : r.jobs){
                cid++;
                if (cid == id1){
                    return j;
                }
            }
        }
        throw new Exception("Unable to find job with total index " + id1);
    }

}
