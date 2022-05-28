package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.entities.Review;
import vn.aptech.doccure.service.ReviewService;

@Controller
public class AdminReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/admin/reviews")
    public ModelAndView reviews() {
        Iterable<Review> reviews = reviewService.findAll();
        ModelAndView modelAndView = new ModelAndView("admin/pages/reviews/reviews");
        modelAndView.addObject("reviews", reviews);
        return modelAndView;
    }

    @PostMapping("/admin/reviews/delete")
    public String update(@RequestParam("id") Long id, RedirectAttributes redirect) {
        try {
            reviewService.deleteById(id);
            redirect.addFlashAttribute("successMessage", "Successfully deleted a review");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "Can't not delete");
        }
        return "redirect:/admin/reviews";
    }
}
