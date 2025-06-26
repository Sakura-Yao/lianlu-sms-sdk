package com.dmo.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ITemplateSMS {

    @JSONField(name = "MchId")
    private String mchId;

    @JSONField(name = "AppId")
    private String appId;

    @JSONField(name = "Version")
    private String version;

    @JSONField(name = "Type")
    private String type;

    @JSONField(name = "PhoneNumberSet")
    private List<String> phoneNumberSet;

    @JSONField(name = "TemplateId")
    private String templateId;

    @JSONField(name = "TemplateParamSet")
    private List<String> templateParamSet;

    @JSONField(name = "TimeStamp")
    private String timeStamp;

    @JSONField(name = "SignType")
    private String signType;

    @JSONField(name = "Signature")
    private String signature;

}
