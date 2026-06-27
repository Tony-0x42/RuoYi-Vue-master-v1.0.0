package com.ruoyi.oa.task.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.task.domain.OaTask;
import com.ruoyi.oa.task.domain.OaTaskComment;
import com.ruoyi.oa.task.domain.OaTaskMember;
import com.ruoyi.oa.task.domain.OaTaskSubtask;
import com.ruoyi.oa.task.mapper.OaTaskCommentMapper;
import com.ruoyi.oa.task.mapper.OaTaskMapper;
import com.ruoyi.oa.task.mapper.OaTaskMemberMapper;
import com.ruoyi.oa.task.mapper.OaTaskSubtaskMapper;
import com.ruoyi.oa.task.service.IOaTaskService;

/**
 * 任务 服务层实现
 */
@Service
public class OaTaskServiceImpl implements IOaTaskService
{
    @Autowired
    private OaTaskMapper taskMapper;

    @Autowired
    private OaTaskMemberMapper memberMapper;

    @Autowired
    private OaTaskSubtaskMapper subtaskMapper;

    @Autowired
    private OaTaskCommentMapper commentMapper;

    @Override
    public OaTask selectById(Long id)
    {
        OaTask task = taskMapper.selectById(id);
        if (task != null)
        {
            task.setMembers(memberMapper.selectByTaskId(id));
            task.setSubtasks(subtaskMapper.selectByParentId(id));
            task.setComments(commentMapper.selectByTaskId(id));
        }
        return task;
    }

    @Override
    public List<OaTask> selectList(OaTask task)
    {
        return taskMapper.selectList(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaTask task)
    {
        if (StringUtils.isEmpty(task.getTitle()))
        {
            throw new ServiceException("任务标题不能为空");
        }
        if (task.getOwnerId() == null)
        {
            task.setOwnerId(SecurityUtils.getUserId());
        }
        if (task.getStatus() == null)
        {
            task.setStatus(0);
        }
        if (task.getPriority() == null)
        {
            task.setPriority("medium");
        }
        if (task.getProgress() == null)
        {
            task.setProgress(0);
        }
        task.setCreateBy(SecurityUtils.getUsername());
        int rows = taskMapper.insert(task);
        saveMembers(task);
        saveSubtasks(task);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaTask task)
    {
        if (task.getId() == null)
        {
            throw new ServiceException("任务ID不能为空");
        }
        if (StringUtils.isEmpty(task.getTitle()))
        {
            throw new ServiceException("任务标题不能为空");
        }
        task.setUpdateBy(SecurityUtils.getUsername());
        int rows = taskMapper.update(task);
        if (rows > 0)
        {
            memberMapper.deleteByTaskId(task.getId());
            saveMembers(task);
            if (task.getSubtasks() != null)
            {
                subtaskMapper.deleteByParentId(task.getId());
                saveSubtasks(task);
            }
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        memberMapper.deleteByTaskId(id);
        subtaskMapper.deleteByParentId(id);
        commentMapper.deleteByTaskId(id);
        return taskMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            memberMapper.deleteByTaskId(id);
            subtaskMapper.deleteByParentId(id);
            commentMapper.deleteByTaskId(id);
        }
        return taskMapper.deleteByIds(ids);
    }

    @Override
    public List<OaTask> selectBoardList(OaTask task)
    {
        return taskMapper.selectList(task);
    }

    @Override
    public List<OaTask> selectGanttList(OaTask task)
    {
        return taskMapper.selectList(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addComment(Long taskId, Long userId, String content)
    {
        if (StringUtils.isEmpty(content))
        {
            throw new ServiceException("评论内容不能为空");
        }
        OaTaskComment comment = new OaTaskComment();
        comment.setTaskId(taskId);
        comment.setUserId(userId);
        comment.setContent(content);
        return commentMapper.insert(comment);
    }

    @Override
    public List<OaTaskComment> selectComments(Long taskId)
    {
        return commentMapper.selectByTaskId(taskId);
    }

    @Override
    public Map<String, Object> statistics()
    {
        long total = taskMapper.countAll();
        long pending = taskMapper.countByStatus(0);
        long inProgress = taskMapper.countByStatus(1);
        long completed = taskMapper.countByStatus(2);
        long cancelled = taskMapper.countByStatus(3);

        long overdue = 0L;
        List<OaTask> list = taskMapper.selectList(new OaTask());
        Date now = new Date();
        for (OaTask task : list)
        {
            if (task.getStatus() != null && task.getStatus() != 2
                    && task.getEndTime() != null && task.getEndTime().before(now))
            {
                overdue++;
            }
        }

        double completionRate = total == 0 ? 0 : Math.round(completed * 100.0 / total);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("pending", pending);
        stats.put("inProgress", inProgress);
        stats.put("completed", completed);
        stats.put("cancelled", cancelled);
        stats.put("overdue", overdue);
        stats.put("completionRate", completionRate);
        return stats;
    }

    private void saveMembers(OaTask task)
    {
        List<OaTaskMember> members = new ArrayList<>();
        if (task.getOwnerId() != null)
        {
            OaTaskMember owner = new OaTaskMember();
            owner.setTaskId(task.getId());
            owner.setUserId(task.getOwnerId());
            owner.setRole("owner");
            owner.setTenantId(task.getTenantId());
            members.add(owner);
        }
        if (!CollectionUtils.isEmpty(task.getParticipantIds()))
        {
            for (Long userId : task.getParticipantIds())
            {
                if (userId == null || (task.getOwnerId() != null && userId.equals(task.getOwnerId())))
                {
                    continue;
                }
                OaTaskMember participant = new OaTaskMember();
                participant.setTaskId(task.getId());
                participant.setUserId(userId);
                participant.setRole("participant");
                participant.setTenantId(task.getTenantId());
                members.add(participant);
            }
        }
        if (!members.isEmpty())
        {
            memberMapper.insertBatch(members);
        }
    }

    private void saveSubtasks(OaTask task)
    {
        if (CollectionUtils.isEmpty(task.getSubtasks()))
        {
            return;
        }
        List<OaTaskSubtask> list = new ArrayList<>();
        for (OaTaskSubtask subtask : task.getSubtasks())
        {
            if (StringUtils.isEmpty(subtask.getTitle()))
            {
                continue;
            }
            subtask.setParentId(task.getId());
            subtask.setTenantId(task.getTenantId());
            if (subtask.getStatus() == null)
            {
                subtask.setStatus(0);
            }
            list.add(subtask);
        }
        if (!list.isEmpty())
        {
            subtaskMapper.insertBatch(list);
        }
    }
}
