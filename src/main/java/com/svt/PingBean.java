package com.svt;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.json.simple.JSONObject;

@Stateless
@LocalBean
public class PingBean {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static AtomicInteger atomicCount = new AtomicInteger();

    private String getString(int count){
         StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
          int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
          builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public JSONObject ping(long calledAt, int sleep, int len) {
        int counterWhenIn=atomicCount.incrementAndGet();
        long d1=System.currentTimeMillis();
        long launchDelay = d1 - calledAt ;
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(PingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        long d2=System.currentTimeMillis();
        long duration=d2-d1;
        int counterWhenOut=atomicCount.getAndDecrement();
        String clazz=this.getClass().getSimpleName();
        JSONObject j=new JSONObject();
        j.put("className",clazz);
        j.put(clazz+".atomicCountIn",counterWhenIn);
        j.put(clazz+".atomicCountOut",counterWhenOut);
        j.put(clazz+".launchDelay",launchDelay);
        j.put(clazz+".duration",duration);
        j.put(clazz+".datas", this.getString(len));
        return(j);
    }
}