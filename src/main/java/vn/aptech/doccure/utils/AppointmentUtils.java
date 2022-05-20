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

    public static String getPatientAppointmentActionsOutput(Appointment appointment) {

        StringBuilder output = new StringBuilder();

        output.append("<div class=\"btn btn-sm bg-info-light mr-1\"><i class=\"far fa-eye\"></i> View</div>");

        switch (appointment.getStatus()) {
            case Appointment.STATUS.PENDING:
                output.append("<div class=\"btn btn-sm bg-danger-light mr-1\" data-action=\"view-appointment\" data-appointment=\"" + appointment.getId() + "\"><i class=\"fas fa-times\"></i> Cancel</div>");
                break;
            case Appointment.STATUS.CANCELED:
                break;
            case Appointment.STATUS.COMPLETED:
                break;
            case Appointment.STATUS.CONFIRMED:
                break;
        }

        return output.toString();
    }

    public static String getDoctorAppointmentActionsOutput(Appointment appointment) {
        StringBuilder output = new StringBuilder();

        output.append("<div class=\"btn btn-sm bg-info-light mr-1\"><i class=\"far fa-eye\"></i> View</div>");

        switch (appointment.getStatus()) {
            case Appointment.STATUS.PENDING:
                output.append("<div class=\"btn btn-sm bg-success-light mr-1\"><i class=\"fas fa-check\"></i> Confirm</div>");
                output.append("<div class=\"btn btn-sm bg-danger-light mr-1\"><i class=\"fas fa-times\"></i> Cancel</div>");
                break;
            case Appointment.STATUS.CANCELED:
                break;
            case Appointment.STATUS.COMPLETED:
                break;
            case Appointment.STATUS.CONFIRMED:
                break;
        }

        return output.toString();
    }

}
