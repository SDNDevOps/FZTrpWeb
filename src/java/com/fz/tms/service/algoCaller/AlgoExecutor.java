/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.tms.service.algoCaller;

//import com.fz.smkrft.FZAlgoContext;
import com.fz.tms.service.algo.FZCustDelivery;
import com.fz.tms.service.algo.FZDeliveryAgent;
import com.fz.tms.service.algo.FZParams;
import com.fz.util.FZVrpUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 *
 */
public class AlgoExecutor {

//    public void run(HttpServletRequest request, HttpServletResponse response
//            , PageContext pc, String deliverySql, String vehicleSql
//    ) throws Exception {
//        
//        final String conName = "jdbc/fztms";
//        String result = "OK";
//        try {
//            // get runID param
//            String runID = FZVrpUtil.getHttpParam(request, "runID");
//
//            // prepare param placeholders
//            String branchCode = "";
//            String shift = "";
//            String tripCalc = "";
//            int maxIter = 0;
//
//// TODO: uncomment status
//
//            // prepare sql to get params
//            String sql = "select branch, shift, tripCalc, maxIter"
//                    + " from bosnet1.dbo.TMS_Progress"
//                    + " where runID = ?"
////                    + " and status = ?"
//                    ;
//
//            // open db con
//            try (Connection con = (new Db()).getConnection(conName);
//                    java.sql.PreparedStatement ps 
//                        = con.prepareStatement(sql)) {
//                
//                ps.setString(1, runID);
////                ps.setString(2, "NEW");
//
//                // begin transaction
//                con.setAutoCommit(false);
//                
//                // read run request in progress table
//                try (java.sql.ResultSet rs = ps.executeQuery()){
//
//                    // if has rows, get the param
//                    if (rs.next()){
//                        
//                        // update progress status as INPR, release transaction
//                        updateStatusAsInProgress(runID, con);
//                        con.setAutoCommit(true);
//                        
//                        // get the params
//                        branchCode = FZVrpUtil.getRsString(rs, 1, "");
//                        shift = FZVrpUtil.getRsString(rs, 2, "");
//                        tripCalc = FZVrpUtil.getRsString(rs, 3, "");
//                        maxIter = FZVrpUtil.getRsInt(rs, 4, 50);
//                        
//                    }
//                    else { // else
//                        
//                        // release transaction
//                        con.setAutoCommit(true);
//                        
//                        // set result = cannot find runID
//                        result = "Cannot find runID = " + runID;
//                    }
//                }
//
//                // if not OK, return
//                if (!result.equals("OK")) {
//                    response.getWriter().println("OK!");
//                    response.getWriter().flush();
//                }//return result;
//
//                // reach here means db read OK
//                //FZAlgoContext cx = new FZAlgoContext();
//                FZAlgoContext cx = new FZAlgoContext();
//
//                cx.params = readParams(con);
//                
//                cx.custDeliveries = readCustDeliveries(
//                        branchCode, shift, con, cx.params, deliverySql);
//                cx.deliveryAgents = readDeliveryAgents(
//                        branchCode, shift, con, cx.params, vehicleSql);
//                //cx.algoEventHandler = new WebEventHandler(cx);
//                cx.maxIteration = cx.params.getInt("MaxIteration");
//                cx.saveToDbFreq = cx.params.getInt("UpdateToDbFreq");
//                cx.timeDistSource = tripCalc;
//                cx.workFolder = cx.params.get("WorkingFolder");
//                
//                cx.runID = runID;
//                cx.prevCostDistRunID = "";
//                cx.branchCode = branchCode;
//                cx.shift = shift;
//        
//                //FZRouter.run(cx);
//                FSOptimizer o = new FSOptimizer(cx);
//                //o.doOptimize();
//                o.startOptimizerThread();
//
//            }
//
//        } catch(Exception e) {
//            result = FZVrpUtil.toStackTraceText(e);
//        }
//        //return result;
//        response.getWriter().println(result);
//    }
//
//    private void updateStatusAsInProgress(String runID
//            , Connection con) throws Exception {
//        
//        String sql = "update bosnet1.dbo.TMS_Progress"
//                + " set status = ?"
//                + " where runID = ?"
//                ;
//        try (PreparedStatement ps = con.prepareStatement(sql)){
//            ps.setString(1, "INPR");
//            ps.setString(2, runID);
//            ps.executeUpdate();
//        }
//    }
//
//    private FZParams readParams(Connection con) throws Exception {
//        FZParams params = new FZParams();
//        String sql = "select param, value from bosnet1.dbo.TMS_Params";
//        try (PreparedStatement ps = con.prepareStatement(sql);
//                ResultSet rs = ps.executeQuery()){
//            while (rs.next()){
//                params.put(rs.getString(1), rs.getString(2));
//            }
//        }
//        return params;
//    }
//    static List<FZCustDelivery> readCustDeliveries(String branchCode
//            , String shift
//            , Connection con
//            , FZParams params
//            , String deliverySql) 
//            throws Exception {
//
//        prepareCustTable(branchCode, con);
//        
//        List<FZCustDelivery> cds = new ArrayList<FZCustDelivery>();
//        String sql = deliverySql;
//        sql = sql.replaceAll("REPLACE_WITH_SHIFT", shift);
//        sql = sql.replaceAll("REPLACE_WITH_BRANCH", branchCode);
//        try (PreparedStatement ps = con.prepareStatement(sql)){
////            ps.setString(1, shift);
////            ps.setString(2, branchCode);
////            ps.setString(3, "N");
//            try (ResultSet rs = ps.executeQuery()){
//                while (rs.next()){
//                    
//                    FZCustDelivery cd = new FZCustDelivery();
//                    int i=1;
//                    
//                    cd.custID = FZVrpUtil.getRsString(rs, i++, "");
//                    cd.DONum = FZVrpUtil.getRsString(rs, i++, "");
//                    
//                    cd.lon = forceGetDouble(rs.getString(i++)
//                            , "Longitude error cust " + cd.custID);
//                    cd.lat = forceGetDouble(rs.getString(i++)
//                            , "Latitude error cust " + cd.custID);
//                    
//                    cd.priority = FZVrpUtil.getRsInt(rs, i++
//                            , params.getInt("DefaultCustPriority"));
//                    
//                    cd.serviceTime = FZVrpUtil.getRsInt(rs, i++
//                            , params.getInt("DefaultCustServiceTime"));
//                    
//                    cd.timeWindowStart = FZVrpUtil.getRsString(rs, i++
//                            , params.get("DefaultCustStartTime"));
//                    cd.timeWindowEnd = FZVrpUtil.getRsString(rs, i++
//                            , params.get("DefaultCustEndTime"));
//                    
//                    String vtl = FZVrpUtil.getRsString(rs, i++ 
//                            , params.get("DefaultCustVehicleTypes")); // vehicle type list
//                    
//                    cd.totalKg = FZVrpUtil.getRsString(rs, i++, "");
//                    cd.totalVolume = FZVrpUtil.getRsString(rs, i++, "");
//                    if (params.get("CapacityInWeightOrVolume")
//                            .equals("Volume")){
//                        cd.totalCapacity = forceGetDouble(cd.totalVolume
//                                , "Volume error cust " + cd.custID);
//                    }
//                    else {
//                        cd.totalCapacity = forceGetDouble(cd.totalKg
//                                , "Weight error cust " + cd.custID);
//                    }
//                    
//                    String[] vts = vtl.split("\\|"); // vehicle types
//                    for (String vt : vts){
//                        cd.agentTypes.add(vt);
//                    }
//                    cds.add(cd);
//                }
//            }
//        }
//        return cds;
//    }
//
//    private static void prepareCustTable(String branchCode, Connection con) 
//        throws Exception {
//        String sql = "EXEC bosnet1.dbo.GetCustLongLat ?";
//        try (PreparedStatement ps = con.prepareStatement(sql)){
//            ps.setEscapeProcessing(true);
//            ps.setQueryTimeout(15);
//            ps.setString(1, branchCode);
//            ps.execute();
//        }
//    }
//
//    private static double forceGetDouble(String s, String errMsg) 
//    throws Exception {
//        try {
//            double d = Double.parseDouble(s);
//            return d;
//        }
//        catch(Exception e){
//            throw new Exception(errMsg); 
//        }
//    }
//
//    private List<FZDeliveryAgent> readDeliveryAgents(String branchCode
//            , String shift, Connection con, FZParams params, String vehicleSql) 
//        throws Exception {
//        
////VehicleID	MaxKG	VehicleCateg	MinStartTime	MaxEndTime	StartLon	StartLat	EndLon	EndLat	MaxCubication
//        List<FZDeliveryAgent> das = new ArrayList<FZDeliveryAgent>();
//        
//        String sql = vehicleSql;
//        sql = sql.replaceAll("REPLACE_WITH_SHIFT", shift);
//        sql = sql.replaceAll("REPLACE_WITH_BRANCH", branchCode);
//        
//        try (PreparedStatement ps = con.prepareStatement(sql)){
//            
////            ps.setString(1, branchCode);
////            ps.setString(2, branchCode);
//            
//            try (ResultSet rs = ps.executeQuery()){
//                while (rs.next()){
//                    FZDeliveryAgent da = new FZDeliveryAgent();
//                    
//                    int i = 1;
//                    da.agentID = FZVrpUtil.getRsString(rs, i++, "");
//                    da.maxKg = FZVrpUtil.getRsDouble(rs, i++, 0)
//                            * params.getDouble("MaxLoadFactor");
//                    da.maxVolume = FZVrpUtil.getRsDouble(rs, i++, 0)
//                            * params.getDouble("MaxLoadFactor");
//                    da.agentType = FZVrpUtil.getRsString(rs, i++, "");
//                    da.branchCode = FZVrpUtil.getRsString(rs, i++, "");
//                    da.startLon = FZVrpUtil.getRsDouble(rs, i++, 0);
//                    da.startLat = FZVrpUtil.getRsDouble(rs, i++, 0);
//                    da.endLon = FZVrpUtil.getRsDouble(rs, i++, 0);
//                    da.endLat = FZVrpUtil.getRsDouble(rs, i++, 0);
//                    da.earliestDepartTime = FZVrpUtil.getRsString(rs, i++
//                            , params.get("DefaultVehicleStartTime"));
//                    da.latestArrivalTime = FZVrpUtil.getRsString(rs, i++
//                            , params.get("DefaultVehicleEndTime"));
//                    
//                    if (params.get("CapacityInWeightOrVolume")
//                            .equals("Volume")){
//                        da.maxCapacity = da.maxVolume;
//                    }
//                    else {
//                        da.maxCapacity = da.maxKg;
//                    }
//                    
//                    da.source = FZVrpUtil.getRsString(rs, i++, "");
//                    das.add(da);
//                }
//            }
//        }
//        return das;
//    }
}
