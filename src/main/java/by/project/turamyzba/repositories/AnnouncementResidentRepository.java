package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.AnnouncementResident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementResidentRepository extends JpaRepository<AnnouncementResident, Long> {
}
