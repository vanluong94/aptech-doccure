
!function($) {
    "use strict";

    var CalendarApp = function() {
        this.$calendar = $('#calendar'),
        this.$calendarObj = null
    };

    /* on click on event */
    CalendarApp.prototype.onEventClick =  function (calEvent, jsEvent, view) {
        if (typeof appointmentUtils != undefined) {
            appointmentUtils.view(calEvent.id);
        }
    },

    /* Initializing */
    CalendarApp.prototype.init = function() {
        
        /*  Initialize the calendar  */
        var $this = this;
        $this.$calendarObj = $this.$calendar.fullCalendar({
            slotDuration: '00:15:00', /* If we want to split day time each 15minutes */
            minTime: '08:00:00',
            maxTime: '19:00:00',  
            timeFormat: 'HH:mm',
            defaultView: 'month',  
            handleWindowResize: true,   
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            },
            events: '/ajax/appointments/myCalendar',
            editable: false,
            disableDragging: true,
            // eventLimit: true, // allow "more" link when too many events
            eventClick: function(calEvent, jsEvent, view) { $this.onEventClick(calEvent, jsEvent, view); }
        });
    },

    //init CalendarApp
    $.CalendarApp = new CalendarApp, $.CalendarApp.Constructor = CalendarApp

}(window.jQuery),

//initializing CalendarApp
function($) {
    "use strict";
    $.CalendarApp.init()
}(window.jQuery);