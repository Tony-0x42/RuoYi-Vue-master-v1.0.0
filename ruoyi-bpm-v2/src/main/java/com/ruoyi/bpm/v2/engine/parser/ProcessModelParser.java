package com.ruoyi.bpm.v2.engine.parser;

import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.bpm.v2.engine.model.BpmProcessModel;
import com.ruoyi.common.utils.StringUtils;

/**
 * 流程模型解析器
 */
@Component
public class ProcessModelParser {

    /**
     * 解析扩展 JSON
     */
    public BpmProcessModel parse(String extJson) {
        if (StringUtils.isEmpty(extJson)) {
            return new BpmProcessModel();
        }
        return JSON.parseObject(extJson, BpmProcessModel.class);
    }

    /**
     * 序列化为 JSON
     */
    public String serialize(BpmProcessModel model) {
        if (model == null) {
            return "{}";
        }
        return JSON.toJSONString(model);
    }
}
