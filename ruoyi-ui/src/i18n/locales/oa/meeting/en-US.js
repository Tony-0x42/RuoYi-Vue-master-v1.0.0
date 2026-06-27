export default {
  route: {
    meeting: 'Meeting Management',
    meetingRoom: 'Meeting Room Management'
  },
  oa: {
    meeting: {
      title: 'Meeting Management',
      roomTitle: 'Meeting Room Management',
      search: {
        title: 'Meeting Subject',
        titlePlaceholder: 'Please enter meeting subject',
        room: 'Meeting Room',
        status: 'Status',
        dateRange: 'Time Range'
      },
      status: {
        pending: 'Pending',
        ongoing: 'Ongoing',
        finished: 'Finished',
        cancelled: 'Cancelled'
      },
      room: {
        code: 'Room Code',
        name: 'Room Name',
        location: 'Location',
        capacity: 'Capacity',
        devices: 'Devices',
        status: 'Status',
        picUrl: 'Picture',
        codePlaceholder: 'Please enter room code',
        namePlaceholder: 'Please enter room name',
        locationPlaceholder: 'Please enter location',
        devicesPlaceholder: 'Please enter device description',
        enable: 'Enabled',
        disable: 'Disabled'
      },
      form: {
        title: 'Meeting Subject',
        titleRequired: 'Meeting subject is required',
        timeRange: 'Meeting Time',
        timeRequired: 'Please select meeting time',
        roomId: 'Meeting Room',
        roomIdRequired: 'Please select a meeting room',
        content: 'Content',
        attendees: 'Attendees',
        organizer: 'Organizer',
        remark: 'Remark'
      },
      add: 'Add Meeting',
      edit: 'Edit Meeting',
      detail: 'Meeting Detail',
      cancel: 'Cancel Meeting',
      cancelSuccess: 'Cancelled successfully',
      signIn: 'Sign In',
      signInSuccess: 'Signed in successfully',
      minutes: 'Meeting Minutes',
      saveMinutes: 'Save Minutes',
      minutesSuccess: 'Minutes saved successfully',
      occupancy: 'Occupancy View',
      deleteConfirm: 'Are you sure to delete meeting "{ids}"?',
      deleteRoomConfirm: 'Are you sure to delete meeting room "{ids}"?',
      required: {
        code: 'Room code is required',
        name: 'Room name is required',
        capacity: 'Capacity is required'
      }
    }
  }
}
