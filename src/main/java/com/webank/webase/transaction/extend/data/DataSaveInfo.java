package com.webank.webase.transaction.extend.data;

import com.webank.webase.transaction.base.ConstantCode;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/8/3 14:53
 */
@Data
public class DataSaveInfo {

    @NotBlank(message = ConstantCode.UUIDSTATELESS_IS_EMPTY)
    private String uuidStateless;

    @NotBlank(message = ConstantCode.UUID_IS_EMPTY)
    private String uuidDeploy;

    @NotNull(message = ConstantCode.SIGN_TYPE_IS_EMPTY)
    private Integer signType;

    private String signUserId;

    @NotBlank(message = ConstantCode.KEY_IS_EMPTY)
    private String key;

    @NotBlank(message = ConstantCode.HOUR_DATA_IS_EMPTY)
    private String hourData;

}
