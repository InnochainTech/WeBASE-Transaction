package com.webank.webase.transaction.extend.data;

import com.webank.webase.transaction.base.ConstantCode;
import com.webank.webase.transaction.util.CommonUtils;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/8/3 14:53
 */
@Data
public class DataDeployInfo {

    @NotBlank(message = ConstantCode.UUID_IS_EMPTY)
    private String uuidDeploy;
    @NotNull(message = ConstantCode.SIGN_TYPE_IS_EMPTY)
    private Integer signType;
    private String signUserId;
    private List<Object> funcParam = new ArrayList<>();
}
