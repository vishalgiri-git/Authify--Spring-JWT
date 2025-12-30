package in.auth_app.authify.app_config;

import in.auth_app.authify.entity.UserEntity;
import in.auth_app.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("inside LoaduserByUsername method");
        Optional<UserEntity> byEmail = userRepository.findByEmail(username);
        if(byEmail.isPresent()){
            log.info("inside loadByUsername if Block");
            UserEntity user = byEmail.get();

            return User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                   // .roles(user.getRole().name())
                    .roles(user.getRole().name().replace("ROLE_", "")) // Removing ROLE_ prefix
                    .build();
        }
        log.info("loadByUsername methods else block");
        throw new UsernameNotFoundException("User not found with username" + username);
    }
}
