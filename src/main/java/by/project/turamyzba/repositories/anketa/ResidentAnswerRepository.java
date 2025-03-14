package by.project.turamyzba.repositories.anketa;

import by.project.turamyzba.entities.Resident;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.anketa.ResidentAnswer;
import by.project.turamyzba.entities.anketa.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentAnswerRepository extends JpaRepository<ResidentAnswer, Long> {
    List<ResidentAnswer> findAllByResident(Resident resident);

}
