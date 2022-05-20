package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.PatientFavorite;
import vn.aptech.doccure.entities.PatientFavoriteId;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.PatientFavoriteService;
import vn.aptech.doccure.service.UserService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/ajax/favorite")
public class FavoriteController {

    @Autowired
    private UserService userService;

    @Autowired
    private PatientFavoriteService favoriteService;

    @PostMapping("/{doctorId}")
    @ResponseBody
    public ResponseEntity<Object> bookmarkToggle(@PathVariable Long doctorId, Authentication authentication) {

        Map<String, Object> response = new LinkedHashMap<>();

        if (authentication == null) {
            return AjaxResponse.responseFail(response, "Login is required to perform this action");
        }

        Optional<User> doctorResult =  userService.findById(doctorId);

        if (!doctorResult.isPresent()) {
            return AjaxResponse.responseFail(response, "doctor not found");
        }

        User doctor = doctorResult.get();
        User patient = (User) authentication.getPrincipal();

        PatientFavoriteId favoriteId = new PatientFavoriteId(doctor, patient);

        Optional<PatientFavorite> favoriteResult = favoriteService.findById(favoriteId);
        boolean isFavorite;

        if (favoriteResult.isPresent()) {
            favoriteService.delete(favoriteResult.get());
            isFavorite = false;
        } else {
            PatientFavorite favorite = new PatientFavorite(favoriteId);
            favoriteService.saveAndFlush(favorite);
            isFavorite = true;
        }

        response.put("isFavorite", isFavorite);

        return AjaxResponse.responseSuccess(response, "success");
    }

}
