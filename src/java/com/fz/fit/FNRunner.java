/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZAlgoContext;

/**
 *
 */
public class FNRunner {
    
    public void run(FZAlgoContext cx) throws Exception {
        
        // create global var container
        FNAlgoVars vr = new FNAlgoVars();
        vr.cx = cx;
        
        // initialize run
        FNSolution sol = FNRunInitializer.initializeRun(vr);
        
        // make initial solution
        sol = (new FNInitialSolutionMaker()).makeInitialSolution(sol);

        // do heuristic
        sol = (new FNSimSteel()).go(sol);
        
        // output
        (new FNAlgoOutputer()).writeIt(sol);
    } 

//    private FNSolution doHeuristic(FNSolution origSol) throws Exception {
//        FNFittestZoneFinder f = new FNFittestZoneFinder();
//        return f.go(origSol);
//    }


}
