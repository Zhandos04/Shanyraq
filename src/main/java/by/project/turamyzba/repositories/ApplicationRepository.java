package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByApplicationBatchId(String applicationBatchId);
}
