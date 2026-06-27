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
    public int receive(Long id, Long userId, String userName)
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

        OaAssetReceive receive = new OaAssetReceive();
        receive.setAssetId(id);
        receive.setUserId(userId);
        receive.setUserName(userName);
        receive.setStatus(0);
        receive.setCreateBy(SecurityUtils.getUsername());
        receiveMapper.insert(receive);

        BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_receive", "asset_receive:" + receive.getId(), userId);
        receive.setProcessInstanceId(instance.getId());
        receive.setUpdateBy(SecurityUtils.getUsername());
        return receiveMapper.update(receive);
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
    public int transfer(Long id, Long fromUserId, String fromUserName, Long toUserId, String toUserName)
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

        OaAssetTransfer transfer = new OaAssetTransfer();
        transfer.setAssetId(id);
        transfer.setFromUserId(fromUserId);
        transfer.setFromUserName(fromUserName);
        transfer.setToUserId(toUserId);
        transfer.setToUserName(toUserName);
        transfer.setStatus(0);
        transfer.setCreateBy(SecurityUtils.getUsername());
        transferMapper.insert(transfer);

        BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_transfer", "asset_transfer:" + transfer.getId(), fromUserId);
        transfer.setProcessInstanceId(instance.getId());
        transfer.setUpdateBy(SecurityUtils.getUsername());
        return transferMapper.update(transfer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int repair(Long id, String reason, java.math.BigDecimal cost, String vendor)
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

        OaAssetRepair repair = new OaAssetRepair();
        repair.setAssetId(id);
        repair.setReason(reason);
        repair.setCost(cost);
        repair.setVendor(vendor);
        repair.setStatus(0);
        repair.setCreateBy(SecurityUtils.getUsername());
        repairMapper.insert(repair);

        BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_repair", "asset_repair:" + repair.getId(), SecurityUtils.getUserId());
        repair.setProcessInstanceId(instance.getId());
        repair.setUpdateBy(SecurityUtils.getUsername());
        return repairMapper.update(repair);
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
    public int scrap(Long id, String reason, String disposalMethod)
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

        OaAssetScrap scrap = new OaAssetScrap();
        scrap.setAssetId(id);
        scrap.setReason(reason);
        scrap.setDisposalMethod(disposalMethod);
        scrap.setStatus(0);
        scrap.setCreateBy(SecurityUtils.getUsername());
        scrapMapper.insert(scrap);

        BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_scrap", "asset_scrap:" + scrap.getId(), SecurityUtils.getUserId());
        scrap.setProcessInstanceId(instance.getId());
        scrap.setUpdateBy(SecurityUtils.getUsername());
        return scrapMapper.update(scrap);
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
