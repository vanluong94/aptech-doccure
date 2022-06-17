jQuery(function($){

    let deletedTimeSlots = [];
    
    $.ajax({
        url: "/ajax/timeSlots/getAll",
        method: "get",
        success(resp) {
            if (resp.isSuccess) {
                if (resp.data.timeSlots) {
                    renderAllTimeSlots(resp.data.timeSlots);
                }
            } else {
                alert('Failed to load time slots');
            }
        }
    });

    const renderAllTimeSlots = (respTimeSlots) => {

        // group by weekday
        let timeSlots = respTimeSlots.reduce((timeSlots, _timeSlot) => {
            if (!timeSlots[_timeSlot.weekday]) {
                timeSlots[_timeSlot.weekday] = [];
            }
            timeSlots[_timeSlot.weekday].push(_timeSlot);
            return timeSlots;
        }, {});
        
        $('form.time-slots-form').each((i,form) => {

            let $form = $(form);
            let weekday = $form.data('weekday');

            if (typeof weekday != 'undefined' && timeSlots[weekday]) {
                renderFormTimeSlots($form, timeSlots[weekday]);
            }

            $form.find('.add-hours').get(0).onclick = () => {
                renderFormTimeSlots($form, [
                    {
                        status: 1,
                        timeStart: '',
                        timeEnd: '',
                    }
                ], true);
            }
        })

    }

    $('form.time-slots-form button.submit-btn').on('click', function(e){

        e.preventDefault();
        
        let postData = {
            update: $('form.time-slots-form .hours-cont').map((i, el) => getTimeSlotData($(el))).toArray(),
            delete: deletedTimeSlots
        };
        
        let $wrapper = $('.card.schedule-widget');
        let $alerts = $('#alerts');

        $.ajax({
            url: `/ajax/timeSlots/saveAll`,
            method: 'post',
            data: JSON.stringify(postData),
            contentType: 'application/json',
            headers: {
                ...getAjaxCsrfTokenHeader()
            },
            beforeSend() {
                addLoadingOverlay($wrapper);
                $alerts.empty();
            },
            success(resp) {
                if (resp.isSuccess) {
                    renderAllTimeSlots(resp.data.timeSlots);
                    $alerts.append(`<div class="alert alert-success">Update successfully!</div>`);
                    deletedTimeSlots = [];
                } else {
                    $alerts.append(`<div class="alert alert-danger">${resp.message}</div>`);
                }
            },
            complete(xhr) {
                removeLoadingOverlay($wrapper);

                if (xhr.responseJSON && !xhr.responseJSON.isSuccess) {
                    alert(xhr.responseJSON.message);
                    $alerts.append(`<div class="alert alert-danger">${resp.message}</div>`);
                } else if (xhr.status != 200) {
                    $alerts.append(`<div class="alert alert-danger">Something wrong happened, please try again later</div>`);
                }
            }
        });

    });

    const renderFormTimeSlots = ($form, timeSlots, isAppending) => {
        let $hoursInfo = $form.find('.hours-info');
        if ($hoursInfo.length) {
            let output = '';
            for (let i in timeSlots) {
                let timeSlot = timeSlots[i];
                output += `
                <div class="row hours-cont py-2 my-1 border-bottom">
                    <div class="mr-2 col-md-2">
                        <div class="form-group">
                            <label>Available</label>
                            <div class="toggle-btn">
                                <input type="checkbox" class="checkbox" name="status" value="1" ${timeSlot.status == 1 ? 'checked' : ''}>
                                <div class="knobs">
                                    <span>YES</span>
                                </div>
                                <div class="layer"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label>Start Time</label>
                            <input type="time" name="timeStart" id="" class="form-control" value="${timeSlot.timeStart}">
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label>End Time</label>
                            <input type="time" name="timeEnd" id="" class="form-control" value="${timeSlot.timeEnd}">
                        </div>
                    </div>
                    <div class="col-md-1">
                        <label>&nbsp;</label>
                        <a href="#" class="btn btn-danger trash">
                            <i class="far fa-trash-alt"></i>
                        </a>
                    </div>
                </div>
                `;
            }

            if (isAppending) {
                $hoursInfo.append(output);
            } else {
                $hoursInfo.html(output)
            }

            $hoursInfo.find('.trash').on('click', function (e) {
                e.preventDefault();
                let $timeSlot = $(this).closest('.hours-cont');
                deletedTimeSlots.push(getTimeSlotData($timeSlot))
                $(this).closest('.hours-cont').remove();
                return false;
            });
        }
    }

    const getTimeSlotData = ($el) => {
        return {
            weekday  : $el.parents('form').data('weekday'),
            status   : $el.find('input[name="status"]').is(':checked') ? 1: 0,
            timeStart: $el.find('input[name="timeStart"]').val(),
            timeEnd  : $el.find('input[name="timeEnd"]').val()
        };
    }
})