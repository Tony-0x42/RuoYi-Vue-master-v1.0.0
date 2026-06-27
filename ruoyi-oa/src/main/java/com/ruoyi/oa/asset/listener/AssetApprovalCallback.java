package com.ruoyi.oa.asset.listener;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.asset.domain.OaAsset;
import com.ruoyi.oa.asset.domain.OaAssetReceive;
import com.ruoyi.oa.asset.domain.OaAssetRepair;
import com.ruoyi.oa.asset.domain.OaAssetScrap;
import com.ruoyi.oa.asset.domain.OaAssetTransfer;
import com.ruoyi.oa.asset.mapper.OaAssetMapper;
import com.ruoyi.oa.asset.mapper.OaAssetReceiveMapper;
import com.ruoyi.oa.asset.mapper.OaAssetRepairMapper;
import com.ruoyi.oa.asset.mapper.OaAssetScrapMapper;
import com.ruoyi.oa.asset.mapper.OaAssetTransferMapper;

/**
 * 资产审批完成回调
 */
@Service
public class AssetApprovalCallback
{
    @Autowired
    private OaAssetMapper assetMapper;

    @Autowired
    private OaAssetReceiveMapper receiveMapper;

    @Autowired
    private OaAssetTransferMapper transferMapper;

    @Autowired
    private OaAssetRepairMapper repairMapper;

    @Autowired
    private OaAssetScrapMapper scrapMapper;

    @Transactional(rollbackFor = Exception.class)
    public void onReceiveApproved(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("asset_receive:", ""));
        OaAssetReceive receive = receiveMapper.selectById(id);
        if (receive == null)
        {
            return;
        }
        if ("agree".equals(action))
        {
            receive.setStatus(1);
            receive.setReceiveTime(new Date());

            OaAsset asset = assetMapper.selectById(receive.getAssetId());
            if (asset != null)
            {
                asset.setStatus(1);
                asset.setUserId(receive.getUserId());
                asset.setUserName(receive.getUserName());
                asset.setUpdateBy(SecurityUtils.getUsername());
                assetMapper.update(asset);
            }
        }
        else
        {
            receive.setStatus(2);
        }
        receive.setUpdateBy(SecurityUtils.getUsername());
        receiveMapper.update(receive);
    }

    @Transactional(rollbackFor = Exception.class)
    public void onTransferApproved(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("asset_transfer:", ""));
        OaAssetTransfer transfer = transferMapper.selectById(id);
        if (transfer == null)
        {
            return;
        }
        if ("agree".equals(action))
        {
            transfer.setStatus(1);
            transfer.setTransferTime(new Date());

            OaAsset asset = assetMapper.selectById(transfer.getAssetId());
            if (asset != null)
            {
                asset.setUserId(transfer.getToUserId());
                asset.setUserName(transfer.getToUserName());
                asset.setUpdateBy(SecurityUtils.getUsername());
                assetMapper.update(asset);
            }
        }
        else
        {
            transfer.setStatus(2);
        }
        transfer.setUpdateBy(SecurityUtils.getUsername());
        transferMapper.update(transfer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void onRepairApproved(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("asset_repair:", ""));
        OaAssetRepair repair = repairMapper.selectById(id);
        if (repair == null)
        {
            return;
        }
        if ("agree".equals(action))
        {
            repair.setStatus(1);
            repair.setRepairTime(new Date());

            OaAsset asset = assetMapper.selectById(repair.getAssetId());
            if (asset != null)
            {
                asset.setStatus(2);
                asset.setUpdateBy(SecurityUtils.getUsername());
                assetMapper.update(asset);
            }
        }
        else
        {
            repair.setStatus(2);
        }
        repair.setUpdateBy(SecurityUtils.getUsername());
        repairMapper.update(repair);
    }

    @Transactional(rollbackFor = Exception.class)
    public void onScrapApproved(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("asset_scrap:", ""));
        OaAssetScrap scrap = scrapMapper.selectById(id);
        if (scrap == null)
        {
            return;
        }
        if ("agree".equals(action))
        {
            scrap.setStatus(1);
            scrap.setScrapTime(new Date());

            OaAsset asset = assetMapper.selectById(scrap.getAssetId());
            if (asset != null)
            {
                asset.setStatus(3);
                asset.setUserId(null);
                asset.setUserName(null);
                asset.setUpdateBy(SecurityUtils.getUsername());
                assetMapper.update(asset);
            }
        }
        else
        {
            scrap.setStatus(2);
        }
        scrap.setUpdateBy(SecurityUtils.getUsername());
        scrapMapper.update(scrap);
    }
}
