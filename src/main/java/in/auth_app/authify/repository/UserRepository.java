package in.auth_app.authify.repository;

import in.auth_app.authify.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    // user
    //Optional<UserEntity> findByUsername(String username);
}
