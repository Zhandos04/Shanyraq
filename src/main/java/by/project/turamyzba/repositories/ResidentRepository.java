package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findAllByAnnouncementId(Long announcementId);
}
