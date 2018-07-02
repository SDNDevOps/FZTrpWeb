/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.util;

import java.sql.Connection;

/**
 *
 */
public class Db {
    
    // dbName as per set in context.xml and web.xml
    // example: jdbc/fz
    public Connection getConnection(String dbName) throws Exception {
        javax.naming.InitialContext ctx = 
                new javax.naming.InitialContext();
        javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup(
                "java:/comp/env/" + dbName);
        java.sql.Connection con = ds.getConnection();
        return con;
    }

}
