package by.project.turamyzba.repositories.anketa;

import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.anketa.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    Optional<UserStatus> findByUser(User user);
}
