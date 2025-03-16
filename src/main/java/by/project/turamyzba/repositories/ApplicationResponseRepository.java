package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.ApplicationResponse;
import by.project.turamyzba.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationResponseRepository extends JpaRepository<ApplicationResponse, Long> {
    List<ApplicationResponse> findAllByGroup(Group group);
}
