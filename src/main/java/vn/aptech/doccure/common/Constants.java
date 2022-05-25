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
}