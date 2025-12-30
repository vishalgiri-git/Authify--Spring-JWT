package in.auth_app.authify.ServiceImpl;

import in.auth_app.authify.entity.UserEntity;
import in.auth_app.authify.enums.Role;
import in.auth_app.authify.io.ProfileRequest;
import in.auth_app.authify.io.ProfileResponse;
import in.auth_app.authify.repository.UserRepository;
import in.auth_app.authify.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    @Override
    public ProfileResponse createProfile(ProfileRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exist");
        }
        request.setPassword(encoder.encode(request.getPassword()));
        request.setRole(Role.ROLE_USER); // default role user
        UserEntity newProfile = convertToUserEntity(request);
        UserEntity savedProfile = userRepository.save(newProfile);
        return convertToProfileResponse(savedProfile);

    }

    @Override
    public List<ProfileResponse> getAllProfile() {

        List<UserEntity> all = userRepository.findAll();
        return all.stream().map(this::convertToProfileResponse).toList();

    }

    @Override
    public String deleteProfileByEmail(String email) {
            Optional<UserEntity> byEmail = userRepository.findByEmail(email);

            if(byEmail.isPresent()) {
                userRepository.deleteByEmail(email);
                return "Profile with email " + email + " deleted successfully.";
            }
            else
                return "Profile with email " + email + " Not Exist." ;

    }
    @Override
    public ResponseEntity<ProfileResponse> updateProfile(
            String email, ProfileRequest request) {
        UserEntity byEmail = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));

        if(request.getName() != null)
            byEmail.setName(request.getName());

        if(request.getPassword() != null) {
            String encodedPassword = encoder.encode(request.getPassword());
            byEmail.setPassword(encodedPassword);
        }

        UserEntity savedUserEntity = userRepository.save(byEmail);
        ProfileResponse response = convertToProfileResponse(savedUserEntity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    };


    private ProfileResponse convertToProfileResponse(UserEntity savedProfile) {
        return ProfileResponse
                .builder()
                .userId(savedProfile.getUserId())
                .name(savedProfile.getName())
                .email(savedProfile.getEmail())
                .role(savedProfile.getRole())
                .isAccountVerified(savedProfile.isAccountVerified())
                .build();

    }

    private UserEntity convertToUserEntity(ProfileRequest request) {
        return UserEntity
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                        .password(request.getPassword())
                .role(request.getRole())
                                .userId(UUID.randomUUID().toString())
                                        .isAccountVerified(false)
                .build();



    }


}
