package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.AnnouncementResident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementResidentRepository extends JpaRepository<AnnouncementResident, Long> {
    List<AnnouncementResident> findAllByAnnouncement(Announcement announcement);
}
