jQuery(function($){
    $.ajax({
        url: "/ajax/timeSlots/getAll",
        method: "get",
        success(resp) {
            if (resp.isSuccess) {

                // group by weekday
                let timeSlots = resp.data.timeSlots.reduce((timeSlots, _timeSlot) => {
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
                        renderTimeSlots($form, timeSlots[weekday]);
                    }

                    $form.on('submit', (formEvt) => {
                        formEvt.preventDefault();
                        // let formData = new FormData($form.get(0));

                        let postData = $form.find('.hours-cont').get().map((el) => {
                            let $el = $(el);
                            return {
                                status   : $el.find('input[name="status"]').is(':checked') ? 1: 0,
                                timeStart: $el.find('input[name="timeStart"]').val(),
                                timeEnd  : $el.find('input[name="timeEnd"]').val()
                            }
                        })

                        $.ajax({
                            url: `/ajax/timeSlots/save?weekday=${weekday}`,
                            method: 'post',
                            data: JSON.stringify(postData),
                            contentType: 'application/json',
                            headers: {
                                ...getAjaxCsrfTokenHeader()
                            },
                            beforeSend() {
                                addLoadingOverlay($form);
                            },
                            success(resp) {
                                if (resp.isSuccess) {
                                    if (resp.data.timeSlots) {
                                        renderTimeSlots($form, resp.data.timeSlots);
                                    }
                                } else {
                                    alert(resp.message);
                                }
                            },
                            complete(xhr) {
                                removeLoadingOverlay($form);

                                if (xhr.responseJSON && !xhr.responseJSON.isSuccess) {
                                    alert(xhr.responseJSON.message);
                                }
                            }
                        });
                    })

                    $form.find('.add-hours').on('click', () => {
                        renderTimeSlots($form, [
                            {
                                status: 1,
                                timeStart: '',
                                timeEnd: '',
                            }
                        ], true);
                    })
                })

            } else {
                alert('Failed to load time slots');
            }
        }
    });

    const renderTimeSlots = ($form, timeSlots, isAppending) => {
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
        }
    }
})