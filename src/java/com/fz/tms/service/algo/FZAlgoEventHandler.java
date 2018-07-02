/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.tms.service.algo;

/**
 *
 * @author Eri Fizal
 */
public interface FZAlgoEventHandler {
    public abstract void onError(Exception e, FZAlgoContext cx);
    public abstract void onProgress(String msg, double percent
            , FZAlgoContext cx) throws Exception;
    public abstract void onDone(FZAlgoContext cx) throws Exception;
}
