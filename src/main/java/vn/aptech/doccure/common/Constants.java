package vn.aptech.doccure.common;

public interface Constants {
    long MAX_FILE_SIZE = 1048576; // 2097152 = 2mb

    long TEN_MINUTES = (10 * 60 * 1000);

    interface Roles {
        String ROLE_ADMIN = "ROLE_ADMIN";
        String ROLE_DOCTOR = "ROLE_DOCTOR";
        String ROLE_PATIENT = "ROLE_PATIENT";
    }

    interface Genders {
        short FEMALE = 0;
        short MALE = 1;
    }

    interface MESSSAGE {
        String ERROR = "errorMessage";
        String SUCCESS = "successMessage";
    }

    interface OBJECT {
        String SPECIALITY = "speciality";
        String SPECIALITIES = "specialities";
        String SERVICE = "service";
        String SERVICES = "services";
    }

    interface PAGE_VIEW {
        interface ADMIN {
            interface SPECIALITIES {
                String LIST_PAGE = "admin/pages/specialities/specialities-list";
                String NEW_PAGE = "admin/pages/specialities/specialities-new";
                String EDIT_PAGE = "admin/pages/specialities/specialities-edit";
            }
            interface SERVICES {
                String LIST_PAGE = "admin/pages/services/service-list";
                String NEW_PAGE = "admin/pages/services/service-new";
                String EDIT_PAGE = "admin/pages/services/service-edit";
            }
        }
    }
}