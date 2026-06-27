package com.ruoyi.bpm.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bpm.domain.BpmWorkbenchComponent;
import com.ruoyi.bpm.domain.BpmWorkbenchComponentScope;
import com.ruoyi.bpm.mapper.BpmWorkbenchComponentMapper;
import com.ruoyi.bpm.mapper.BpmWorkbenchComponentScopeMapper;
import com.ruoyi.bpm.service.IBpmWorkbenchComponentService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;

/**
 * 工作台组件注册Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class BpmWorkbenchComponentServiceImpl implements IBpmWorkbenchComponentService
{
    @Autowired
    private BpmWorkbenchComponentMapper bpmWorkbenchComponentMapper;

    @Autowired
    private BpmWorkbenchComponentScopeMapper bpmWorkbenchComponentScopeMapper;

    @Override
    public BpmWorkbenchComponent selectBpmWorkbenchComponentById(Long componentId)
    {
        BpmWorkbenchComponent component = bpmWorkbenchComponentMapper.selectBpmWorkbenchComponentById(componentId);
        if (component != null)
        {
            BpmWorkbenchComponentScope scope = new BpmWorkbenchComponentScope();
            scope.setComponentId(componentId);
            component.setParams(new java.util.HashMap<>());
            component.getParams().put("scopeList", bpmWorkbenchComponentScopeMapper.selectBpmWorkbenchComponentScopeList(scope));
        }
        return component;
    }

    @Override
    public List<BpmWorkbenchComponent> selectBpmWorkbenchComponentList(BpmWorkbenchComponent bpmWorkbenchComponent)
    {
        return bpmWorkbenchComponentMapper.selectBpmWorkbenchComponentList(bpmWorkbenchComponent);
    }

    @Override
    @Transactional
    public int insertBpmWorkbenchComponent(BpmWorkbenchComponent bpmWorkbenchComponent)
    {
        validateComponentName(bpmWorkbenchComponent);
        int rows = bpmWorkbenchComponentMapper.insertBpmWorkbenchComponent(bpmWorkbenchComponent);
        insertComponentScope(bpmWorkbenchComponent);
        return rows;
    }

    @Override
    @Transactional
    public int updateBpmWorkbenchComponent(BpmWorkbenchComponent bpmWorkbenchComponent)
    {
        validateComponentName(bpmWorkbenchComponent);
        bpmWorkbenchComponentScopeMapper.deleteBpmWorkbenchComponentScopeByComponentId(bpmWorkbenchComponent.getComponentId());
        insertComponentScope(bpmWorkbenchComponent);
        return bpmWorkbenchComponentMapper.updateBpmWorkbenchComponent(bpmWorkbenchComponent);
    }

    @Override
    @Transactional
    public int deleteBpmWorkbenchComponentByIds(Long[] componentIds)
    {
        for (Long componentId : componentIds)
        {
            bpmWorkbenchComponentScopeMapper.deleteBpmWorkbenchComponentScopeByComponentId(componentId);
        }
        return bpmWorkbenchComponentMapper.deleteBpmWorkbenchComponentByIds(componentIds);
    }

    @Override
    @Transactional
    public int deleteBpmWorkbenchComponentById(Long componentId)
    {
        bpmWorkbenchComponentScopeMapper.deleteBpmWorkbenchComponentScopeByComponentId(componentId);
        return bpmWorkbenchComponentMapper.deleteBpmWorkbenchComponentById(componentId);
    }

    @Override
    public List<BpmWorkbenchComponent> selectUserAvailableComponents(Long userId)
    {
        return bpmWorkbenchComponentMapper.selectUserAvailableComponents(userId);
    }

    private void validateComponentName(BpmWorkbenchComponent component)
    {
        if (StringUtils.isEmpty(component.getComponentName()))
        {
            return;
        }
        BpmWorkbenchComponent exist = bpmWorkbenchComponentMapper.selectBpmWorkbenchComponentByName(component.getComponentName());
        if (exist != null && !exist.getComponentId().equals(component.getComponentId()))
        {
            throw new ServiceException("组件英文名已存在：" + component.getComponentName());
        }
    }

    private void insertComponentScope(BpmWorkbenchComponent component)
    {
        @SuppressWarnings("unchecked")
        List<BpmWorkbenchComponentScope> scopeList = (List<BpmWorkbenchComponentScope>) component.getParams().get("scopeList");
        if (scopeList == null || scopeList.isEmpty())
        {
            return;
        }
        for (BpmWorkbenchComponentScope scope : scopeList)
        {
            scope.setComponentId(component.getComponentId());
            scope.setCreateBy(component.getCreateBy());
            bpmWorkbenchComponentScopeMapper.insertBpmWorkbenchComponentScope(scope);
        }
    }
}
