package by.project.turamyzba.repositories.anketa;

import by.project.turamyzba.entities.anketa.ResidentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentAnswerRepository extends JpaRepository<ResidentAnswer, Long> {
}
