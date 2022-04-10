package vn.aptech.doccure.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.doccure.common.Constants;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("doctor")
public class DoctorController {

}
