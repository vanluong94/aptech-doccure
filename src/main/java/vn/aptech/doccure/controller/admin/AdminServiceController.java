package vn.aptech.doccure.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.service.ServiceService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.utils.StringUtils;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@Controller
@RequestMapping("/admin/services")
@RolesAllowed("ROLE_ADMIN")
public class AdminServiceController {

    private final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView getServiceList() {
        ModelAndView modelAndView = new ModelAndView("/admin/pages/services/service-list");
        modelAndView.addObject("services", serviceService.findAll());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        Service service = new Service();
        ModelAndView modelAndView = new ModelAndView(Constants.PAGE_VIEW.ADMIN.SERVICES.NEW_PAGE);
        modelAndView.addObject(Constants.OBJECT.SERVICE, service);
        return modelAndView;
    }

    @PostMapping("/create")
    public String addNewService(@Validated @ModelAttribute("service") Service service, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return Constants.PAGE_VIEW.ADMIN.SERVICES.NEW_PAGE;
        }
        service.setSlug(StringUtils.toNoSign(service.getName()).toLowerCase());
        try {
            Service serviceSave = serviceService.save(service);
            if (serviceSave != null) {
                redirect.addFlashAttribute(Constants.MESSAGE.SUCCESS, "The item has been created successfully.");
            } else {
                redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "Cannot create item " + service.getName() + ".");
            }
        } catch (Exception e) {
            logger.error("Exception when /admin/services/create", e);
            redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "Cannot create item " + service.getName() + ". Details: " + e.getMessage());
        }
        return "redirect:/admin/services";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") Long id, RedirectAttributes redirect) {
        Optional<Service> service = serviceService.findById(id);
        ModelAndView modelAndView = new ModelAndView("admin/pages/services/service-edit");
        if (service.isPresent()) {
            modelAndView.addObject("editService", service.get());
        } else {
            modelAndView.addObject("editService", new Service());
            modelAndView.addObject("errorMessage", "Service not found");
        }
        return modelAndView;
    }

    @PostMapping("/edit")
    public String update(@Validated @ModelAttribute("editService") Service service, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "admin/pages/services/service-edit";
        }
        try {
            Service serviceSave = serviceService.save(service);
            if (serviceSave != null) {
                redirect.addFlashAttribute("successMessage", "Update successfully.");
            } else {
                redirect.addFlashAttribute("errorMessage", "Error.");
            }
        } catch (Exception e) {
            logger.error("Exception when /admin/services/edit", e);
            redirect.addFlashAttribute("errorMessage", "Error. Details: " + e.getMessage());
        }
        return "redirect:/admin/services/edit/" + service.getId();
    }

    @PostMapping("/delete")
    public String update(@RequestParam("id") Long id, RedirectAttributes redirect) {
        serviceService.deleteById(id);
        redirect.addFlashAttribute("successMessage", "Successfully deleted a service");
        return "redirect:/admin/services";
    }
}
