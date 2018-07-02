/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.tms.service.algoCaller;

import com.fz.tms.service.algoCaller.Db;
import com.fz.util.FZVrpUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.fz.tms.service.algo.FZAlgoContext;
import com.fz.tms.service.algo.FZAlgoEventHandler;
import com.fz.tms.service.algo.FZRouteJob;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 */
public class WebEventHandler implements FZAlgoEventHandler {

    public FZAlgoContext cx;

    public WebEventHandler(FZAlgoContext cx) throws Exception {
        this.cx = cx;
    }
    
    @Override
    public void onError(Exception e, FZAlgoContext cx) {
        
        cx.mustFinish = true;
        String msg = FZVrpUtil.toStackTraceText(e);
        
        System.out.println(msg);
        
        try {
            tryWriteToWeb(msg, 0, cx);
            tryLogToDb(msg, cx);
            cx.response.getWriter().write(msg);
            //FZVrpUtil.logSlowly("C:\\fza\\log\\log\\", "FZWebTrp", msg);
        }
        catch(Exception e2){
            System.out.println("Error at err handler: " + e2.getMessage());
        }
    }

    private void tryWriteToWeb(String msg, double percent, FZAlgoContext cx){
        try {
            cx.response.getWriter().write("\n<br>" + msg);
            cx.response.getWriter().flush();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void onProgress(String msg, double percent, FZAlgoContext cx) 
            throws Exception {
        
        // if already finish, ignore
        if (cx.mustFinish) return;

        tryWriteToWeb(msg, percent, cx);
        
        // update db only once in every dbAccessTimes loop
        // if reach end of update cycle, restart cycle
        ++cx.dbAccessTimes;
        if (cx.dbAccessTimes >= cx.saveToDbFreq) {
            cx.dbAccessTimes = 0;
        }
        // if above 3 loop in a cycle, dont write
        if (cx.dbAccessTimes > 3 ) {
            
            // do not access db, so just return
            return;
        } 
        
        // prepare sql
        String sql = "select mustFinish "
                + " from bosnet1.dbo.TMS_Progress "
                + " where runID = ?"
                ;
        
        try (Connection con = (new Db()).getConnection("jdbc/fztms");
                PreparedStatement ps = con.prepareStatement(sql) ){
            
            // set current run id progress
            ps.setString(1, cx.runID);
            try (ResultSet rs = ps.executeQuery()){
                
                // if has record
                if (rs.next()){
                    
                    // if must finish
                    String mustFinish = FZVrpUtil.getRsString(rs, 1, "0");
                    if (mustFinish.equals("1")){
                        
                        // set must finish
                        cx.mustFinish = true;
                        //updateProgressInDb(con, msg, percent, "FNSH");
                    }
                    else {
                        
                        // else, update progress
                        updateProgressInDb(con, msg, percent, "INPR");
                    }
                }
                else {
                    insertProgressInDb(con, msg, percent);
                }
            }
        }
        
    }


    public void updateProgressInDb(Connection con, String msg, double percent
            , String status) 
        throws Exception {
        
        String sql = "update bosnet1.dbo.TMS_Progress set "
                + " msg = ?"
                + ", pct = ?"
                + ", status = ?"
                + ", lastUpd = ?"
                + " where runID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, msg);
            ps.setDouble(2, percent);
            ps.setString(3, status);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setString(5, cx.runID);
            ps.executeUpdate();
        }
    }

    private void insertProgressInDb(Connection con, String msg, double percent) 
        throws Exception {
        
        String sql = "insert into bosnet1.dbo.TMS_Progress(msg, pct, runID) "
                + " values(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, msg);
            ps.setDouble(2, percent);
            ps.setString(3, cx.runID);
            ps.executeUpdate();
        }
    }

