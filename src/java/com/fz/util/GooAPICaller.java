/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.util;

import com.google.gson.Gson;
import java.net.URL;
import org.json.JSONObject;

/**
 *
 * @author Eri Fizal
 */
public class GooAPICaller {

////  old mediatract
//    private final String key = "78kE37r_JfuGutwtupaHGcH5kdw=";
//    private final String clientID = "gme-mediatracsistemkomunikasi";
        
//    // SDN's, 2017-09-27
//    private final String key = "XBzLBGnYLscAto8pgSeXIRce";
//    private final String clientID = "180317705838-9jb8pfq0l9qqr4n5crno2nule8uhnvp0.apps.googleusercontent.com";
        
    // SDN's, 2017-11-13
    private final String key = "AIzaSyBOsad8CCGx7acE9H_c-27JVH-qqKzei20";
    private final String clientID = "180317705838-9jb8pfq0l9qqr4n5crno2nule8uhnvp0.apps.googleusercontent.com";
        
    UrlSigner signer = null; 
    
    public GooAPICaller() throws Exception{
        signer = new UrlSigner(key);
    }

    public void go(String urlString
            , StringBuffer resultUrl
            , StringBuffer resultJson
    ) 
            throws Exception {
        
        URL url = new URL(urlString + "?key=" + this.key);

        String finalURL = url.toString();
        System.out.println("URL=" + finalURL);
        
        String result = UrlResponseGetter.getURLResponse(finalURL);
        
        JSONObject json = new JSONObject(result);
        if (!json.getString("status").equals("OK")){
            System.out.println("");
        }
        
        resultUrl.append(finalURL);
        resultJson.append(result);

    }

    public void go_ORIG(String urlString
            , StringBuffer resultUrl
            , StringBuffer resultJson
    ) 
            throws Exception {
        
        URL url = new URL(urlString
                + "&client=" + clientID
        );

        String request = signer.signRequest(url.getPath(),url.getQuery());
        String finalURL = url.getProtocol() + "://" + url.getHost() + request;
        System.out.println("URL=" + finalURL);
        
        String result = UrlResponseGetter.getURLResponse(finalURL);
        
        resultUrl.append(finalURL);
        resultJson.append(result);

    }

    public void getDist(String origLatLons, String destLatLons
            , StringBuffer resultUrl
            , StringBuffer resultJson
    ) throws Exception {
        
        // call google API 
        String urlString =
            "https://maps.googleapis.com/maps/api/distancematrix/json"
                + "?origins=" + origLatLons
                + "&destinations=" + destLatLons
                + "&departure_time=now"
                + "&traffic_model=best_guess"
                ;

        URL url = new URL(urlString
                + "&client=" + clientID
        );

        String request = signer.signRequest(url.getPath(),url.getQuery());
        String finalURL = url.getProtocol() + "://" + url.getHost() + request;
        System.out.println("URL=" + finalURL);
        
        String result = UrlResponseGetter.getURLResponse(finalURL);
        
        resultUrl.append(finalURL);
        resultJson.append(result);

    }
    
//    public void getDist(String origLatLons, String destLatLons
//            , StringBuffer resultUrl
//            , StringBuffer resultJson
//    ) throws Exception {
//        
//        // call google API 
//        String urlString =
//            "https://maps.googleapis.com/maps/api/distancematrix/json"
//                + "?origins=" + origLatLons
//                + "&destinations=" + destLatLons
//                + "&departure_time=now"
//                + "&traffic_model=best_guess"
//                ;
//
//        URL url = new URL(urlString
//                + "&client=" + clientID
//        );
//
//        String request = signer.signRequest(url.getPath(),url.getQuery());
//        String finalURL = url.getProtocol() + "://" + url.getHost() + request;
//        System.out.println("URL=" + finalURL);
//
//        String result = UrlResponseGetter.getURLResponse(finalURL);
//        
//        resultUrl.append(finalURL);
//        resultJson.append(result);
//
//    }
    
}
