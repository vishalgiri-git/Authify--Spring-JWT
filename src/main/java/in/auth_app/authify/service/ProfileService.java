package in.auth_app.authify.service;

import in.auth_app.authify.io.ProfileRequest;
import in.auth_app.authify.io.ProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);
    List<ProfileResponse> getAllProfile();

    String deleteProfileByEmail(String email);

    ResponseEntity<ProfileResponse> updateProfile(String email, ProfileRequest request);
}
