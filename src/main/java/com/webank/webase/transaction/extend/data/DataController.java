package com.webank.webase.transaction.extend.data;

import com.webank.webase.transaction.base.BaseController;
import com.webank.webase.transaction.base.ConstantCode;
import com.webank.webase.transaction.base.ResponseEntity;
import com.webank.webase.transaction.base.exception.BaseException;
import com.webank.webase.transaction.contract.ContractService;
import com.webank.webase.transaction.contract.entity.ReqDeployInfo;
import com.webank.webase.transaction.trans.TransService;
import com.webank.webase.transaction.trans.entity.ReqTransCallInfo;
import com.webank.webase.transaction.trans.entity.ReqTransSendInfo;
import com.webank.webase.transaction.trans.entity.TransInfoDto;
import com.webank.webase.transaction.util.AddressUtils;
import com.webank.webase.transaction.base.exception.AddressException;
import com.webank.webase.transaction.base.exception.Web3Exception;
import com.webank.webase.transaction.util.CommonUtils;
import com.webank.webase.transaction.util.JsonUtils;
import com.webank.webase.transaction.util.result.ResultEnum;
import com.webank.webase.transaction.util.result.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/7/16 9:49
 */
@Api(value = "/data", tags = "数据合约")
@Slf4j
@RestController
@RequestMapping(value = "/data")
public class DataController extends BaseController {

    @Autowired
    ContractService contractService;

    @Autowired
    TransService transService;

    @Value("${sdk.groupConfig.allChannelConnections[0].groupId}")
    private Integer groupId;


    @ApiOperation(value = "部署数据合约")
    @PostMapping("/deployDataContract")
    public ResponseEntity deployDataContract(@Valid @RequestBody DataDeployInfo dataDeployInfo, BindingResult result) throws BaseException, InterruptedException {

        log.info("deployDataContract start. deployInfo:{}", JsonUtils.toJSONString(dataDeployInfo));
        checkParamResult(result);
        ReqDeployInfo reqDeployInfo = new ReqDeployInfo();
        reqDeployInfo.setGroupId(groupId);
        reqDeployInfo.setUuidDeploy(dataDeployInfo.getUuidDeploy());
        reqDeployInfo.setContractAbi(CommonUtils.getContractAbi("abi/Data.abi"));
        reqDeployInfo.setContractBin(CommonUtils.getContractBin("bin/Data.bin"));
        reqDeployInfo.setSignType(dataDeployInfo.getSignType());
        reqDeployInfo.setSignUserId(dataDeployInfo.getSignUserId());
        final CountDownLatch latch= new CountDownLatch(1);
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    contractService.deploy(reqDeployInfo);
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println("子线程执行！");
                latch.countDown();//让latch中的数值减一
            }
        }).start();
        latch.await();
        return contractService.getAddress(groupId, dataDeployInfo.getUuidDeploy());
    }

    @ApiOperation(value = "查找数据")
    @PostMapping("/getData")
    public ResponseEntity getData(@Valid @RequestBody DataGetInfo dataGetInfo,BindingResult result) throws BaseException {
        log.info("getData start. Info:{}", JsonUtils.toJSONString(dataGetInfo));
        checkParamResult(result);

        ReqTransCallInfo reqTransCallInfo = new ReqTransCallInfo();
        reqTransCallInfo.setGroupId(groupId);
        reqTransCallInfo.setUuidDeploy(dataGetInfo.getUuidDeploy());
        List<Object> funcParam = new ArrayList<>();
        funcParam.add(dataGetInfo.getKey());
        reqTransCallInfo.setFuncParam(funcParam);
        reqTransCallInfo.setContractAbi(CommonUtils.getContractAbi("abi/Data.abi"));
        reqTransCallInfo.setContractAddress(dataGetInfo.getContractAddress());
        String funcName = new String();

        switch (dataGetInfo.getKey().length()){
            case 10:
                funcName = "getData";
                break;
            case 8:
                funcName = "dailyDatas";
                break;
            case 6:
                funcName = "monthlyDatas";
                break;
            case 4:
                funcName = "annualDatas";
                break;
            default:
                funcName = "rootData";
                break;
        }
        reqTransCallInfo.setFuncName(funcName);
        return transService.call(reqTransCallInfo);

    }

    @ApiOperation(value = "上传数据")
    @PostMapping("/saveData")
    public ResponseEntity saveData(@Valid @RequestBody DataSaveInfo dataSaveInfo, BindingResult result) throws BaseException, InterruptedException {
        log.info("saveData start. dataSaveInfo:{}", JsonUtils.toJSONString(dataSaveInfo));
        checkParamResult(result);

        String regex="^[0-9]{10}$";
        if(!dataSaveInfo.getKey().matches(regex)){
            throw new BaseException(ConstantCode.DATA_FORMAT_ERROR);
        }
        BigInteger year = new BigInteger(dataSaveInfo.getKey().substring(0,4));
        if(year.intValue()>2050||year.intValue()<1990){
            throw new BaseException(ConstantCode.YEAR_FORMAT_ERROR);
        }
        BigInteger month = new BigInteger(dataSaveInfo.getKey().substring(4,6));
        if(month.intValue()>12||month.intValue()<0){
            throw new BaseException(ConstantCode.MONTH_FORMAT_ERROR);
        }
        BigInteger day = new BigInteger(dataSaveInfo.getKey().substring(6,8));
        if(day.intValue()>getMonthDays(year.intValue(),month.intValue())||day.intValue()<0){
            throw new BaseException(ConstantCode.DAY_FORMAT_ERROR);
        }
        BigInteger hour = new BigInteger(dataSaveInfo.getKey().substring(8,10));
        if(hour.intValue()>24||hour.intValue()<0){
            throw new BaseException(ConstantCode.HOUR_FORMAT_ERROR);
        }

        ReqTransSendInfo reqTransSendInfo = new ReqTransSendInfo();
        reqTransSendInfo.setGroupId(groupId);
        reqTransSendInfo.setUuidStateless(dataSaveInfo.getUuidStateless());
        reqTransSendInfo.setUuidDeploy(dataSaveInfo.getUuidDeploy());
        reqTransSendInfo.setSignType(dataSaveInfo.getSignType());
        reqTransSendInfo.setSignUserId(dataSaveInfo.getSignUserId());
        reqTransSendInfo.setFuncName("saveData");
        List<Object> params = new ArrayList<>();
        params.add(year);
        params.add(month);
        params.add(day);
        params.add(hour);
        params.add(dataSaveInfo.getHourData());
        reqTransSendInfo.setFuncParam(params);

        final CountDownLatch latch= new CountDownLatch(1);
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    transService.save(reqTransSendInfo);
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println("子线程执行！");
                latch.countDown();//让latch中的数值减一
            }
        }).start();
        latch.await();

        DataSaveInfoDto dataSaveInfoDto = transService.getDataSaveInfoDto(groupId, dataSaveInfo.getUuidStateless());
        return ResultUtils.success(dataSaveInfoDto);

    }

    private int getMonthDays(int _year,int _month){
        boolean isLeapYear = false;
        int daysNum = 0;
        if((_year %4 == 0 && _year%100 != 0) || _year % 400 ==0){
            isLeapYear = true;
        }
        if(_month == 1 || _month == 3 || _month == 5 || _month == 7 || _month == 8 || _month == 10 || _month == 12){
            daysNum = 31;
        }else if(_month == 2){
            if(isLeapYear){
                daysNum = 29;
            }else{
                daysNum = 28;
            }
        }else{
            daysNum = 30;
        }
        return daysNum;
    }

}
