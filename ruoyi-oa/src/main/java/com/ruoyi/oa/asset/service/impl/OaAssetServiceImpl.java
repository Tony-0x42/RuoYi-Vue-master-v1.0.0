package com.ruoyi.oa.asset.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.oa.asset.domain.OaAsset;
import com.ruoyi.oa.asset.domain.OaAssetReceive;
import com.ruoyi.oa.asset.domain.OaAssetRepair;
import com.ruoyi.oa.asset.domain.OaAssetReturn;
import com.ruoyi.oa.asset.domain.OaAssetScrap;
import com.ruoyi.oa.asset.domain.OaAssetTransfer;
import com.ruoyi.oa.asset.mapper.OaAssetMapper;
import com.ruoyi.oa.asset.mapper.OaAssetReceiveMapper;
import com.ruoyi.oa.asset.mapper.OaAssetRepairMapper;
import com.ruoyi.oa.asset.mapper.OaAssetReturnMapper;
import com.ruoyi.oa.asset.mapper.OaAssetScrapMapper;
import com.ruoyi.oa.asset.mapper.OaAssetTransferMapper;
import com.ruoyi.oa.common.OaBpmHelper;
import com.ruoyi.oa.asset.service.IOaAssetService;

/**
 * 资产台账 服务层实现
 */
@Service
public class OaAssetServiceImpl implements IOaAssetService
{
    @Autowired
    private OaAssetMapper assetMapper;

    @Autowired
    private OaAssetReceiveMapper receiveMapper;

    @Autowired
    private OaAssetReturnMapper returnMapper;

    @Autowired
    private OaAssetTransferMapper transferMapper;

    @Autowired
    private OaAssetRepairMapper repairMapper;

    @Autowired
    private OaAssetScrapMapper scrapMapper;

    @Autowired
    private OaBpmHelper bpmHelper;

    @Override
    public OaAsset selectById(Long id)
    {
        return assetMapper.selectById(id);
    }

    @Override
    public OaAsset selectByCode(String code)
    {
        return assetMapper.selectByCode(code);
    }

