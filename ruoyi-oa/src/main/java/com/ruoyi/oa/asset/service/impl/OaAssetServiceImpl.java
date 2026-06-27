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
        OaAsset update = new OaAsset();
        update.setId(id);
        update.setStatus(1);
        update.setUserId(userId);
        update.setUserName(userName);
        update.setUpdateBy(SecurityUtils.getUsername());
        assetMapper.update(update);

        OaAssetReceive receive = new OaAssetReceive();
        receive.setAssetId(id);
        receive.setUserId(userId);
        receive.setUserName(userName);
        receive.setReceiveTime(new Date());
        receive.setStatus(1);
        receive.setProcessInstanceId("mock-proc-receive-" + id);
        receive.setCreateBy(SecurityUtils.getUsername());
        return receiveMapper.insert(receive);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int returnAsset(Long id, Long userId, String userName, Integer status)
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
        int newStatus = Integer.valueOf(1).equals(status) ? 2 : 0;
        OaAsset update = new OaAsset();
        update.setId(id);
        update.setStatus(newStatus);
        update.setUserId(null);
        update.setUserName(null);
        update.setUpdateBy(SecurityUtils.getUsername());
        assetMapper.update(update);

        OaAssetReturn record = new OaAssetReturn();
        record.setAssetId(id);
        record.setUserId(userId);
        record.setUserName(userName);
        record.setReturnTime(new Date());
        record.setStatus(status);
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
                r.setReturnTime(new Date());
                r.setStatus(2);
                receiveMapper.update(r);
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
        OaAsset update = new OaAsset();
        update.setId(id);
        update.setUserId(toUserId);
        update.setUserName(toUserName);
        update.setUpdateBy(SecurityUtils.getUsername());
        assetMapper.update(update);

        OaAssetTransfer transfer = new OaAssetTransfer();
        transfer.setAssetId(id);
        transfer.setFromUserId(fromUserId);
        transfer.setFromUserName(fromUserName);
        transfer.setToUserId(toUserId);
        transfer.setToUserName(toUserName);
        transfer.setTransferTime(new Date());
        transfer.setProcessInstanceId("mock-proc-transfer-" + id);
        transfer.setCreateBy(SecurityUtils.getUsername());
        return transferMapper.insert(transfer);
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
        OaAsset update = new OaAsset();
        update.setId(id);
        update.setStatus(2);
        update.setUpdateBy(SecurityUtils.getUsername());
        assetMapper.update(update);

        OaAssetRepair repair = new OaAssetRepair();
        repair.setAssetId(id);
        repair.setReason(reason);
        repair.setCost(cost);
        repair.setVendor(vendor);
        repair.setRepairTime(new Date());
        repair.setStatus(0);
        repair.setCreateBy(SecurityUtils.getUsername());
        return repairMapper.insert(repair);
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
        repair.setStatus(1);
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
        OaAsset update = new OaAsset();
        update.setId(id);
        update.setStatus(3);
        update.setUserId(null);
        update.setUserName(null);
        update.setUpdateBy(SecurityUtils.getUsername());
        assetMapper.update(update);

        OaAssetScrap scrap = new OaAssetScrap();
        scrap.setAssetId(id);
        scrap.setReason(reason);
        scrap.setDisposalMethod(disposalMethod);
        scrap.setScrapTime(new Date());
        scrap.setProcessInstanceId("mock-proc-scrap-" + id);
        scrap.setCreateBy(SecurityUtils.getUsername());
        return scrapMapper.insert(scrap);
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
