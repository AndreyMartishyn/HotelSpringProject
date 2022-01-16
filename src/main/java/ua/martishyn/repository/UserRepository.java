package ua.martishyn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.martishyn.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
