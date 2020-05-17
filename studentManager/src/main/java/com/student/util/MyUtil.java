package com.student.util;

import java.util.Date;
import java.util.List;

public class MyUtil {
    public static  String joinString(List<Integer> list,String split) {
        String str = "";
        for(Integer integer:list) {
            str += integer+split;
        }
        if(!str.equals("")) {
         //   str = str.substring(0,str.length() - split.length());
            str = str.substring(0,str.length() - 1);
        }
        return str;
    }
    //生成学号的方法
    public static String genernateSn(String prefix,String suffix) {
        return  prefix + new Date().getTime() + suffix;
    }
}
