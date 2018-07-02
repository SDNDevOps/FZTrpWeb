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
public class FNSimSteel {
    
    public FNSolution go(FNSolution origSol) throws Exception {
        
        double t = 100;
        FNSolution s = origSol;
        FNSolution best = s;
        
        for (int iter = 1; iter < origSol.vr.cx.maxIteration; iter++){
            
            FNSolution r = twist(copyIt(s));
            if ( 
                    ( quality(r) > quality(s) ) 
                    || 
                    ( isCooling(r, s, t) )
                    ){
                s = r;
            }
            t = t * 0.9;
            if (quality(s) > quality(best)){
                best = s;
                System.out.println("Iter " + iter + ", NEW, " + best.totalCost);
            }
            else {
                System.out.println("Iter " + iter + ", NO CHANGE, " + best.totalCost);
            }
            
        }
        return best;
    }

    private double quality(FNSolution s) {
        return s.totalCost;
    }

    private FNSolution twist(FNSolution s) throws Exception {
        int twistType = FZUtil.randBetween(0, 0);
        FNTwister tw = null;
        if (twistType == 0){
            tw = new FNTwistRndmSwpInRte();
        }
        else return s;
        FNSolution twistSol = copyIt(s); 
        return tw.twistIt(twistSol);
    }

    private FNSolution copyIt(FNSolution s) {
        return FNCloner.cloneSolution(s);
    }

    private boolean isCooling(FNSolution r, FNSolution s, double t) {
        double delta = quality(r) - quality(s);
        double randNbr = FZUtil.randBetween(0, 1);
        double expNbr = Math.exp(-delta / t);
        boolean b = randNbr < expNbr;
        return b;
    }
}
