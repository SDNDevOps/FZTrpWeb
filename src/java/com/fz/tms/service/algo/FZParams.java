/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.tms.service.algo;

import java.util.HashMap;

/**
 *
 */
public class FZParams {
    private HashMap<String, String> params
            = new HashMap<String, String>();
    
    public void put(String param, String value) {
        params.put(param, value);
    }
    
    public String get(String param) throws Exception {
        String s = params.get(param);
        if (s == null) throw new Exception("Param " + param + " not exist");
        return s;
    }
    
    public double getDouble(String param) throws Exception {
        double d = 0;
        try {
            d = Double.parseDouble(params.get(param));
            return d;
        }
        catch(Exception e){
            throw new Exception("Param " + param + " invalid, float expected");
        }
    }
    
    public int getInt(String param) throws Exception {
        int d = 0;
        try {
            d = Integer.parseInt(params.get(param));
            return d;
        }
        catch(Exception e){
            throw new Exception("Param " + param + " invalid, integer expected");
        }
    }
}
