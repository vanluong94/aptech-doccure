package vn.aptech.doccure.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Review;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.ReviewService;

@Controller
@RequestMapping("/dashboard/doctor-reviews")
@Secured(Constants.Roles.ROLE_DOCTOR)
public class DoctorReviewController {
    private final Logger logger = LoggerFactory.getLogger(DoctorReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ModelAndView reviews(Authentication auth) {
        User user = (User) auth.getPrincipal();
        Iterable<Review> reviews = reviewService.findAllByDoctorId(user.getId());
        ModelAndView modelAndView = new ModelAndView("/pages/doctor/doctor-reviews");
        modelAndView.addObject("reviews", reviews);
        return modelAndView;
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") Long id, RedirectAttributes redirect) {
        try {
            reviewService.deleteById(id);
            redirect.addFlashAttribute(Constants.MESSAGE.SUCCESS, "The item has been successfully deleted.");
        } catch (Exception e) {
            logger.error("Exception when /doctor/reviews/delete", e);
            redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "The selected item cannot be deleted. Details: " + e.getMessage());
        }
        return "redirect:/dashboard/doctor-reviews";
    }
}
