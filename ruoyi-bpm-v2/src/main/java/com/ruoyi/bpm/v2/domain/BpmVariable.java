package com.ruoyi.bpm.v2.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 变量定义 bpm_variable
 */
@Alias("BpmV2Variable")
public class BpmVariable extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 变量ID */
    @Excel(name = "变量ID")
    private Long variableId;

    /** 分类ID */
    private Long categoryId;

    /** 流程定义ID */
    private Long definitionId;

    /** 变量名称 */
    @Excel(name = "变量名称")
    private String variableName;

    /** 变量编码 */
    @Excel(name = "变量编码")
    private String variableCode;

    /** 变量类型（0字符串 1数字 2布尔 3日期） */
    @Excel(name = "变量类型", readConverterExp = "0=字符串,1=数字,2=布尔,3=日期")
    private String variableType;

    /** 默认值 */
    @Excel(name = "默认值")
    private String defaultValue;

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId) {
        this.definitionId = definitionId;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableCode() {
        return variableCode;
    }

    public void setVariableCode(String variableCode) {
        this.variableCode = variableCode;
    }

    public String getVariableType() {
        return variableType;
    }

    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("variableId", getVariableId())
                .append("categoryId", getCategoryId())
                .append("definitionId", getDefinitionId())
                .append("variableName", getVariableName())
                .append("variableCode", getVariableCode())
                .append("variableType", getVariableType())
                .append("defaultValue", getDefaultValue())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
