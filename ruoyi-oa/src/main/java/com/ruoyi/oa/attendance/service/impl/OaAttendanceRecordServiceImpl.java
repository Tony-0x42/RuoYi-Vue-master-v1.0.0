package com.ruoyi.oa.attendance.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.attendance.domain.OaAttendanceRecord;
import com.ruoyi.oa.attendance.mapper.OaAttendanceRecordMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceRecordService;

/**
 * 打卡记录 服务层实现
 */
@Service
public class OaAttendanceRecordServiceImpl implements IOaAttendanceRecordService
{
    @Autowired
    private OaAttendanceRecordMapper recordMapper;

    @Override
    public OaAttendanceRecord selectById(Long id)
    {
        return recordMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceRecord> selectList(OaAttendanceRecord record)
    {
        return recordMapper.selectList(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceRecord record)
    {
        if (record.getUserId() == null)
        {
            record.setUserId(SecurityUtils.getUserId());
        }
        if (record.getCheckInTime() == null)
        {
            record.setCheckInTime(new Date());
        }
        if (record.getCheckInDate() == null)
        {
            record.setCheckInDate(record.getCheckInTime());
        }
        if (StringUtils.isEmpty(record.getType()))
        {
            record.setType("gps");
        }
        if (StringUtils.isEmpty(record.getStatus()))
        {
            record.setStatus("normal");
        }
        record.setCreateBy(SecurityUtils.getUsername());
        return recordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceRecord record)
    {
        if (record.getId() == null)
        {
            throw new ServiceException("打卡记录ID不能为空");
        }
        return recordMapper.update(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return recordMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return recordMapper.deleteByIds(ids);
    }

    @Override
    public Map<String, Object> statistics()
    {
        long total = recordMapper.countAll();
        long normal = recordMapper.countByStatus("normal");
        long late = recordMapper.countByStatus("late");
        long early = recordMapper.countByStatus("early");
        long absent = recordMapper.countByStatus("absent");

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("normal", normal);
        stats.put("late", late);
        stats.put("early", early);
        stats.put("absent", absent);
        return stats;
    }
}
