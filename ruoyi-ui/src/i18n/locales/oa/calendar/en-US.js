export default {
  route: {
    calendar: 'Calendar'
  },
  calendar: {
    title: 'Calendar Management',
    search: {
      title: 'Event Title',
      titlePlaceholder: 'Please enter event title',
      type: 'Event Type',
      dateRange: 'Date Range'
    },
    type: {
      personal: 'Personal',
      meeting: 'Meeting',
      leave: 'Leave',
      trip: 'Trip'
    },
    allDay: 'All Day',
    startTime: 'Start Time',
    endTime: 'End Time',
    location: 'Location',
    attendees: 'Attendees',
    source: 'Source',
    status: 'Status',
    add: 'Add Event',
    edit: 'Edit Event',
    detail: 'Event Detail',
    deleteConfirm: 'Are you sure to delete event(s) with ID "{eventIds}"?',
    form: {
      title: 'Event Title',
      titleRequired: 'Event title is required',
      typeRequired: 'Please select event type',
      timeRequired: 'Please select start and end time',
      locationPlaceholder: 'Please enter location',
      remark: 'Remark'
    }
  }
}
