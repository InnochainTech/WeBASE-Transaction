package com.webank.webase.transaction.util;

/**
 * @Description: 地址工具类
 * @Author peifeng
 * @Date 2021/7/19 11:21
 */
public class AddressUtils {
    public static boolean isPrivateKey(String input){
        String regex="^[0-9a-fA-F]{64}$";
        if(input.matches(regex)){
            return true;
        }else{
            return false;
        }
    }
}
