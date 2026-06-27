package com.ruoyi.oa.attendance.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.attendance.domain.OaAttendanceLeave;
import com.ruoyi.oa.attendance.domain.OaAttendanceMakeup;
import com.ruoyi.oa.attendance.domain.OaAttendanceOvertime;
import com.ruoyi.oa.attendance.domain.OaAttendanceTrip;
import com.ruoyi.oa.attendance.mapper.OaAttendanceLeaveMapper;
import com.ruoyi.oa.attendance.mapper.OaAttendanceMakeupMapper;
import com.ruoyi.oa.attendance.mapper.OaAttendanceOvertimeMapper;
import com.ruoyi.oa.attendance.mapper.OaAttendanceTripMapper;

/**
 * 考勤申请审批完成回调
 */
@Service
public class AttendanceApprovalCallback
{
    @Autowired
    private OaAttendanceLeaveMapper leaveMapper;

    @Autowired
    private OaAttendanceOvertimeMapper overtimeMapper;

    @Autowired
    private OaAttendanceTripMapper tripMapper;

    @Autowired
    private OaAttendanceMakeupMapper makeupMapper;

    @Transactional(rollbackFor = Exception.class)
    public void onLeaveCompleted(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("attendance_leave:", ""));
        OaAttendanceLeave leave = leaveMapper.selectById(id);
        if (leave == null)
        {
            return;
        }
        leave.setStatus("agree".equals(action) ? "agreed" : "rejected");
        leave.setUpdateBy(SecurityUtils.getUsername());
        leaveMapper.update(leave);
    }

    @Transactional(rollbackFor = Exception.class)
    public void onOvertimeCompleted(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("attendance_overtime:", ""));
        OaAttendanceOvertime overtime = overtimeMapper.selectById(id);
        if (overtime == null)
        {
            return;
        }
        overtime.setStatus("agree".equals(action) ? "agreed" : "rejected");
        overtime.setUpdateBy(SecurityUtils.getUsername());
        overtimeMapper.update(overtime);
    }

    @Transactional(rollbackFor = Exception.class)
    public void onTripCompleted(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("attendance_trip:", ""));
        OaAttendanceTrip trip = tripMapper.selectById(id);
        if (trip == null)
        {
            return;
        }
        trip.setStatus("agree".equals(action) ? "agreed" : "rejected");
        trip.setUpdateBy(SecurityUtils.getUsername());
        tripMapper.update(trip);
    }

    @Transactional(rollbackFor = Exception.class)
    public void onMakeupCompleted(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("attendance_makeup:", ""));
        OaAttendanceMakeup makeup = makeupMapper.selectById(id);
        if (makeup == null)
        {
            return;
        }
        makeup.setStatus("agree".equals(action) ? "agreed" : "rejected");
        makeup.setUpdateBy(SecurityUtils.getUsername());
        makeupMapper.update(makeup);
    }
}
