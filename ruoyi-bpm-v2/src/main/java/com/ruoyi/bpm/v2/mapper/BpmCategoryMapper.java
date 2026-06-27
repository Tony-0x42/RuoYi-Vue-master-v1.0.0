package com.ruoyi.bpm.v2.mapper;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.ruoyi.bpm.v2.domain.BpmCategory;

/**
 * 流程分类 数据层
 */
@Repository("bpmV2CategoryMapper")
public interface BpmCategoryMapper {

    /**
     * 通过ID查询流程分类
     */
    BpmCategory selectById(Long id);

    /**
     * 查询流程分类列表
     */
    List<BpmCategory> selectList(BpmCategory category);

    /**
     * 新增流程分类
     */
    int insert(BpmCategory category);

    /**
     * 修改流程分类
     */
    int update(BpmCategory category);

    /**
     * 删除流程分类
     */
    int deleteById(Long id);

    /**
     * 批量删除流程分类
     */
    int deleteByIds(Long[] ids);

    /**
     * 校验分类编码唯一性
     */
    BpmCategory checkCodeUnique(String code);
}
