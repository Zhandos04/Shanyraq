package by.project.turamyzba.repositories;

import by.project.turamyzba.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    User getUserById(Long id);
    Boolean existsByEmail(String email);
    Boolean existsByNickName(String nickName);
}