    @Override
    public void onDone(FZAlgoContext cx) throws Exception {
//        // save to db
//        String sql = "insert into bosnet1.dbo.TMS_RouteJob("
//            + "job_id"
//            + ", customer_id"
//            + ", do_number"
//            + ", vehicle_code"
//            + ", activity"
//            + ", routeNb"
//            + ", jobNb"
//            + ", arrive"
//            + ", depart"
//            + ", runID"
//            + ", create_dtm"
//            + ", branch"
//            + ", shift"
//            + ", lon"
//            + ", lat"
//            + ", weight"
//            + ", volume"
//            + ", transportCost"
//            + ", activityCost"
//            + ", dist"
//            + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
//        ;
//        
//        try (Connection con = (new Db()).getConnection("jdbc/fztms")){
//
//            try (PreparedStatement ps = con.prepareStatement(sql) ){
//                
//                // routed jobs
//                for (FZRouteJob j : cx.routeJobs){
//
//                    ps.clearParameters();
//
//                    int i = 1;
//                    ps.setString(i++, j.siteID);
//                    ps.setString(i++, j.custID);
//                    ps.setString(i++, j.DONum);
//                    ps.setString(i++, j.vehicleCode);
//                    ps.setString(i++, j.activity);
//                    ps.setInt(i++, j.routeNb);
//                    ps.setInt(i++, j.jobNb);
//                    ps.setString(i++, j.arrive);
//                    ps.setString(i++, j.depart);
//                    ps.setString(i++, j.runID);
//                    ps.setString(i++, FZVrpUtil.getCurTime());
//                    ps.setString(i++, j.branch);
//                    ps.setString(i++, j.shift);
//                    ps.setString(i++, j.lon);
//                    ps.setString(i++, j.lat);
//                    ps.setString(i++, j.weight);
//                    ps.setString(i++, j.volume);
//                    ps.setDouble(i++, j.transportCost);
//                    ps.setDouble(i++, j.activityCost);
//                    ps.setDouble(i++, j.dist);
//
//                    ps.addBatch();
//                }
//                
//                // unasg jobs
//                for (FZRouteJob j : cx.unAsgJobs){
//
//                    ps.clearParameters();
//
//                    int i = 1;
//                    ps.setString(i++, j.siteID);
//                    ps.setString(i++, j.custID);
//                    ps.setString(i++, j.DONum);
//                    ps.setString(i++, j.vehicleCode); 
//                    ps.setString(i++, j.activity);
//                    ps.setInt(i++, j.routeNb); 
//                    ps.setInt(i++, j.jobNb);
//                    ps.setString(i++, j.arrive);
//                    ps.setString(i++, j.depart);
//                    ps.setString(i++, j.runID);
//                    ps.setString(i++, FZVrpUtil.getCurTime());
//                    ps.setString(i++, j.branch);
//                    ps.setString(i++, j.shift);
//                    ps.setString(i++, j.lon);
//                    ps.setString(i++, j.lat);
//                    ps.setString(i++, j.weight);
//                    ps.setString(i++, j.volume);
//                    ps.setDouble(i++, j.transportCost);
//                    ps.setDouble(i++, j.activityCost);
//                    ps.setDouble(i++, j.dist);
//
//                    ps.addBatch();
//                }
//                ps.executeBatch();
//                updateProgressInDb(con, "Done", 100, "DONE");
//            }
//        }
    }

    private void tryLogToDb(String msg, FZAlgoContext cx) {
        try {
            String sql = "update bosnet1.dbo.TMS_Progress"
                    + " set msg = ?"
                    + " , status = ?"
                    + " , mustFinish = ?"
                    + " where runID = ?"
                    ;
            try (Connection con = (new Db()).getConnection("jdbc/fztms");
                    PreparedStatement ps = con.prepareStatement(sql) ){
                ps.setString(1, msg);
                ps.setString(2, "ERR");
                ps.setString(3, "1");
                ps.setString(4, cx.runID);
                ps.executeUpdate();
            }
        } catch (Exception e){
            // nothing, this is a try only
        }
    }
    
}
