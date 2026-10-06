package in.gracy.removebg.repository;

import in.gracy.removebg.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByClerkId(String clerkId);

    Optional<UserEntity> findByClerkId(String clerkId);

    Optional<UserEntity> findByEmail(String email);

}