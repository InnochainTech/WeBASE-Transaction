package com.webank.webase.transaction.util.result;

public enum ResultEnum {
    UNKONW_ERROR(-1, "未知错误"),
            SUCCESS(200, "成功"),
            FORMAT_ERROR(399,"日期字符格式错误"),
            YEAR_ERROR(398,"年份格式错误"),
            MONTH_ERROR(397,"月份格式错误"),
            DAY_ERROR(396,"日期格式错误"),
            HOUR_ERROR(395,"小时格式错误"),
            PRIVATEKEY_ERROR(499,"私钥格式错误"),
            ERROR(599,"发送交易失败"),
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

