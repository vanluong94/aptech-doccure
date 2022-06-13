package vn.aptech.doccure.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Review;
import vn.aptech.doccure.service.ReviewService;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {
    private final Logger logger = LoggerFactory.getLogger(AdminReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ModelAndView reviews() {
        Iterable<Review> reviews = reviewService.findAll();
        ModelAndView modelAndView = new ModelAndView("admin/pages/reviews/reviews");
        modelAndView.addObject("reviews", reviews);
        return modelAndView;
    }

    @PostMapping("/delete")
    public String update(@RequestParam("id") Long id, RedirectAttributes redirect) {
        try {
            reviewService.deleteById(id);
            redirect.addFlashAttribute(Constants.MESSAGE.SUCCESS, "The item has been successfully deleted.");
        } catch (Exception e) {
            logger.error("Exception when /admin/reviews/delete", e);
            redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "The selected item cannot be deleted. Details: " + e.getMessage());
        }
        return "redirect:/admin/reviews";
    }
}
