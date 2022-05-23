const appointmentUtils = {

    $appointmentsTable: $('#appointmentsTable'),
    $getTable() {
        return this.$appointmentsTable;
    },
    $getDataTable() {
        return this.$appointmentsTable.DataTable();
    },

    /**
     * Table data processor
     */
    parseDoctorTableData(resp) {
        return resp.data.map((row) => {
            return Object.keys(row)
            .filter((key) => ['id', 'doctor', 'bookingDate'].indexOf(key) == -1) // exclude these field in results
            .map((key) => {
    
                let output = row[key];
    
                if (key == 'patient') {
                    output = this.renderUserTd(row.patient);
                } else if (key == 'timeStart' || key == 'timeEnd') {
                    output = `<span class="text-info">${row[key]}</span>`;
                } else if (key == 'status') {
                    output = this.renderStatusPill(row.status);
                } else if (key == 'action') {
    
                    output = `<div class="btn btn-sm bg-info-light mr-1" data-apmt-action="view" data-appointment-id="${row.id}"><i class="far fa-eye"></i> View</div>`;
    
                    switch(row.status) {
                        case 0:
                            output += `<div class="btn btn-sm bg-success-light mr-1" data-apmt-action="confirm" data-appointment-id="${row.id}"><i class="fas fa-check"></i> Confirm</div>`;
                            output += `<div class="btn btn-sm bg-danger-light mr-1" data-apmt-action="cancel" data-appointment-id="${row.id}"><i class="fas fa-times"></i> Cancel</div>`;
                            break;
                    }
    
                }
    
                return output;
            })
        });
    },
    parsePatientTableData(resp) {
        return resp.data.map((row) => {
            return Object.keys(row)
            .filter((key) => ['id', 'patient'].indexOf(key) == -1) // exclude these field in results
            .map((key) => {
    
                let output = row[key];
    
                if (key == 'doctor') {
                    output = this.renderUserTd(row.doctor);
                } else if (key == 'timeStart' || key == 'timeEnd') {
                    output = `<span class="text-info">${row[key]}</span>`;
                } else if (key == 'status') {
                    output = this.renderStatusPill(row.status);
                } else if (key == 'action') {
    
                    output = `<div class="btn btn-sm bg-info-light mr-1" data-apmt-action="view" data-appointment-id="${row.id}"><i class="far fa-eye"></i> View</div>`;
    
                    switch(row.status) {
                        case 0:
                            output += `<div class="btn btn-sm bg-danger-light mr-1" data-apmt-action="cancel" data-appointment-id="${row.id}"><i class="fas fa-times"></i> Cancel</div>`;
                            break;
                    }
    
                }
    
                return output;
            })
        });
    },

    /**
     * Appointment actions
     */
    view(apmtId) {

        const $appointmentDetailModal = $('#appointmentDetail');
        const $appointmentDetailBody = $appointmentDetailModal.find('.modal-body');

        $appointmentDetailModal.modal('show');
    
        let $info = $appointmentDetailBody.find('.info-details');
        let $fields = $appointmentDetailBody.find('[data-field]');
    
        $.ajax({
            url: `/ajax/appointments/${apmtId}/get`,
            beforeSend() {
                addLoadingOverlay($info);
                $fields.empty();
            },
            success(resp) {
                if (resp.isSuccess) {
                    $fields.each((i,e) => {
                        $e = $(e);
                        $e.empty(); // just to make sure it's empty
    
                        let name = $e.data('field');
                        let value;
    
                        if (name == 'statusBadge') {
                            value = appointmentUtils.renderStatusBadege(resp.data.appointment.status);
                        } else if (name == 'logs') {
                            for (log of resp.data.appointment.logs) {
                                $e.append(
                                    ` <tr>
                                        <td>${log.createdDate}</td>
                                        <td>${appointmentUtils.renderLogContent(log.content)}</td>
                                    </tr>`
                                );
                            }
                        } else {
                            value = resp.data.appointment;
                            name.split('.').forEach(key => {
                                value = value[key];
                            });
                        }
    
                        $e.html(value);
                    })
                } else {
                    alert(resp.message);
                }
            },
            complete(status, xhr) {
                removeLoadingOverlay($info);
            }
        });
    },
    cancel(apmtId) {
        if (confirm(`Are you sure that you want to cancel this appointment?`)) {
            $.ajax({
                url: `/ajax/appointments/${apmtId}/cancel`,
                method: 'post',
                headers: {
                    ...getAjaxCsrfTokenHeader()
                },
                beforeSend() {
                    addLoadingOverlay(appointmentUtils.$getTable());
                },
                success(resp) {
                    if (resp.isSuccess) {
                        appointmentUtils.$getDataTable().ajax.reload();
                    } else {
                        alert(resp.message);
                    }
                },
                complete(status, xhr) {
                    removeLoadingOverlay(appointmentUtils.$getTable());
                }
            });
        }
    },
    confirm(apmtId) {
        if (confirm(`Are you sure that you want to confirm this appointment?`)) {
            $.ajax({
                url: `/ajax/appointments/${apmtId}/confirm`,
                method: 'post',
                headers: {
                    ...getAjaxCsrfTokenHeader()
                },
                beforeSend() {
                    addLoadingOverlay(appointmentUtils.$getTable());
                },
                success(resp) {
                    if (resp.isSuccess) {
                        appointmentUtils.$getDataTable().ajax.reload();
                    } else {
                        alert(resp.message);
                    }
                },
                complete(status, xhr) {
                    removeLoadingOverlay(appointmentUtils.$getTable());
                }
            });
        }
    },

    /**
     * Other utils
     */
    renderStatusBadege(status) {
        switch (status) {
            case 0:
                return `<div class="btn bg-warning-light btn-sm">Pending</div>`;
            case 1:
                return `<div class="btn bg-info-light btn-sm">Confirm</div>`;
            case 2:
                return `<div class="btn bg-success-light btn-sm">Completed</div>`;
            case 3:
                return `<div class="btn bg-danger-light btn-sm">Cancelled</div>`;
        }
    },
    renderStatusPill(status) {
        switch (status) {
            case 0:
                return `<span class="badge badge-pill bg-warning-light">Pending</span>`;
            case 1:
                return `<span class="badge badge-pill bg-info-light">Confirm</span>`;
            case 2:
                return `<span class="badge badge-pill bg-success-light">Completed</span>`;
            case 3:
                return `<span class="badge badge-pill bg-danger-light">Cancelled</span>`;
        }
    },
    renderLogContent(content) {
        return content.replace(/\[([^\]]+)\]/g, '<mark>$1</mark>');
    },
    renderUserTd(user) {
        return `<h2 class="table-avatar">` +
            `<a href="${user.url}" class="avatar avatar-sm mr-2">` +
                `<img class="avatar-img rounded-circle" src="${user.avatar}" alt="${user.title}">` +
            `</a>` +
            `<a href="${user.url}">${user.title}<span>${user.subtitle}</span></a>` +
        `</h2>`;
    },
}
