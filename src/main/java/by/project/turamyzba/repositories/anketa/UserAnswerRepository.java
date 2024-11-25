package by.project.turamyzba.repositories.anketa;

import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.anketa.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    void deleteAllByUser(User user);
}
