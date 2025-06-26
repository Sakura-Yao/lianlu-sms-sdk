package com.dmo.sdk;

import com.alibaba.fastjson2.JSONObject;
import com.dmo.dto.ITemplateSMS;
import com.dmo.dto.OTemplateSMS;
import com.dmo.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * 联麓云短信平台 SDK（线程安全、无状态）
 */
@Slf4j
public class LianLuSms {

    private static final String TEMPLATE_SMS_URL = "https://api.shlianlu.com/sms/trade/template/send";

    private final String appKey;

    private final String appId;

    private final String mchId;

    private static final String VERSION = "1.1.0";

    private static final String TYPE = "3";

    private static final String MD5 = "MD5";

    private static final String SUCCESS_CODE = "00";

    private final Boolean enableLog;

    public LianLuSms(String appKey, String appId, String mchId) {
        this.appKey = appKey;
        this.appId = appId;
        this.mchId = mchId;
        this.enableLog = true;
    }

    public LianLuSms(String appKey, String appId, String mchId, Boolean enableLog) {
        this.appKey = appKey;
        this.appId = appId;
        this.mchId = mchId;
        this.enableLog = enableLog;
    }

     /**
     * 发送短信至多个手机号
     * @param smsTemplateId 短信模板ID，不能为空
     * @param phoneNumberList 手机号列表，不能为空
     * @param smsParamList 模板参数列表，可为 null
     * @return 是否发送成功
     */
    public boolean sendSmsToMultiplePhones(@NotNull String smsTemplateId, @NotNull List<String> phoneNumberList,
                                          List<String> smsParamList) {
        if (StringUtils.isBlank(smsTemplateId)) {
            errLog("`smsTemplateId`参数不能为空");
            return false;
        }
        if (phoneNumberList.isEmpty()) {
            errLog("`phoneNumberList`参数不能为空");
            return false;
        }
        ITemplateSMS iTemplateSMS = buildTemplateSmsBean(smsTemplateId, phoneNumberList, smsParamList);
        return sendSms(iTemplateSMS);
    }

    /**
     * 发送短信至多个手机号
     * @param smsTemplateId 短信模板ID，不能为空
     * @param phoneNumber 手机号，不能为空
     * @param smsParamList 模板参数列表，可为 null
     * @return 是否发送成功
     */
    public boolean sendSmsToSinglePhone(@NotNull String smsTemplateId, @NotNull String phoneNumber,
                                  List<String> smsParamList) {
        if (StringUtils.isBlank(smsTemplateId)) {
            errLog("`smsTemplateId`参数不能为空");
            return false;
        }
        if (StringUtils.isBlank(phoneNumber)) {
            errLog("`phoneNumber`参数不能为空");
            return false;
        }
        ITemplateSMS iTemplateSMS = buildTemplateSmsBean(smsTemplateId, Collections.singletonList(phoneNumber), smsParamList);
        return sendSms(iTemplateSMS);
    }

    /*
     * @Description : 发送短信
     * @author      : yaoyuan
     * @date        : 2025/6/25 16:16
     */
    private boolean sendSms(ITemplateSMS iTemplateSMS) {
        infoLog(JSONObject.toJSONString(iTemplateSMS));
        return Optional.ofNullable(callSendSMSApi(iTemplateSMS))
                .map(i -> Objects.equals(i.getStatus(), SUCCESS_CODE))
                .orElse(false);
    }

    /*
     * @Description : 调用SMS API
     * @author      : yaoyuan
     * @date        : 2025/6/25 16:09
     */
    private OTemplateSMS callSendSMSApi(ITemplateSMS iTemplateSMS) {
        try {
            String res = HttpUtil.post(TEMPLATE_SMS_URL, JSONObject.toJSONString(iTemplateSMS));
            infoLog(res);
            return JSONObject.parseObject(res, OTemplateSMS.class);
        } catch (IOException e) {
            errLog(e);
            return null;
        }
    }

    /*
     * @Description : 构建参数体
     * @author      : yaoyuan
     * @date        : 2025/6/25 15:56
     */
    private ITemplateSMS buildTemplateSmsBean(String smsTemplateId,
                                                     List<String> phoneNumberList,
                                                     List<String> smsParamList) {
        smsParamList = Objects.isNull(smsParamList) ? new ArrayList<>() : smsParamList;
        ITemplateSMS iTemplateSMS = ITemplateSMS.builder()
                .version(VERSION)
                .type(TYPE)
                .signType(MD5)
                .mchId(mchId)
                .appId(appId)
                .phoneNumberSet(phoneNumberList)
                .templateId(smsTemplateId)
                .templateParamSet(smsParamList)
                .timeStamp(String.valueOf(System.currentTimeMillis())).build();
        iTemplateSMS.setSignature(buildSignature(smsTemplateId, iTemplateSMS.getTimeStamp()));
        return iTemplateSMS;
    }

    /*
     * @Description : 计算签名
     * @author      : yaoyuan
     * @date        : 2025/6/25 16:03
     */
    private String buildSignature(String smsTemplateId, String timestamp) {
        String signature = "AppId=" + appId
                + "&MchId=" + mchId
                + "&SignType=MD5"
                + "&TemplateId=" + smsTemplateId
                + "&TimeStamp=" + timestamp
                + "&Type=3"
                + "&Version=1.1.0"
                + "&key=" + appKey;
        return DigestUtils.md5Hex(signature).toUpperCase();
    }

    private void infoLog(String msg) {
        if (Boolean.TRUE.equals(this.enableLog)) {
            log.info("联麓云SDK: {}", msg);
        }
    }

    private void errLog(String errMes) {
        log.error("联麓云SDK: 异常提示: {}", errMes);
    }

    private void errLog(Exception e) {
        log.error("联麓云SDK: 异常提示: ", e);
    }

}
