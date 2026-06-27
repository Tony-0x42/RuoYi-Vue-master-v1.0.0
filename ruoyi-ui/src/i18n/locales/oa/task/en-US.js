export default {
  route: {
    task: 'Task Collaboration'
  },
  oa: {
    task: {
      title: 'Task Title',
      description: 'Description',
      owner: 'Owner',
      participant: 'Participants',
      priorityLabel: 'Priority',
      statusLabel: 'Status',
      progress: 'Progress',
      startTime: 'Start Time',
      endTime: 'Due Time',
      subtask: 'Subtask',
      comment: 'Comment',
      board: 'Board',
      list: 'List',
      statistics: 'Statistics',
      detail: 'Task Detail',
      addTask: 'Add Task',
      editTask: 'Edit Task',
      addSubtask: 'Add Subtask',
      view: 'View',
      status: {
        pending: 'Pending',
        inProgress: 'In Progress',
        completed: 'Completed',
        cancelled: 'Cancelled'
      },
      priority: {
        urgent: 'Urgent',
        high: 'High',
        medium: 'Medium',
        low: 'Low'
      },
      placeholder: {
        title: 'Please enter task title',
        description: 'Please enter description',
        owner: 'Please select owner',
        participant: 'Please select participants',
        priority: 'Please select priority',
        status: 'Please select status',
        timeRange: 'Please select time range',
        subtaskTitle: 'Please enter subtask title'
      },
      required: {
        title: 'Task title is required'
      },
      confirm: {
        deleteTask: 'Are you sure to delete task(s) "{ids}"?'
      }
    }
  }
}
