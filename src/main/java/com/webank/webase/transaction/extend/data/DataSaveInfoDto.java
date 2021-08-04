package com.webank.webase.transaction.extend.data;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/8/3 17:18
 */
@Data
public class DataSaveInfoDto {
    private String transHash;
    private Map<String,String> Output;
    private boolean receiptStatus;
    private String transMassage;
    private String blockNumber;
}
