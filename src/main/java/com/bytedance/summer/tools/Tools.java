package com.bytedance.summer.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author pjl
 * @date 2019/8/15 11:14
 * 工具类
 */

public class Tools {
    /*
    给pid和uid算出order_id
     */
    public static String getOrderId(Long pid, Integer uid, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date) + String.format("%010d", pid) +String.format("%09d", uid);
    }
    public static Long getPidFromOrderId(String orderId){
        String pidStr=orderId.substring(14,24);
        return Long.valueOf(pidStr);
    }
    public static Integer getUidFromOrderId(String orderId){
        String uidStr=orderId.substring(24,33);
        return Integer.valueOf(uidStr);
    }
    public static String getTimeFromOrderId(String orderId){
        return orderId.substring(0,14);
    }
    public static void main(String[] args){

        Long a = 998L;
        Long b= 3132132132L;
        String oid=getOrderId(a,123456,new Date());
        String oid2= getOrderId(b,123456789,new Date());
        System.out.println(oid);
        System.out.println(oid2);
        System.out.println(getTimeFromOrderId(oid));
        System.out.println(getPidFromOrderId(oid));
        System.out.println(getUidFromOrderId(oid));
        System.out.println(getTimeFromOrderId(oid2));
        System.out.println(getPidFromOrderId(oid2));
        System.out.println(getUidFromOrderId(oid2));
    }
}
