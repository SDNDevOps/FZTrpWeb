/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.fit;

import com.fz.tms.service.algo.FZCustDelivery;
import com.fz.tms.service.algo.FZDeliveryAgent;

/**
 *
 */
class FNAgentUtil {

    static FZDeliveryAgent findPriorityUnAsgAgent(FNSolution sol
            , FNJob j) 
            throws Exception {
        
        FZDeliveryAgent fa = null;
                
        // for all sorted agent
        for (FZDeliveryAgent a : sol.vr.sortedAgents){
                
            // if not asg
            if (!isAssigned(a, sol)){
                
                // if access type ok
                if (accessTypeMatch(j, a)) {
                    
                    //return
                    fa = a;
                    break;
                }
            }
        }
        return fa;
    }

    static boolean isAssigned(FZDeliveryAgent a, FNSolution sol) {
        boolean found = false;
        for (FNRoute r : sol.routes){
            if (r.agent.agentID.equals(a.agentID)){
                found = true;
                break;
            }
        }
        return found;
    }

    static boolean accessTypeMatch(FNJob j, FZDeliveryAgent a) {
        for (String t : j.custDlv.agentTypes){
            if (t.equals(a.agentType)){
                return true;
            }
        }
        return false;
    }

}
