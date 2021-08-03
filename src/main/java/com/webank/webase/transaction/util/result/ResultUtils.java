package com.webank.webase.transaction.util.result;

import com.webank.webase.transaction.base.ResponseEntity;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 15:47
 */
public class ResultUtils {
    public static ResponseEntity success(Object object) {
        ResponseEntity result = new ResponseEntity();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(object);
        return result;
    }

    public static ResponseEntity success() {
        return success(null);
    }

    public static ResponseEntity error(Integer code, String msg) {
        ResponseEntity result = new ResponseEntity();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
}
