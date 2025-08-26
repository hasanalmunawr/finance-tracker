package hasanalmunawr.dev.backend_spring.user.controller;

import hasanalmunawr.dev.backend_spring.base.constants.Endpoint;
import hasanalmunawr.dev.backend_spring.user.api.request.UpdatePasswordRequest;
import hasanalmunawr.dev.backend_spring.user.api.request.UpdateProfileRequest;
import hasanalmunawr.dev.backend_spring.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.Base.USER)
@Tag(name = "User Controller", description = "API for managing user related operations.")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping(Endpoint.User.PROFILE)
    public ResponseEntity<?> profile() {
        return userService.profile();
    }

    @PostMapping(Endpoint.User.UPDATE_PROFILE)
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(request);
    }

    @PostMapping(Endpoint.User.UPDATE_PASSWORD)
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        log.info("oldPassword = {}", request.getOldPassword());
        log.info("newPassword = {}", request.getNewPassword());

        log.info(request.toString()); // ller.UserController     : UpdatePasswordRequest(oldPassword=null, newPassword=null)
        return userService.updatePassword(request);
    }

}
