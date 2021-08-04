package com.webank.webase.transaction.util;

import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/8/4 10:49
 */
public class ExtendsStringUtils {

    private static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public static String encodeHexString(Bytes32 byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.getValue().length; i++) {
            hexStringBuffer.append(byteToHex(byteArray.getValue()[i]));
        }
        return hexStringBuffer.toString();
    }

    public static Map<String,String> getDataSaveReturn(String output){
        Map<String,String> returnMap = new HashMap<>();
        if(!StringUtils.isEmpty(output)&&output.length()==258){
            returnMap.put("rootData","0x"+output.substring(2,66));
            returnMap.put("yearData","0x"+output.substring(66,130));
            returnMap.put("monthData","0x"+output.substring(130,194));
            returnMap.put("dayData","0x"+output.substring(194,258));
        }
        return returnMap;
    }

    public static int hex2decimal(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static void main(String[] args) {
        System.out.println(hex2decimal("c9"));
    }
}
