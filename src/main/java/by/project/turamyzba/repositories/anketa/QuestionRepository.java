package by.project.turamyzba.repositories.anketa;

import by.project.turamyzba.entities.anketa.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