    @Override
    public List<OaAsset> selectList(OaAsset asset)
    {
        return assetMapper.selectList(asset);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAsset asset)
    {
        if (asset.getStatus() == null)
        {
            asset.setStatus(0);
        }
        if (asset.getValue() == null)
        {
            asset.setValue(new java.math.BigDecimal("0"));
        }
        asset.setCreateBy(SecurityUtils.getUsername());
        int rows = assetMapper.insert(asset);
        if (asset.getCode() == null || asset.getCode().trim().isEmpty())
        {
            asset.setCode("ASSET" + String.format("%06d", asset.getId()));
            assetMapper.update(asset);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAsset asset)
    {
        asset.setUpdateBy(SecurityUtils.getUsername());
        return assetMapper.update(asset);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return assetMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return assetMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long receive(Long id, Map<String, Object> params)
    {
        Object recordIdObj = params != null ? params.get("id") : null;
        Long recordId = toLong(recordIdObj);
        Object approver = params != null ? params.get("approvalAssignee") : null;

        OaAssetReceive receive;
        if (recordId != null)
        {
            receive = receiveMapper.selectById(recordId);
            if (receive == null)
            {
                throw new ServiceException("领用记录不存在");
            }
            if (receive.getProcessInstanceId() != null && !receive.getProcessInstanceId().isEmpty())
            {
                throw new ServiceException("领用申请已提交");
            }
        }
        else
        {
            OaAsset asset = assetMapper.selectById(id);
            if (asset == null)
            {
                throw new ServiceException("资产不存在");
            }
            if (!Integer.valueOf(0).equals(asset.getStatus()))
            {
                throw new ServiceException("仅闲置资产可领用");
            }
            Long userId = toLong(params != null ? params.get("userId") : null);
            String userName = toStringVal(params != null ? params.get("userName") : null);
            receive = new OaAssetReceive();
            receive.setAssetId(id);
            receive.setUserId(userId);
            receive.setUserName(userName);
            receive.setRemark(toStringVal(params != null ? params.get("remark") : null));
            receive.setStatus(0);
            receive.setCreateBy(SecurityUtils.getUsername());
            receiveMapper.insert(receive);
        }

        if (approver != null)
        {
            Long starter = receive.getUserId() != null ? receive.getUserId() : SecurityUtils.getUserId();
            BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_receive", "asset_receive:" + receive.getId(), starter, approver);
            receive.setProcessInstanceId(instance.getId());
            receive.setStatus(0);
            receive.setUpdateBy(SecurityUtils.getUsername());
            receiveMapper.update(receive);
        }
        return receive.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int returnAsset(Long id, Long userId, String userName)
    {
        OaAsset asset = assetMapper.selectById(id);
        if (asset == null)
        {
            throw new ServiceException("资产不存在");
        }
        if (!Integer.valueOf(1).equals(asset.getStatus()))
        {
            throw new ServiceException("仅在用资产可归还");
        }
        OaAsset update = new OaAsset();
        update.setId(id);
        update.setStatus(0);
        update.setUserId(null);
        update.setUserName(null);
        update.setUpdateBy(SecurityUtils.getUsername());
        assetMapper.update(update);

        OaAssetReturn record = new OaAssetReturn();
        record.setAssetId(id);
        record.setUserId(userId);
        record.setUserName(userName);
        record.setReturnTime(new Date());
        record.setStatus(0);
        record.setCreateBy(SecurityUtils.getUsername());
        returnMapper.insert(record);

        OaAssetReceive query = new OaAssetReceive();
        query.setAssetId(id);
        query.setStatus(1);
        List<OaAssetReceive> receives = receiveMapper.selectList(query);
        if (receives != null && !receives.isEmpty())
        {
            for (OaAssetReceive r : receives)
            {
                if (r.getReturnTime() == null)
                {
                    r.setReturnTime(new Date());
                    r.setUpdateBy(SecurityUtils.getUsername());
                    receiveMapper.update(r);
                }
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long transfer(Long id, Map<String, Object> params)
    {
        Object recordIdObj = params != null ? params.get("id") : null;
        Long recordId = toLong(recordIdObj);
        Object approver = params != null ? params.get("approvalAssignee") : null;

        OaAssetTransfer transfer;
        if (recordId != null)
        {
            transfer = transferMapper.selectById(recordId);
            if (transfer == null)
            {
                throw new ServiceException("调拨记录不存在");
            }
            if (transfer.getProcessInstanceId() != null && !transfer.getProcessInstanceId().isEmpty())
            {
                throw new ServiceException("调拨申请已提交");
            }
        }
        else
        {
            OaAsset asset = assetMapper.selectById(id);
            if (asset == null)
            {
                throw new ServiceException("资产不存在");
            }
            if (!Integer.valueOf(1).equals(asset.getStatus()))
            {
                throw new ServiceException("仅在用资产可调拨");
            }
            transfer = new OaAssetTransfer();
            transfer.setAssetId(id);
            transfer.setFromUserId(toLong(params != null ? params.get("fromUserId") : null));
            transfer.setFromUserName(toStringVal(params != null ? params.get("fromUserName") : null));
            transfer.setToUserId(toLong(params != null ? params.get("toUserId") : null));
            transfer.setToUserName(toStringVal(params != null ? params.get("toUserName") : null));
            transfer.setRemark(toStringVal(params != null ? params.get("remark") : null));
            transfer.setStatus(0);
            transfer.setCreateBy(SecurityUtils.getUsername());
            transferMapper.insert(transfer);
        }

        if (approver != null)
        {
            Long starter = transfer.getFromUserId() != null ? transfer.getFromUserId() : SecurityUtils.getUserId();
            BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_transfer", "asset_transfer:" + transfer.getId(), starter, approver);
            transfer.setProcessInstanceId(instance.getId());
            transfer.setStatus(0);
            transfer.setUpdateBy(SecurityUtils.getUsername());
            transferMapper.update(transfer);
        }
        return transfer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long repair(Long id, Map<String, Object> params)
    {
        Object recordIdObj = params != null ? params.get("id") : null;
        Long recordId = toLong(recordIdObj);
        Object approver = params != null ? params.get("approvalAssignee") : null;

        OaAssetRepair repair;
        if (recordId != null)
        {
            repair = repairMapper.selectById(recordId);
            if (repair == null)
            {
                throw new ServiceException("维修记录不存在");
            }
            if (repair.getProcessInstanceId() != null && !repair.getProcessInstanceId().isEmpty())
            {
                throw new ServiceException("维修申请已提交");
            }
        }
        else
        {
            OaAsset asset = assetMapper.selectById(id);
            if (asset == null)
            {
                throw new ServiceException("资产不存在");
            }
            if (Integer.valueOf(3).equals(asset.getStatus()))
            {
                throw new ServiceException("已报废资产不可维修");
            }
            repair = new OaAssetRepair();
            repair.setAssetId(id);
            repair.setReason(toStringVal(params != null ? params.get("reason") : null));
            repair.setCost(toBigDecimal(params != null ? params.get("cost") : null));
            repair.setVendor(toStringVal(params != null ? params.get("vendor") : null));
            repair.setRemark(toStringVal(params != null ? params.get("remark") : null));
            repair.setStatus(0);
            repair.setCreateBy(SecurityUtils.getUsername());
            repairMapper.insert(repair);
        }

        if (approver != null)
        {
            Long starter = SecurityUtils.getUserId();
            BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_repair", "asset_repair:" + repair.getId(), starter, approver);
            repair.setProcessInstanceId(instance.getId());
            repair.setStatus(0);
            repair.setUpdateBy(SecurityUtils.getUsername());
            repairMapper.update(repair);
        }
        return repair.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int finishRepair(Long repairId)
    {
        OaAssetRepair repair = repairMapper.selectById(repairId);
        if (repair == null)
        {
            throw new ServiceException("维修记录不存在");
        }
        if (!Integer.valueOf(1).equals(repair.getStatus()))
        {
            throw new ServiceException("仅维修中的记录可完成");
        }
        repair.setStatus(3);
        repair.setUpdateBy(SecurityUtils.getUsername());
        repairMapper.update(repair);

        OaAsset update = new OaAsset();
        update.setId(repair.getAssetId());
        update.setStatus(0);
        update.setUpdateBy(SecurityUtils.getUsername());
        return assetMapper.update(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long scrap(Long id, Map<String, Object> params)
    {
        Object recordIdObj = params != null ? params.get("id") : null;
        Long recordId = toLong(recordIdObj);
        Object approver = params != null ? params.get("approvalAssignee") : null;

        OaAssetScrap scrap;
        if (recordId != null)
        {
            scrap = scrapMapper.selectById(recordId);
            if (scrap == null)
            {
                throw new ServiceException("报废记录不存在");
            }
            if (scrap.getProcessInstanceId() != null && !scrap.getProcessInstanceId().isEmpty())
            {
                throw new ServiceException("报废申请已提交");
            }
        }
        else
        {
            OaAsset asset = assetMapper.selectById(id);
            if (asset == null)
            {
                throw new ServiceException("资产不存在");
            }
            if (Integer.valueOf(3).equals(asset.getStatus()))
            {
                throw new ServiceException("资产已报废");
            }
            scrap = new OaAssetScrap();
            scrap.setAssetId(id);
            scrap.setReason(toStringVal(params != null ? params.get("reason") : null));
            scrap.setDisposalMethod(toStringVal(params != null ? params.get("disposalMethod") : null));
            scrap.setRemark(toStringVal(params != null ? params.get("remark") : null));
            scrap.setStatus(0);
            scrap.setCreateBy(SecurityUtils.getUsername());
            scrapMapper.insert(scrap);
        }

        if (approver != null)
        {
            Long starter = SecurityUtils.getUserId();
            BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_scrap", "asset_scrap:" + scrap.getId(), starter, approver);
            scrap.setProcessInstanceId(instance.getId());
            scrap.setStatus(0);
            scrap.setUpdateBy(SecurityUtils.getUsername());
            scrapMapper.update(scrap);
        }
        return scrap.getId();
    }

    private Long toLong(Object obj)
    {
        if (obj == null)
        {
            return null;
        }
        if (obj instanceof Number)
        {
            return ((Number) obj).longValue();
        }
        return Long.valueOf(obj.toString());
    }

    private String toStringVal(Object obj)
    {
        return obj == null ? null : obj.toString();
    }

    private java.math.BigDecimal toBigDecimal(Object obj)
    {
        if (obj == null)
        {
            return null;
        }
        if (obj instanceof java.math.BigDecimal)
        {
            return (java.math.BigDecimal) obj;
        }
        return new java.math.BigDecimal(obj.toString());
    }

    @Override
    public Map<String, Object> statistics()
    {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", assetMapper.countAll());
        stats.put("idle", assetMapper.countByStatus(0));
        stats.put("inUse", assetMapper.countByStatus(1));
        stats.put("repairing", assetMapper.countByStatus(2));
        stats.put("scrapped", assetMapper.countByStatus(3));
        stats.put("totalValue", assetMapper.sumValue());
        stats.put("categoryCounts", assetMapper.countByCategory());
        return stats;
    }
}
