/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.tms.service.algo;

import com.fz.fit.FNRunner;
import com.fz.util.FZUtil;
import java.util.Collection;

/**
 *
 */
public class FZRouter {

    public static void run(FZAlgoContext cx) throws Exception {

        cx.algoEventHandler.onProgress("Algorithm is running, please wait ..."
                , 0, cx);
//        (new Thread() {
//          public void run() {

              try {
                  
                // new smackRefit non-thread version
                (new FNRunner()).run(cx);
                
                
              } catch (Exception e) {
                  cx.algoEventHandler.onError(e, cx);
              }

//          }
//
//         }).start();
    }



}
