package vn.aptech.doccure.utils;

import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.User;

public class AppointmentUtils {

    public static String getDoctorItemOutput(User doctor) {
        return "<h2 class=\"table-avatar\">" +
                "<a href=\"" + DoctorUtils.getDoctorProfileUrl(doctor) + "\" class=\"avatar avatar-sm mr-2\">" +
                "<img class=\"avatar-img rounded-circle\" src=\"" + doctor.getTheAvatar() + "\" alt=\"User Image\">" +
                "</a>" +
                "<a href=\"" + DoctorUtils.getDoctorProfileUrl(doctor) + "\">" + doctor.getDoctorTitle() + " <span>Dental</span></a>" +
                "</h2>";
    }

    public static String getStatusBadgeOutput(Appointment appointment) {

        switch (appointment.getStatus()) {
            case Appointment.STATUS.QUEUE:
                return "<span class=\"badge badge-pill bg-secondary text-light\">Confirm</span>";
            case Appointment.STATUS.PENDING:
                return "<span class=\"badge badge-pill bg-warning-light\">Pending</span>";
            case Appointment.STATUS.CANCELED:
                return "<span class=\"badge badge-pill bg-danger-light\">Cancelled</span>";
            case Appointment.STATUS.COMPLETED:
                return "<span class=\"badge badge-pill bg-info-light\">Confirm</span>";
            case Appointment.STATUS.CONFIRMED:
                return "<span class=\"badge badge-pill bg-success-light\">Confirm</span>";
            default:
                return "";
        }

    }

    public static String getAppointmentActionsOutput(Appointment appointment) {
        StringBuilder output = new StringBuilder();
        return output.toString();
    }

}
