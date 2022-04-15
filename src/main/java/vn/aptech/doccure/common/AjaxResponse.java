package vn.aptech.doccure.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class AjaxResponse {
    public static ResponseEntity<Object> response(HttpStatus status, boolean error, String message, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("status", status.value());
            map.put("isSuccess", !error);
            map.put("message", message);
            map.put("data", responseObj);
            return new ResponseEntity<Object>(map,status);
        } catch (Exception e) {
            map.clear();
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("isSuccess",false);
            map.put("message", e.getMessage());
            map.put("data", null);
            return new ResponseEntity<Object>(map,status);
        }
    }

    public static ResponseEntity<Object> responseSuccess(Object responseObj, String message) {
        return response(HttpStatus.OK, false, message, responseObj);
    }

    public static ResponseEntity<Object> responseFail(Object respObj, String message) {
        return response(HttpStatus.FORBIDDEN, true, message, respObj);
    }
}
