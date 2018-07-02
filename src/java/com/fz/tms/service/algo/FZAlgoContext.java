/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.tms.service.algo;

import com.fz.util.FZUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 */
public class FZAlgoContext {

        public String runID = "";
        public List<FZCustDelivery> custDeliveries;
        public List<FZDeliveryAgent> deliveryAgents;
        public int maxIteration = 50;
        public String timeDistSource = "";
        public String prevCostDistRunID = "";
        public String workFolder = "";
        public int saveToDbFreq = 0;
        public FZAlgoEventHandler algoEventHandler = null;
        public String branchCode = "";
        public String shift = "";
        public boolean mustFinish = false;
        public int dbAccessTimes = 0;
        public List<FZRouteJob> routeJobs = new ArrayList<FZRouteJob>();
        public List<FZRouteJob> unAsgJobs = new ArrayList<FZRouteJob>();
        public FZParams params = new FZParams();
        public HttpServletResponse response = null;
        public Object problem = null; // cast to VehicleRoutingProblem
        public JSONObject otherParams = null;
        public String folderForLog = "c:\\fza\\log\\";
        public String algoPreference = "speed";
        
        // costDists item = 
        // lon1, lat1, lon2, lat2
        // , dist, dur, from (CustID), to (CustID)
        public List<JSONObject> costDists = null;
        
        
    // smack refit    
    public int curIter;
    
    public double firstPriorityUnassignedPenalty = 9123456;
    public double secondPriorityUnassignedPenalty = 1824691;  // 20% 1st priority
    public double infeasibleRoutePenalty = 91234567;
    public double eachPriorityUnassignedPenalty = 100;

        public void log(String msg) throws Exception {
            FZUtil.logSlowly(this.folderForLog, "tms_" + runID, msg);
        }

}
