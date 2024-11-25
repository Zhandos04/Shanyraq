package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Anketa;
import by.project.turamyzba.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnketaRepository extends JpaRepository<Anketa, Long> {
    List<Anketa> findAllByUser(User user);
}
