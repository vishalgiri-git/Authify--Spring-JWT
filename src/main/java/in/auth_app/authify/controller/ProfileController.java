package in.auth_app.authify.controller;

import in.auth_app.authify.entity.UserEntity;
import in.auth_app.authify.io.ProfileRequest;
import in.auth_app.authify.io.ProfileResponse;
import in.auth_app.authify.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableMethodSecurity
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody ProfileRequest request){

        return profileService.createProfile(request);
    }

    // update profile fields like name, password etc.
    @PatchMapping("update/{email}")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable(name="email") String email,
                                                         @RequestBody ProfileRequest request){
        return profileService.updateProfile(email, request);

    }


    // delete profile by mail id.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{email}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteProfile(@PathVariable(name = "email") String email){
        return profileService.deleteProfileByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileResponse> getAllProfiles(){
        return profileService.getAllProfile();

    };

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/about/{email}")
    public UserEntity accessMyProfile(@PathVariable String email){
        return profileService.accessMyProfile(email);
    }
    // welcome page for authenticated users or admin.
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to Authify! You have successfully accessed a public resource.";
    }



}
